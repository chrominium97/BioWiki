package kr.kdev.dg1s.biowiki.ui.map;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.info.PlantInformationFragment;

public class DistributionViewerActivity extends BIActionBarActivity implements MapViewerFragment.OnPlantSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.maps_marker);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MapViewerFragment viewerFragment = new MapViewerFragment();
        transaction.add(R.id.default_info_window, viewerFragment);
        transaction.commit();
    }

    public void onPlantSelected(String name) {
        PlantInformationFragment informationFragment = new PlantInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("plant", name);
        Log.d("Bundle tag name : ", name);
        informationFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.selector_category, informationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}