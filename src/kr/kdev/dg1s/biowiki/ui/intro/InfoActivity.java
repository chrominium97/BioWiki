package kr.kdev.dg1s.biowiki.ui.intro;

import android.os.Bundle;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;

public class InfoActivity extends BIActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.intro_info);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
