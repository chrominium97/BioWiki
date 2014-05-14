package kr.kdev.dg1s.biowiki.ui.intro;

import android.os.Bundle;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BWActionBarActivity;

public class WikiActivity extends BWActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.intro_wiki);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
