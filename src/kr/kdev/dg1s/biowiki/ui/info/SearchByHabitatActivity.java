package kr.kdev.dg1s.biowiki.ui.info;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.io.IOException;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.plantInfo.PlantInformationFragment;

public class SearchByHabitatActivity extends BIActionBarActivity implements CategorySelectionFragment.OnPlantSelectedListener {

    String plantInstance;

    CategorySelectionFragment selectionFragment = new CategorySelectionFragment();
    boolean isViewingDetails = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.blank_linearlayout);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.selector_category, selectionFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (isViewingDetails) {
            isViewingDetails = false;
            super.onBackPressed();
            return;
        }
        if (!(selectionFragment.currentElement.getName().equals("repo"))) {
            try {
                selectionFragment.parseXML(null, -2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void onPlantSelected(String name) {
        PlantInformationFragment informationFragment = new PlantInformationFragment();

        plantInstance = name;
        Bundle bundle = new Bundle();
        bundle.putString("plant", name);
        Log.d("Bundle tag name : ", name);
        informationFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.selector_category, informationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        isViewingDetails = true;
    }

}
