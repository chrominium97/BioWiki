package kr.kdev.dg1s.biowiki.ui.info;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.ArrayList;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.info.fragments.GraphViewFragment;

public class ChartViewActivity extends BIActionBarActivity implements GraphViewFragment.OnDatasetRefreshRequest {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.blank_linearlayout);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.selector_category, new GraphViewFragment());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public void onDatasetRefreshRequest(ArrayList<String> names) {
        Log.d("Chart", names.toString());
    }

}
