package kr.kdev.dg1s.biowiki.ui.info;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.plantInfo.PlantInformationFragment;
import kr.kdev.dg1s.biowiki.util.ToastUtils;

public class SearchByAttributeActivity extends BIActionBarActivity implements AttributeSelectionFragment.OnAttributeDecidedListener {

    Menu menu;

    PlantInformationFragment informationFragment = null;
    AttributeSelectionFragment selectionFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.blank_linearlayout);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        informationFragment = new PlantInformationFragment();
        selectionFragment = new AttributeSelectionFragment();
        transaction.add(R.id.selector_category, selectionFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu createdMenu) {
        menu = createdMenu;
        getSupportMenuInflater().inflate(R.menu.attribute_menus, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                if (mMenuDrawer != null) {
                    mMenuDrawer.toggleMenu();
                    return true;
                }
            case R.id.selection_finished:
                selectionFragment.getSelectedAttributes();
                break;
        }
        return false;
    }

    public void onBackPressed() {
        /**
        if (getSupportFragmentManager().getFragments().get(0).equals(informationFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.selector_category, selectionFragment);
            transaction.commit();
        } else {
            super.onBackPressed();
        }
         */
        super.onBackPressed();
    }

    public void onAttributeDecided(ArrayList<Integer> ids) {
        ToastUtils.showToast(getApplicationContext(), "Switching...\n" + ids);
        Log.d("Attribute", "Switching...\n" + ids);
        Bundle bundle = new Bundle();
        bundle.putString("plant", "미나리");
        informationFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.selector_category, informationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
