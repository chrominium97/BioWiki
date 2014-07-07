package kr.kdev.dg1s.biowiki;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class Constants {

    public static final DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageOnLoading(R.drawable.remote_image)
            .showImageOnFail(R.drawable.remote_failed)
            .build();

    public static final String URL_TOS = "http://en.wordpress.com/tos";
    /*
     * Reader constants
     */
    public static final int READER_MAX_POSTS_TO_REQUEST = 20;                          // max #posts to request when updating posts (should be an even # to avoid "hanging post" in 2-column grid mode)
    public static final int READER_MAX_COMMENTS_TO_REQUEST = READER_MAX_POSTS_TO_REQUEST; // max #comments to request when updating comments
    public static final int READER_MAX_POSTS_TO_DISPLAY = 200;                         // max #posts to display in ReaderPostListFragment
    public static final int READER_MAX_USERS_TO_DISPLAY = 500;                         // max #users to show in ReaderUserListActivity
    public static final long READER_AUTO_UPDATE_DELAY_MINUTES = 15;                       // 15 minute delay between automatic updates
    // intent IDs
    public static final int INTENT_READER_TAGS = 1000;
    public static final int INTENT_READER_REBLOG = 1001;
    public static final int INTENT_COMMENT_EDITOR = 1010;
    public static String readerURL = "https://en.wordpress.com/reader/mobile/v2";
    public static String readerLoginURL = "https://wordpress.com/wp-login.php";
    public static String readerURL_v3 = "https://en.wordpress.com/reader/mobile/v2/?chrome=no";
    public static String authorizedHybridHost = "en.wordpress.com";
    public static String readerTopicsURL = "http://en.wordpress.com/reader/mobile/v2/?template=topics";
    public static String wpcomXMLRPCURL = "https://wordpress.com/xmlrpc.php";
    public static String videoPressURL = "http://videopress.com";
    public static int QUICK_POST_PHOTO_CAMERA = 0;
    public static int QUICK_POST_PHOTO_LIBRARY = 1;
    public static int QUICK_POST_VIDEO_CAMERA = 2;
    public static int QUICK_POST_VIDEO_LIBRARY = 3;

}
