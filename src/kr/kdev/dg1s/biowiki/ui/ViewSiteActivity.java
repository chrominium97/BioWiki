package kr.kdev.dg1s.biowiki.ui;

import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;

/**
 * Activity to view the WordPress blog in a WebView
 */
public class ViewSiteActivity extends AuthenticatedWebViewActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createMenuDrawer(this.findViewById(R.id.webview_wrapper));

        this.setTitle(getResources().getText(R.string.view_site));

        // configure webview
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        loadSiteURL();
    }

    private void loadSiteURL() {
        if (mBlog == null)
            return;
        String siteURL = null;
        Gson gson = new Gson();
        Type type = new TypeToken<Map<?, ?>>() {
        }.getType();
        Map<?, ?> blogOptions = gson.fromJson(mBlog.getBlogOptions(), type);
        if (blogOptions != null) {
            Map<?, ?> homeURLMap = (Map<?, ?>) blogOptions.get("home_url");
            if (homeURLMap != null)
                siteURL = homeURLMap.get("value").toString();
        }
        // Try to guess the URL of the site if blogOptions is null (blog not added to the app)
        if (siteURL == null) {
            siteURL = mBlog.getUrl().replace("/xmlrpc.php", "");
        }
        loadAuthenticatedUrl(siteURL);
    }

    @Override
    public void onBlogChanged() {
        super.onBlogChanged();
        mBlog = BioWiki.currentBlog;
        loadSiteURL();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mMenuDrawer != null) {
                mMenuDrawer.toggleMenu();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
