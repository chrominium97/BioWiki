package kr.kdev.dg1s.biowiki.ui.notifications;

import com.android.volley.VolleyError;
import com.wordpress.rest.RestRequest.ErrorListener;
import com.wordpress.rest.RestRequest.Listener;

import org.json.JSONObject;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.BioWiki;

class FollowListener implements FollowRow.OnFollowListener {
    private final int mNoteId;

    public FollowListener(int noteId) {
        super();
        mNoteId = noteId;
    }

    class FollowResponseHandler implements Listener, ErrorListener {
        private final FollowRow mRow;
        private final String mSiteId;
        private final boolean mShouldFollow;

        FollowResponseHandler(FollowRow row, String siteId, boolean shouldFollow) {
            mRow = row;
            mSiteId = siteId;
            mShouldFollow = shouldFollow;
            disableFollowButton();
        }

        @Override
        public void onResponse(JSONObject response) {
            if (mRow.isSiteId(mSiteId)) {
                mRow.setFollowing(mShouldFollow);
            }
            enableFollowButton();

            // update the associated note so it has the correct follow status
            NotificationUtils.updateNotification(mNoteId, null);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            enableFollowButton();
            AppLog.d(AppLog.T.NOTIFS, String.format("Failed to follow the blog: %s ", error));
        }

        public void disableFollowButton() {
            if (mRow.isSiteId(mSiteId)) {
                mRow.getFollowButton().setEnabled(false);
            }
        }

        public void enableFollowButton() {
            if (mRow.isSiteId(mSiteId)) {
                mRow.getFollowButton().setEnabled(true);
            }
        }
    }

    @Override
    public void onFollow(final FollowRow row, final String siteId) {
        FollowResponseHandler handler = new FollowResponseHandler(row, siteId, true);
        BioWiki.getRestClientUtils().followSite(siteId, handler, handler);
    }

    @Override
    public void onUnfollow(final FollowRow row, final String siteId) {
        FollowResponseHandler handler = new FollowResponseHandler(row, siteId, false);
        BioWiki.getRestClientUtils().unfollowSite(siteId, handler, handler);
    }
}