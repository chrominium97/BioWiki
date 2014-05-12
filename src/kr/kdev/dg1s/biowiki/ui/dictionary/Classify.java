package kr.kdev.dg1s.biowiki.ui.dictionary;

import android.os.Bundle;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BWActionBarActivity;

/**
 * This shows how to place markers on a map.
 */
public class Classify extends BWActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.classification);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
