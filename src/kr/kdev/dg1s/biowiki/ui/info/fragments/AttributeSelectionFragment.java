package kr.kdev.dg1s.biowiki.ui.info.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.actionbarsherlock.app.SherlockFragment;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import kr.kdev.dg1s.biowiki.Constants;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.info.fragments.utils.ExpandableGridView;
import kr.kdev.dg1s.biowiki.ui.info.fragments.utils.ImageAdapter;
import kr.kdev.dg1s.biowiki.util.ToastUtils;

public class AttributeSelectionFragment extends SherlockFragment {

    ScrollView flowerLayout;
    ScrollView leafLayout;
    ScrollView fruitLayout;

    ExpandableGridView flowerGrid1, flowerGrid2, flowerGrid3;
    ExpandableGridView leafGrid1, leafGrid2, leafGrid3, leafGrid4, leafGrid5, leafGrid6, leafGrid7;
    ExpandableGridView fruitGrid;

    ViewPager pager = null;
    MainPagerAdapter pagerAdapter = null;

    Context context;

    OnAttributeDecidedListener mCallback;

    public void getSelectedAttributes() {
        ArrayList<ImageAdapter> imageAdapters = new ArrayList<ImageAdapter>();
        imageAdapters.add((ImageAdapter) flowerGrid1.getAdapter());
        imageAdapters.add((ImageAdapter) flowerGrid2.getAdapter());
        imageAdapters.add((ImageAdapter) flowerGrid3.getAdapter());
        imageAdapters.add((ImageAdapter) leafGrid1.getAdapter());
        imageAdapters.add((ImageAdapter) leafGrid2.getAdapter());
        imageAdapters.add((ImageAdapter) leafGrid3.getAdapter());
        imageAdapters.add((ImageAdapter) leafGrid4.getAdapter());
        imageAdapters.add((ImageAdapter) leafGrid5.getAdapter());
        imageAdapters.add((ImageAdapter) leafGrid6.getAdapter());
        imageAdapters.add((ImageAdapter) leafGrid7.getAdapter());
        imageAdapters.add((ImageAdapter) fruitGrid.getAdapter());

        ArrayList<Integer> ids = new ArrayList<Integer>();

        for (ImageAdapter imageAdapter : imageAdapters) {
            if (imageAdapter.mSelectedRB != null) {
                ids.add(imageAdapter.mSelectedRB.getId());
            }
        }
        if (ids.size() == 0) {
            ToastUtils.showToast(context, "속성을 선택해 주세요.");
        } else {
            mCallback.onAttributeDecided(ids);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAttributeDecidedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAttributeDecidedListener");
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    public void setupViews() {

        flowerLayout = (ScrollView) getLayoutInflater(new Bundle()).inflate(R.layout.attributes_flower, null);
        leafLayout = (ScrollView) getLayoutInflater(new Bundle()).inflate(R.layout.attributes_leaf, null);
        fruitLayout = (ScrollView) getLayoutInflater(new Bundle()).inflate(R.layout.attributes_fruit, null);

        flowerGrid1 = (ExpandableGridView) flowerLayout.findViewById(R.id.flowerGrid1);
        flowerGrid2 = (ExpandableGridView) flowerLayout.findViewById(R.id.flowerGrid2);
        flowerGrid3 = (ExpandableGridView) flowerLayout.findViewById(R.id.flowerGrid3);
        leafGrid1 = (ExpandableGridView) leafLayout.findViewById(R.id.leafGrid1);
        leafGrid2 = (ExpandableGridView) leafLayout.findViewById(R.id.leafGrid2);
        leafGrid3 = (ExpandableGridView) leafLayout.findViewById(R.id.leafGrid3);
        leafGrid4 = (ExpandableGridView) leafLayout.findViewById(R.id.leafGrid4);
        leafGrid5 = (ExpandableGridView) leafLayout.findViewById(R.id.leafGrid5);
        leafGrid6 = (ExpandableGridView) leafLayout.findViewById(R.id.leafGrid6);
        leafGrid7 = (ExpandableGridView) leafLayout.findViewById(R.id.leafGrid7);
        fruitGrid = (ExpandableGridView) fruitLayout.findViewById(R.id.fruitGrid);

        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) getView().findViewById(R.id.attributePager);
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                System.gc();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                pager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        initializeCategory();

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
        File file = getActivity().getCacheDir();
        // Instance of ImageAdapter Class
        flowerGrid1.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Flowers.ATTRIBUTES.flowerDrawable1, Constants.PlantsAttributes.Flowers.ATTRIBUTES.flowerNames1, file));
        flowerGrid2.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Flowers.ATTRIBUTES.flowerDrawable2, Constants.PlantsAttributes.Flowers.ATTRIBUTES.flowerNames2, file));
        flowerGrid3.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Flowers.ATTRIBUTES.flowerDrawable3, Constants.PlantsAttributes.Flowers.ATTRIBUTES.flowerNames3, file));
        leafGrid1.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafDrawable1, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafNames1, file));
        leafGrid2.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafDrawable2, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafNames2, file));
        leafGrid3.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafDrawable3, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafNames3, file));
        leafGrid4.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafDrawable4, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafNames4, file));
        leafGrid5.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafDrawable5, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafNames5, file));
        leafGrid6.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafDrawable6, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafNames6, file));
        leafGrid7.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafDrawable7, Constants.PlantsAttributes.Leaves.ATTRIBUTES.leafNames7, file));
        fruitGrid.setAdapter(
                new ImageAdapter(context, Constants.PlantsAttributes.Fruits.ATTRIBUTES.fruitDrawable, Constants.PlantsAttributes.Fruits.ATTRIBUTES.fruitNames, file));

        flowerGrid1.setExpanded(true);
        flowerGrid2.setExpanded(true);
        flowerGrid3.setExpanded(true);
        leafGrid1.setExpanded(true);
        leafGrid2.setExpanded(true);
        leafGrid3.setExpanded(true);
        leafGrid4.setExpanded(true);
        leafGrid5.setExpanded(true);
        leafGrid6.setExpanded(true);
        leafGrid7.setExpanded(true);
        fruitGrid.setExpanded(true);
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
        }
        return export;
    }

    /**
     * public void parseXML(String tag, int position) throws IOException {
     * ArrayList<String> names = new ArrayList<String>();
     * if (position == -1) {
     * displayedElements = currentElement.getChildElements();
     * } else if (position == -2) {
     * if (!currentElement.getName().equals("repo"))
     * currentElement = currentElement.getParentElement();
     * displayedElements = currentElement.getChildElements();
     * } else if (currentElement.getFirstElement("name", tag, false).getName().equals("what")) {
     * ArrayList<String> details = getDetails(currentElement.getFirstElement("name", tag, false).getAttributeValue("name"));
     * if (details.size() == 0) {
     * Toast.makeText(context, "정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
     * return;
     * }
     * mCallback.onAttributeDecided(tag);
     * return;
     * } else {
     * currentElement = currentElement.getFirstElement("name", tag, false);
     * displayedElements = currentElement.getChildElements();
     * }
     * for (Element element : displayedElements) {
     * names.add(element.getAttributeValue("name"));
     * Collections.sort(names);
     * }
     * gridView.invalidateViews();
     * gridView.setAdapter(new ElementAdapter(context, names));
     * if (tag != null) {
     * getSherlockActivity().getSupportActionBar().setTitle(tag);
     * } else if (!(currentElement.getAttributeValue("name") == null)) {
     * getSherlockActivity().getSupportActionBar().setTitle(currentElement.getAttributeValue("name"));
     * } else {
     * getSherlockActivity().getSupportActionBar().setTitle(R.string.app_name);
     * }
     * }
     */

    @Override
    public void onResume() {
        super.onResume();
    }

    // Container Activity must implement this interface
    public interface OnAttributeDecidedListener {
        public void onAttributeDecided(ArrayList<Integer> ids);
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
}
