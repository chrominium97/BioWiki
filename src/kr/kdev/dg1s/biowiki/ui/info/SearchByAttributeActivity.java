package kr.kdev.dg1s.biowiki.ui.info;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.kdev.dg1s.biowiki.Constants;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.info.fragments.AttributeSelectionFragment;
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

    List<ArrayList<String>> getCorrespondingPlants(ArrayList<Integer> integers) {
        ArrayList<ArrayList<String>> plants = new ArrayList<ArrayList<String>>();
        for (int id : integers) {
            ArrayList<String> arrayList = new ArrayList<String>();
            switch (id) {
                case R.drawable.d1101 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.OPTITULUM));
                    break;
                case R.drawable.d1102 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CATKIN));
                    break;
                case R.drawable.d1103 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CYATHIUM));
                    break;
                case R.drawable.d1104 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CORYMB));
                    break;
                case R.drawable.d1105 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.UMBEL));
                    break;
                case R.drawable.d1106 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.RHIPIDIUM));
                    break;
                case R.drawable.d1107 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.SPIKE));
                    break;
                case R.drawable.d1108 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.PANICLE));
                    break;
                case R.drawable.d1109 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.SPEDIX)); // 오타: SPEDIX -> SPADIX
                    break;
                case R.drawable.d1110 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.RECEME)); // 오타: RECEME -> RACEME
                    break;
                case R.drawable.d1111_1 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CYME));
                    break;
                case R.drawable.d1111_2 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CYME));
                    break;

                case R.drawable.d1201 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.OVERY.SUPERIOR_OVERY));
                    break;
                case R.drawable.d1202 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.OVERY.HALFINFERIOR_OVERY));
                    break;
                case R.drawable.d1203 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.OVERY.INFERIOR_OVARY));
                    break;
                case R.drawable.d1301 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.PERSONATE));
                    break;
                case R.drawable.d1302 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.TUBULAR));
                    break;
                case R.drawable.d1303 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.PALATE));
                    break;
                case R.drawable.d1304 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.SACCATE));
                    break;
           /*     case R.drawable.d1305 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1306 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1307 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1308 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1309 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1310 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1311 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1312 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1313 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1314 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1315 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1316 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break;
                case R.drawable.d1317 :
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.));
                    break; */

            }
            plants.add(arrayList);
        }
        return plants;
    }

}
