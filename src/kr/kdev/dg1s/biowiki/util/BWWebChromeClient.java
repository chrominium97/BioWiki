package kr.kdev.dg1s.biowiki.util;

import android.app.Activity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class BWWebChromeClient extends WebChromeClient {
    private ProgressBar mProgressBar;
    private Activity mActivity;

    public BWWebChromeClient(Activity activity, ProgressBar progressBar) {
        mProgressBar = progressBar;
        mActivity = activity;
    }

    public void onProgressChanged(WebView webView, int progress) {
        if (!mActivity.isFinishing()) {
            mActivity.setTitle(webView.getTitle());
        }
        if (progress == 100) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(progress);
        }
    }
}