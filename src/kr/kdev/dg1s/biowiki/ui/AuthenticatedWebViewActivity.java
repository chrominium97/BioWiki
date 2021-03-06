package kr.kdev.dg1s.biowiki.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wordpress.passcodelock.AppLockManager;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Map;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.Blog;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.BWWebChromeClient;
import kr.kdev.dg1s.biowiki.util.BWWebViewClient;

/**
 * Activity for displaying WordPress content in a webview which may require authentication.
 * Currently, this activity can only load content for the {@link kr.kdev.dg1s.biowiki.BioWiki.currentBlog}.
 */
public class AuthenticatedWebViewActivity extends WebViewActivity {

    public static final String LOAD_AUTHENTICATED_URL = "loadAuthenticatedUrl";

    /**
     * Blog for which this activity is loading content.
     */
    protected Blog mBlog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBlog = BioWiki.getCurrentBlog();
        if (mBlog == null) {
            Toast.makeText(this, getResources().getText(R.string.blog_not_found),
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        mWebView.setWebViewClient(new BWWebViewClient(mBlog));

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setSavePassword(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(LOAD_AUTHENTICATED_URL)) {
            String authUrl = extras.getString(LOAD_AUTHENTICATED_URL);
            loadAuthenticatedUrl(authUrl);
        }
        mWebView.setWebChromeClient(new BWWebChromeClient(this, (ProgressBar) findViewById(R.id.progress_bar)));
    }

    /**
     * Get the URL of the WordPress login page.
     *
     * @return URL of the login page.
     */
    protected String getLoginUrl() {
        String loginURL = null;
        Gson gson = new Gson();
        Type type = new TypeToken<Map<?, ?>>() {
        }.getType();
        Map<?, ?> blogOptions = gson.fromJson(mBlog.getBlogOptions(), type);
        if (blogOptions != null) {
            Map<?, ?> homeURLMap = (Map<?, ?>) blogOptions.get("login_url");
            if (homeURLMap != null)
                loginURL = homeURLMap.get("value").toString();
        }
        // Try to guess the login URL if blogOptions is null (blog not added to the app), or WP version is < 3.6
        if (loginURL == null) {
            if (mBlog.getUrl().lastIndexOf("/") != -1) {
                return mBlog.getUrl().substring(0, mBlog.getUrl().lastIndexOf("/"))
                        + "/wp-login.php";
            } else {
                return mBlog.getUrl().replace("xmlrpc.php", "wp-login.php");
            }
        }

        return loginURL;
    }

    /**
     * Login to the BioWiki blog and load the specified URL.
     *
     * @param url URL to be loaded in the webview.
     */
    protected void loadAuthenticatedUrl(String url) {
        try {
            String postData = String.format("log=%s&pwd=%s&redirect_to=%s",
                    URLEncoder.encode(mBlog.getUsername(), "UTF-8"), URLEncoder.encode(mBlog.getPassword(), "UTF-8"),
                    URLEncoder.encode(url, "UTF-8"));
            mWebView.postUrl(getLoginUrl(), postData.getBytes());
        } catch (UnsupportedEncodingException e) {
            AppLog.e(AppLog.T.UTILS, e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mWebView == null)
            return false;

        int itemID = item.getItemId();
        if (itemID == R.id.menu_refresh) {
            mWebView.reload();
            return true;
        } else if (itemID == R.id.menu_share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
            startActivity(Intent.createChooser(share, getResources().getText(R.string.share_link)));
            return true;
        } else if (itemID == R.id.menu_browser) {
            String url = mWebView.getUrl();
            if (url != null) {
                Uri uri = Uri.parse(url);
                if (uri != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(uri);
                    startActivity(i);
                    AppLockManager.getInstance().setExtendedTimeout();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
