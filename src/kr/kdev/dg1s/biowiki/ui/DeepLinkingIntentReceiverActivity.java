package kr.kdev.dg1s.biowiki.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.ToastUtils;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.accounts.WelcomeActivity;

/**
 * An activity to handle deep linking. 
 * 
 * wordpress://viewpost?blogId={blogId}&postId={postId}
 * 
 * Redirects users to the reader activity along with IDs passed in the intent
 */
public class DeepLinkingIntentReceiverActivity extends Activity {
    private static final int INTENT_WELCOME = 0;

    private String mBlogId;
    private String mPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent().getAction();
        Uri uri = getIntent().getData();

        // check if this intent is started via custom scheme link
        if (Intent.ACTION_VIEW.equals(action) && uri != null) {
            mBlogId = uri.getQueryParameter("blogId");
            mPostId = uri.getQueryParameter("postId");

            // if user is logged in, show the post right away - otherwise show welcome activity
            // and then show the post once t
            // he user has logged in
            if (/*BioWiki.hasValidWPComCredentials(this)*/ false) {
                //showPost();
            } else {
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivityForResult(intent, INTENT_WELCOME);
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // show the post if user is returning from successful login
        if (requestCode == INTENT_WELCOME && resultCode == RESULT_OK);
            //showPost();
    }
/*
    private void showPost() {
        if (!TextUtils.isEmpty(mBlogId) && !TextUtils.isEmpty(mPostId)) {
            try {
                ReaderActivityLauncher.showReaderPostDetail(this, Long.parseLong(mBlogId), Long.parseLong(mPostId));
            } catch (NumberFormatException e) {
                AppLog.e(AppLog.T.READER, e);
            }
        } else {
            ToastUtils.showToast(this, R.string.error_generic);
        }
        finish();
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
