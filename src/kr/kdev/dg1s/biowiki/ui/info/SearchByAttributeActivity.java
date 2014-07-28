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

    public void onAttributeDecided(ArrayList<Integer> ids, int logLevel) {
        ToastUtils.showToast(getApplicationContext(), "Switching...\n" + ids);
        List<ArrayList<String>> correspondingPlants = getCorrespondingPlants(ids);
        ArrayList<String> unfilteredPlants = correspondingPlants.get(0);
        ArrayList<String> filteredPlants = new ArrayList<String>();
        for (ArrayList<String> plantList : correspondingPlants) {
            if (logLevel > LOG_LEVEL_LOW)
                Log.d("COMPARISION", "Comparing \n" + plantList + "\nto\n" + unfilteredPlants);
            for (String plant : plantList) {
                if (unfilteredPlants.contains(plant)) {
                    if (logLevel > LOG_LEVEL_MEDIUM)
                        Log.d("COMPARISION", plant + " is included within array");
                    filteredPlants.add(plant);
                } else {
                    if (logLevel > LOG_LEVEL_MEDIUM)
                        Log.d("COMPARISION", plant + " ISN'T included within array");
                }
            }
            unfilteredPlants = new ArrayList<String>(filteredPlants);
            filteredPlants.clear();
            if (logLevel > LOG_LEVEL_NONE)
                Log.d("COMPARISION", "Filtered : \n" + unfilteredPlants);
        }
        if (logLevel > LOG_LEVEL_NONE)
            Log.d("COMPARISION", "----------------------END OF COMPARISION----------------------");
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
            Log.d("LIST", "Current elements : \n" + plants);
            ArrayList<String> arrayList = new ArrayList<String>();
            switch (id) {
                //FLOWERS
                case R.drawable.d1101:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.OPTITULUM));
                    break;
                case R.drawable.d1102:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CATKIN));
                    break;
                case R.drawable.d1103:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CYATHIUM));
                    break;
                case R.drawable.d1104:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CORYMB));
                    break;
                case R.drawable.d1105:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.UMBEL));
                    break;
                case R.drawable.d1106:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.RHIPIDIUM));
                    break;
                case R.drawable.d1107:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.SPIKE));
                    break;
                case R.drawable.d1108:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.PANICLE));
                    break;
                case R.drawable.d1109:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.SPEDIX)); // 오타: SPEDIX -> SPADIX
                    break;
                case R.drawable.d1110:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.RECEME)); // 오타: RECEME -> RACEME
                    break;
                case R.drawable.d1111_1:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CYME));
                    break;
                case R.drawable.d1111_2:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.ORDER.CYME));
                    break;

                case R.drawable.d1201:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.OVERY.SUPERIOR_OVERY));
                    break;
                case R.drawable.d1202:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.OVERY.HALFINFERIOR_OVERY));
                    break;
                case R.drawable.d1203:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.OVERY.INFERIOR_OVARY));
                    break;

                case R.drawable.d1301:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.PERSONATE));
                    break;
                case R.drawable.d1302:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.TUBULAR));
                    break;
                case R.drawable.d1303:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.PALATE));
                    break;
                case R.drawable.d1304:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.SACCATE));
                    break;
                case R.drawable.d1305:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.FUNNEL));
                    break;
                case R.drawable.d1306:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.ACTINOMORPHIC));
                    break;
                case R.drawable.d1307:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.SALVER));
                    break;
                case R.drawable.d1308:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.LIGULATE));
                    break;
                case R.drawable.d1309:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.BILABIATE));
                    break;
                case R.drawable.d1310:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.CRUCIFORM));
                    break;
                case R.drawable.d1311:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.CORONATE));
                    break;
                case R.drawable.d1312:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.PAPILLIONACEOUS));
                    break;
                case R.drawable.d1313:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.CAMPANULATE));
                    break;
                case R.drawable.d1314:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.GIBBOUS));
                    break;
                case R.drawable.d1315:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.GALEATE));
                    break;
                case R.drawable.d1316:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.ROTATE));
                    break;
                case R.drawable.d1317:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Flowers.CorollaType.URCEOLATE));
                    break;

                //LEAF
                case R.drawable.d2101:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.MUCRONATE));
                    break;
                case R.drawable.d2102:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.OBTUSE));
                    break;
                case R.drawable.d2103:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.CAUDATE));
                    break;
                case R.drawable.d2104:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.ACUTE));
                    break;
                case R.drawable.d2105:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.CUSPIDATE));
                    break;
                case R.drawable.d2106:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.EMARGINATE));
                    break;
                case R.drawable.d2107:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.ROUNDED));
                    break;
                case R.drawable.d2108:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.ACUMINATE));
                    break;
                case R.drawable.d2109:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.TIPS.TRUNCATE));
                    break;

                case R.drawable.d2201:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.ENTIRE));
                    break;
                case R.drawable.d2202:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.CRENATE));
                    break;
                case R.drawable.d2203:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.CRENULATE));
                    break;
                case R.drawable.d2204:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.SERRATE));
                    break;
                case R.drawable.d2205:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.SERRULATE));
                    break;
                case R.drawable.d2206:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.BISERRATE));
                    break;
                case R.drawable.d2207:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.DENTATE));
                    break;
                case R.drawable.d2208:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.DENTICULATE));
                    break;
                case R.drawable.d2209:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.UNDULATE));
                    break;
                case R.drawable.d2210:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.REVOLUTE));
                    break;
                case R.drawable.d2211:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.INVOLUTE));
                    break;
                case R.drawable.d2212:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.PLANE));
                    break;
                case R.drawable.d2213:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.LOBED));
                    break;
                case R.drawable.d2214:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.CLEFT));
                    break;
                case R.drawable.d2215:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.PARTED));
                    break;
                case R.drawable.d2216:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.PINNATIFID));
                    break;
                case R.drawable.d2217:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.MARGINS.PALMATIFID));
                    break;
                case R.drawable.d2301:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.PERPOLIATE));
                    break;
                case R.drawable.d2302:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.HASTATE));
                    break;
                case R.drawable.d2303:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.OBTUSE));
                    break;
                case R.drawable.d2304:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.CUNEATE));
                    break;
                case R.drawable.d2305:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.PELTATE));
                    break;
                case R.drawable.d2306:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.CORDATE));
                    break;
                case R.drawable.d2307:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.ACUTE));
                    break;
                case R.drawable.d2308:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.OBLIQUE));
                    break;
                case R.drawable.d2309:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.ROUNDED));
                    break;
                case R.drawable.d2310:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.ATTEUATE));
                    break;
                case R.drawable.d2311:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.AURICULATE));
                    break;
                case R.drawable.d2312:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.BASE.TRUNCATE));
                    break;

                case R.drawable.d2401:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.OVAL));
                    break;
                case R.drawable.d2402:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.HASTATE));
                    break;
                case R.drawable.d2403:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.OVATE));
                    break;
                case R.drawable.d2404:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.OBOVATE));
                    break;
                case R.drawable.d2405:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.OBLANCEOLATE));
                    break;
                case R.drawable.d2406:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.RUNCINATE));
                    break;
                case R.drawable.d2407:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.DELTOID));
                    break;
                case R.drawable.d2408:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.LINEAR));
                    break;
                case R.drawable.d2409:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.RENIFORM));
                    break;
                case R.drawable.d2410:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.CORDATE));
                    break;
                case R.drawable.d2411:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.ORBICULAR));
                    break;
                case R.drawable.d2412:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.OBLONG));
                    break;
                case R.drawable.d2413:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.SAGITTATE));
                    break;
                case R.drawable.d2414:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.SPATULATE));
                    break;
                case R.drawable.d2415:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.ACICULAR));
                    break;
                case R.drawable.d2416:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.ELLIPTICAL));
                    break;
                case R.drawable.d2417:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.SHAPES.LANCEOLATE));
                    break;

                case R.drawable.d2501:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PINNATELY_COMPOUND.ODD_PINNATE_UNIJUGATE));
                    break;
                case R.drawable.d2502:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PINNATELY_COMPOUND.ODD_PINNATE));
                    break;
                case R.drawable.d2503:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PINNATELY_COMPOUND.ODD_BIPINNATE));
                    break;
                case R.drawable.d2504:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PINNATELY_COMPOUND.EVEN_PINNATE));
                    break;
                case R.drawable.d2505:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PINNATELY_COMPOUND.EVEN_BIPINNATE));
                    break;

                case R.drawable.d2601:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.ORDER_COMPOUND.VERTICILLATE));
                    break;
                case R.drawable.d2602:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.ORDER_COMPOUND.SIMPLE_OPPOSITE));
                    break;
                case R.drawable.d2603:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.ORDER_COMPOUND.SIMPLE_ALTERNATIVE));
                    break;
                case R.drawable.d2701:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PALMATELY_COMPOUND.BITERNATE));
                    break;
                case R.drawable.d2702:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PALMATELY_COMPOUND.TRIFOLIOLATE));
                    break;
                case R.drawable.d2703:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PALMATELY_COMPOUND.TRITERNATE));
                    break;
                case R.drawable.d2704:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Leaves.PALMATELY_COMPOUND.PENTAFOLIOLATE));
                    break;

                //FRUITS
                case R.drawable.d3001:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.PYXIS));
                    break;
                case R.drawable.d3002:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.NUT));
                    break;
                case R.drawable.d3003:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.LOMENT));
                    break;
            /* 3004: 분열과는 갖다 버리면 됨.
             case R.drawable.d3004:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.));
                    break;
                    */
                case R.drawable.d3005:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.CAPSULE));
                    break;
                case R.drawable.d3006:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.ACHENE));
                    break;
                case R.drawable.d3007:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.POME));
                    break;
                case R.drawable.d3008:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.SEMARA));  //오타: SEMARA -> SAMARA
                    break;
                case R.drawable.d3009:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.BERRY));
                    break;
                case R.drawable.d3010:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.AGGREGATE));
                    break;
                case R.drawable.d3011:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.DRUPE));
                    break;
                case R.drawable.d3012:
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.LEGUME));
                    break;
                case R.drawable.dg1s:  //이거 영과. 사진 추가하던가 해야 할 듯 싶음..;;
                    arrayList.addAll(Arrays.asList(Constants.PlantsAttributes.Fruits.CARYOPSIS));
                    break;


            }
            plants.add(arrayList);
        }
        return plants;
    }

}
