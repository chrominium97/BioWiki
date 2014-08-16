package kr.kdev.dg1s.biowiki.ui.info.categorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.io.IOException;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.info.categorization.fragments.HabitatSelectionFragment;
import kr.kdev.dg1s.biowiki.ui.info.viewer.PlantInformationViewerActivity;

public class SearchByHabitatActivity extends BIActionBarActivity implements HabitatSelectionFragment.OnPlantSelectedListener {

    HabitatSelectionFragment selectionFragment = new HabitatSelectionFragment();
    boolean isViewingDetails = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = getString(R.string.classificationItem);
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
        Log.d("Bundle tag name : ", name);
        Intent intent = new Intent(this, PlantInformationViewerActivity.class);
        intent.putExtra("plant", name);

        startActivity(intent);

    }

}
