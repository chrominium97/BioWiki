package kr.kdev.dg1s.biowiki.ui.intro;

import android.os.Bundle;
import android.widget.GridView;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.dictionary.ImageAdapter;

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
