package kr.kdev.dg1s.biowiki.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlrpc.android.ApiHelper;
import org.xmlrpc.android.ApiHelper.ErrorType;
import org.xmlrpc.android.ApiHelper.GetMediaItemTask;

import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.MediaFile;

/**
 * A service for uploading media files from the media browser.
 * Only one file is uploaded at a time.
 */
public class MediaUploadService extends Service {
    /**
     * Listen to this Intent for when there are updates to the upload queue *
     */
    public static final String MEDIA_UPLOAD_INTENT_NOTIFICATION = "MEDIA_UPLOAD_INTENT_NOTIFICATION";
    public static final String MEDIA_UPLOAD_INTENT_NOTIFICATION_EXTRA = "MEDIA_UPLOAD_INTENT_NOTIFICATION_EXTRA";
    public static final String MEDIA_UPLOAD_INTENT_NOTIFICATION_ERROR = "MEDIA_UPLOAD_INTENT_NOTIFICATION_ERROR";
    // time to wait before trying to upload the next file
    private static final int UPLOAD_WAIT_TIME = 1000;
    private Context mContext;
    private Handler mHandler = new Handler();
    private boolean mUploadInProgress;
    private Runnable mFetchQueueTask = new Runnable() {

        @Override
        public void run() {
            Cursor cursor = getQueue();
            try {
                if ((cursor == null || cursor.getCount() == 0 || mContext == null) && !mUploadInProgress) {
                    MediaUploadService.this.stopSelf();
                    return;
                } else {
                    if (mUploadInProgress) {
                        mHandler.postDelayed(this, UPLOAD_WAIT_TIME);
                    } else {
                        uploadMediaFile(cursor);
                    }
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();
        mUploadInProgress = false;

        cancelOldUploads();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        mHandler.post(mFetchQueueTask);
    }

    private void cancelOldUploads() {
        // There should be no media files with an upload state of 'uploading' at the start of this service.
        // Since we won't be able to receive notifications for these, set them to 'failed'.

        if (BioWiki.getCurrentBlog() != null) {
            String blogId = String.valueOf(BioWiki.getCurrentBlog().getLocalTableBlogId());
            BioWiki.wpDB.setMediaUploadingToFailed(blogId);
            sendUpdateBroadcast(null, null);
        }
    }

    private Cursor getQueue() {
        if (BioWiki.getCurrentBlog() == null)
            return null;

        String blogId = String.valueOf(BioWiki.getCurrentBlog().getLocalTableBlogId());
        return BioWiki.wpDB.getMediaUploadQueue(blogId);
    }

    private void uploadMediaFile(Cursor cursor) {
        if (!cursor.moveToFirst())
            return;

        mUploadInProgress = true;

        final String blogIdStr = cursor.getString((cursor.getColumnIndex("blogId")));
        final String mediaId = cursor.getString(cursor.getColumnIndex("mediaId"));
        String fileName = cursor.getString(cursor.getColumnIndex("fileName"));
        String filePath = cursor.getString(cursor.getColumnIndex("filePath"));
        String mimeType = cursor.getString(cursor.getColumnIndex("mimeType"));

        MediaFile mediaFile = new MediaFile();
        mediaFile.setBlogId(blogIdStr);
        mediaFile.setFileName(fileName);
        mediaFile.setFilePath(filePath);
        mediaFile.setMimeType(mimeType);

        ApiHelper.UploadMediaTask task = new ApiHelper.UploadMediaTask(mContext, mediaFile,
                new ApiHelper.UploadMediaTask.Callback() {
                    @Override
                    public void onSuccess(String id) {
                        // once the file has been uploaded, delete the local database entry and
                        // download the new one so that we are up-to-date and so that users can edit it.
                        BioWiki.wpDB.deleteMediaFile(blogIdStr, mediaId);
                        sendUpdateBroadcast(mediaId, null);
                        fetchMediaFile(id);
                    }

                    @Override
                    public void onFailure(ApiHelper.ErrorType errorType, String errorMessage, Throwable throwable) {
                        BioWiki.wpDB.updateMediaUploadState(blogIdStr, mediaId, "failed");
                        mUploadInProgress = false;
                        sendUpdateBroadcast(mediaId, getString(R.string.upload_failed));
                        mHandler.post(mFetchQueueTask);
                        // Only log the error if it's not caused by the network (internal inconsistency)
                        if (errorType != ErrorType.NETWORK_XMLRPC) {
                            JSONObject properties = new JSONObject();
                            try {
                                properties.put("error_message", errorType.name() + "-" + errorMessage);
                            } catch (JSONException e) {
                                AppLog.e(AppLog.T.MEDIA, "Can't serialize message to JSON: " + errorMessage);
                            }
                            BWMobileStatsUtil.trackException(throwable, BWMobileStatsUtil.StatsPropertyExceptionUploadMedia,
                                    properties);
                        }
                    }
                }
        );

        BioWiki.wpDB.updateMediaUploadState(blogIdStr, mediaId, "uploading");
        sendUpdateBroadcast(mediaId, null);
        List<Object> apiArgs = new ArrayList<Object>();
        apiArgs.add(BioWiki.getCurrentBlog());
        task.execute(apiArgs);
        mHandler.post(mFetchQueueTask);
    }

    private void fetchMediaFile(final String id) {
        List<Object> apiArgs = new ArrayList<Object>();
        apiArgs.add(BioWiki.getCurrentBlog());
        GetMediaItemTask task = new GetMediaItemTask(Integer.valueOf(id),
                new ApiHelper.GetMediaItemTask.Callback() {
                    @Override
                    public void onSuccess(MediaFile mediaFile) {
                        String blogId = mediaFile.getBlogId();
                        String mediaId = mediaFile.getMediaId();
                        BioWiki.wpDB.updateMediaUploadState(blogId, mediaId, "uploaded");
                        mUploadInProgress = false;
                        sendUpdateBroadcast(id, null);
                        mHandler.post(mFetchQueueTask);
                    }

                    @Override
                    public void onFailure(ApiHelper.ErrorType errorType, String errorMessage, Throwable throwable) {
                        mUploadInProgress = false;
                        sendUpdateBroadcast(id, getString(R.string.error_refresh_media));
                        mHandler.post(mFetchQueueTask);
                        // Only log the error if it's not caused by the network (internal inconsistency)
                        if (errorType != ErrorType.NETWORK_XMLRPC) {
                            JSONObject properties = new JSONObject();
                            try {
                                properties.put("error_message", errorType.name() + "-" + errorMessage);
                            } catch (JSONException e) {
                                AppLog.e(AppLog.T.MEDIA, "Can't serialize message to JSON: " + errorMessage);
                            }
                            BWMobileStatsUtil.trackException(throwable, BWMobileStatsUtil.StatsPropertyExceptionFetchMedia,
                                    properties);
                        }
                    }
                }
        );
        task.execute(apiArgs);
    }

    private void sendUpdateBroadcast(String mediaId, String errorMessage) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(mContext);
        Intent intent = new Intent(MEDIA_UPLOAD_INTENT_NOTIFICATION);
        if (mediaId != null) {
            intent.putExtra(MEDIA_UPLOAD_INTENT_NOTIFICATION_EXTRA, mediaId);
        }
        if (errorMessage != null) {
            intent.putExtra(MEDIA_UPLOAD_INTENT_NOTIFICATION_ERROR, errorMessage);
        }
        lbm.sendBroadcast(intent);
    }

}
