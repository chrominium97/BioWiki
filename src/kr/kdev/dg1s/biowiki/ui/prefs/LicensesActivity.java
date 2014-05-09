package kr.kdev.dg1s.biowiki.ui.prefs;

import android.os.Bundle;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.WebViewActivity;

/**
 * Display open source licenses for the application.
 */
public class LicensesActivity extends WebViewActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getText(R.string.open_source_licenses));
        loadUrl("file:///android_asset/licenses.html");
    }

}
