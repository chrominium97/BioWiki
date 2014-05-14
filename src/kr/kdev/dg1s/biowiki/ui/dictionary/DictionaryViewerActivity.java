package kr.kdev.dg1s.biowiki.ui.dictionary;

import android.os.Bundle;
import android.widget.GridView;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.BWActionBarActivity;

/**
 * This shows how to place markers on a map.
 */
public class DictionaryViewerActivity extends BIActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.dictionary);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
