package kr.kdev.dg1s.biowiki.ui.dictionary;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.category.CategorySelectionFragment;
import kr.kdev.dg1s.biowiki.ui.info.PlantInformationFragment;

public class DictionaryViewerActivity extends BIActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.blank_linearlayout);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AttributeSelectionFragment selectionFragment = new AttributeSelectionFragment();
        transaction.add(R.id.attributeSelector, selectionFragment);
        transaction.commit();
    }

    public void onPlantSelected(String name) {
        CategorySelectionFragment selectionFragment = new CategorySelectionFragment();
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
