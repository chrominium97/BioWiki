package kr.kdev.dg1s.biowiki.ui.dictionary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.info.ElementAdapter;

public class AttributeSelectionFragment extends SherlockFragment {

    int[] flowerArray1 = {R.drawable.d1101, R.drawable.d1102, R.drawable.d1103, R.drawable.d1104,
                        R.drawable.d1105, R.drawable.d1106, R.drawable.d1107, R.drawable.d1108,
                        R.drawable.d1109, R.drawable.d1110, R.drawable.d1111_1, R.drawable.d1111_2};
    int[] flowerArray2 = {R.drawable.d1201, R.drawable.d1202, R.drawable.d1203};
    int[] flowerArray3 = {R.drawable.d1301, R.drawable.d1302, R.drawable.d1303,R.drawable.d1304,
                        R.drawable.d1305, R.drawable.d1306, R.drawable.d1307, R.drawable.d1308,
                        R.drawable.d1309, R.drawable.d1310, R.drawable.d1311, R.drawable.d1312,
                        R.drawable.d1313, R.drawable.d1314, R.drawable.d1315, R.drawable.d1316,
                        R.drawable.d1317};
    int[] leafArray1 = {R.drawable.d2101, R.drawable.d2102, R.drawable.d2103, R.drawable.d2104,
                        R.drawable.d2105, R.drawable.d2106, R.drawable.d2107, R.drawable.d2108,
                        R.drawable.d2109};
    int[] leafArray2 = {R.drawable.d2201, R.drawable.d2202, R.drawable.d2203, R.drawable.d2204,
                        R.drawable.d2205, R.drawable.d2206, R.drawable.d2207, R.drawable.d2208,
                        R.drawable.d2209, R.drawable.d2210, R.drawable.d2211, R.drawable.d2212,
                        R.drawable.d2213, R.drawable.d2214, R.drawable.d2215, R.drawable.d2216,
                        R.drawable.d2217};
    int[] leafArray3 = {R.drawable.d2301, R.drawable.d2302, R.drawable.d2303, R.drawable.d2304,
                        R.drawable.d2305, R.drawable.d2306, R.drawable.d2307, R.drawable.d2308,
                        R.drawable.d2309, R.drawable.d2310, R.drawable.d2311, R.drawable.d2312};
    int[] leafArray4 = {R.drawable.d2401, R.drawable.d2402, R.drawable.d2403, R.drawable.d2404,
                        R.drawable.d2405, R.drawable.d2406, R.drawable.d2407, R.drawable.d2408,
                        R.drawable.d2409, R.drawable.d2410, R.drawable.d2411, R.drawable.d2412,
                        R.drawable.d2413, R.drawable.d2414, R.drawable.d2415, R.drawable.d2416,
                        R.drawable.d2417};
    int[] leafArray5 = {R.drawable.d2501, R.drawable.d2502, R.drawable.d2503, R.drawable.d2504,
                        R.drawable.d2505};
    int[] leafArray6 = {R.drawable.d2601, R.drawable.d2602, R.drawable.d2603};
    int[] leafArray7 = {R.drawable.d2701, R.drawable.d2702, R.drawable.d2703, R.drawable.d2704};
    int[] fruitArray = {R.drawable.d3001, R.drawable.d3002, R.drawable.d3003, R.drawable.d3004,
                        R.drawable.d3005, R.drawable.d3006, R.drawable.d3007, R.drawable.d3008,
                        R.drawable.d3009, R.drawable.d3010, R.drawable.d3011, R.drawable.d3012};

    GridView gridView;

    ViewPager pager;
    ImageAdapter adapter;

    Element currentElement;
    List<Element> displayedElements;

    Context context;

    Source source;

    OnPlantSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnPlantSelectedListener {
        public void onPlantSelected(String name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPlantSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlantSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attribute_selector, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setupViews();
        try {
            initializeCategory();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to initialize blank_linearlayout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    public void setupViews() {
    }

    public void initializeCategory() throws IOException {
        // Instance of ImageAdapter Class

    }

    public ArrayList<String> getDetails(String name) throws IOException {
        ArrayList<String> export = new ArrayList<String>();
        Source source1 = new Source(getResources().getAssets().open("xmls/kingdom.xml"));
        Log.d("XML", "Searching for details on " + name);
        Element element = source1.getFirstElement("name", name, false);
        if (element == null) {
            return export;
        }
        Attributes attributes = element.getAttributes();
        for (Attribute attribute : attributes) {
            export.add(attribute.getName());
            export.add(attribute.getValue());
        } return export;
    }

    public void parseXML(String tag, int position) throws IOException {
        ArrayList<String> names = new ArrayList<String>();
        if (position == -1) {
            displayedElements = currentElement.getChildElements();
        } else if (position == -2) {
            if (!currentElement.getName().equals("repo"))
                currentElement = currentElement.getParentElement();
            displayedElements = currentElement.getChildElements();
        } else if (currentElement.getFirstElement("name", tag, false).getName().equals("what")) {
            ArrayList<String> details = getDetails(currentElement.getFirstElement("name", tag, false).getAttributeValue("name"));
            if (details.size() == 0) {
                Toast.makeText(context, "정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            mCallback.onPlantSelected(tag);
            return;
        } else {
            currentElement = currentElement.getFirstElement("name", tag, false);
            displayedElements = currentElement.getChildElements();
        }
        for (Element element : displayedElements) {
            names.add(element.getAttributeValue("name"));
            Collections.sort(names);
        }
        gridView.invalidateViews();
        gridView.setAdapter(new ElementAdapter(context, names));
        if (tag != null) {
            getSherlockActivity().getSupportActionBar().setTitle(tag);
        } else if (!(currentElement.getAttributeValue("name") == null)) {
            getSherlockActivity().getSupportActionBar().setTitle(currentElement.getAttributeValue("name"));
        } else {
            getSherlockActivity().getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
