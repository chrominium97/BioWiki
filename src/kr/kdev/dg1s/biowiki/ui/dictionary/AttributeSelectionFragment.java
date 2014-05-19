package kr.kdev.dg1s.biowiki.ui.dictionary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    LinearLayout flowerLayout;
    LinearLayout leafLayout;
    LinearLayout fruitLayout;

    GridView flowerGrid1, flowerGrid2, flowerGrid3;
    GridView leafGrid1, leafGrid2, leafGrid3, leafGrid4, leafGrid5, leafGrid6, leafGrid7;
    GridView fruitGrid;

    GridView gridView;

    ViewPager pager = null;
    MainPagerAdapter pagerAdapter = null;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViews();
        initializeCategory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    public void setupViews() {

        flowerLayout = (LinearLayout) getLayoutInflater(new Bundle()).inflate(R.layout.attributes_flower, null);
        leafLayout = (LinearLayout) getLayoutInflater(new Bundle()).inflate(R.layout.attributes_leaf, null);
        fruitLayout = (LinearLayout) getLayoutInflater(new Bundle()).inflate(R.layout.attributes_fruit, null);

        flowerGrid1 = (GridView) flowerLayout.findViewById(R.id.flowerGrid1);
        flowerGrid2 = (GridView) flowerLayout.findViewById(R.id.flowerGrid2);
        flowerGrid3 = (GridView) flowerLayout.findViewById(R.id.flowerGrid3);
        leafGrid1 = (GridView) leafLayout.findViewById(R.id.leafGrid1);
        leafGrid2 = (GridView) leafLayout.findViewById(R.id.leafGrid2);
        leafGrid3 = (GridView) leafLayout.findViewById(R.id.leafGrid3);
        leafGrid4 = (GridView) leafLayout.findViewById(R.id.leafGrid4);
        leafGrid5 = (GridView) leafLayout.findViewById(R.id.leafGrid5);
        leafGrid6 = (GridView) leafLayout.findViewById(R.id.leafGrid6);
        leafGrid7 = (GridView) leafLayout.findViewById(R.id.leafGrid7);
        fruitGrid = (GridView) fruitLayout.findViewById(R.id.fruitGrid);

        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) getView().findViewById(R.id.attributePager);
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                pager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        addView(flowerLayout);
        addView(leafLayout);
        addView(fruitLayout);

    }

    // Here's what the app should do to add a view to the ViewPager.
    public void addView(View newPage) {
        pagerAdapter.addView(newPage);
        pagerAdapter.notifyDataSetChanged();
    }

    public void initializeCategory() {
        // Instance of ImageAdapter Class
        flowerGrid1.setAdapter(new ImageAdapter(context, flowerArray1));
        flowerGrid2.setAdapter(new ImageAdapter(context, flowerArray2));
        flowerGrid3.setAdapter(new ImageAdapter(context, flowerArray3));
        leafGrid1.setAdapter(new ImageAdapter(context, leafArray1));
        leafGrid2.setAdapter(new ImageAdapter(context, leafArray2));
        leafGrid3.setAdapter(new ImageAdapter(context, leafArray3));
        leafGrid4.setAdapter(new ImageAdapter(context, leafArray4));
        leafGrid5.setAdapter(new ImageAdapter(context, leafArray5));
        leafGrid6.setAdapter(new ImageAdapter(context, leafArray6));
        leafGrid7.setAdapter(new ImageAdapter(context, leafArray7));
        fruitGrid.setAdapter(new ImageAdapter(context, fruitArray));
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

    public static class MainPagerAdapter extends PagerAdapter {
        // This holds all the currently displayable views, in order from left to right.
        private ArrayList<View> views = new ArrayList<View>();

        // Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
        // page should be displayed, from left-to-right.  If the page no longer exists,
        // return POSITION_NONE.
        @Override
        public int getItemPosition(Object object) {
            int index = views.indexOf(object);
            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }

        // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
        // to add the page to the container, which is normally the ViewPager itself.  Since
        // all our pages are persistent, we simply retrieve it from our "views" ArrayList.
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
        // is our job to remove the page from the container, which is normally the
        // ViewPager itself.  Since all our pages are persistent, we do nothing to the
        // contents of our "views" ArrayList.
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        // Used by ViewPager; can be used by app as well.
        // Returns the total number of pages that the ViewPage can display.  This must
        // never be 0.
        @Override
        public int getCount() {
            return views.size();
        }

        // Used by ViewPager.
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // Add "view" to right end of "views".
        // Returns the position of the new view.
        // The app should call this to add pages; not used by ViewPager.
        public int addView(View v) {
            return addView(v, views.size());
        }

        // Add "view" at "position" to "views".
        // Returns position of new view.
        // The app should call this to add pages; not used by ViewPager.
        public int addView(View v, int position) {
            views.add(position, v);
            return position;
        }

        // Removes "view" from "views".
        // Retuns position of removed view.
        // The app should call this to remove pages; not used by ViewPager.
        public int removeView(ViewPager pager, View v) {
            return removeView(pager, views.indexOf(v));
        }

        // Removes the "view" at "position" from "views".
        // Retuns position of removed view.
        // The app should call this to remove pages; not used by ViewPager.
        public int removeView(ViewPager pager, int position) {
            // ViewPager doesn't have a delete method; the closest is to set the adapter
            // again.  When doing so, it deletes all its views.  Then we can delete the view
            // from from the adapter and finally set the adapter to the pager again.  Note
            // that we set the adapter to null before removing the view from "views" - that's
            // because while ViewPager deletes all its views, it will call destroyItem which
            // will in turn cause a null pointer ref.
            pager.setAdapter(null);
            views.remove(position);
            pager.setAdapter(this);

            return position;
        }

        // Returns the "view" at "position".
        // The app should call this to retrieve a view; not used by ViewPager.
        public View getView(int position) {
            return views.get(position);
        }

        // Other relevant methods:

        // finishUpdate - called by the ViewPager - we don't care about what pages the
        // pager is displaying so we don't use this method.
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
