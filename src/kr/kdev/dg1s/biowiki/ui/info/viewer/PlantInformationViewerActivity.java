package kr.kdev.dg1s.biowiki.ui.info.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.Constants;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.info.categorization.SearchByHabitatActivity;

public class PlantInformationViewerActivity extends BIActionBarActivity {

    LinearLayout infoContainer;
    ViewPager pager = null;
    MainPagerAdapter pagerAdapter = null;

    ImageLoaderConfiguration config;
    DisplayImageOptions options;

    Context context;

    Source dictionaryAssets;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = getString(R.string.plantInformationTitle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bioinfo_plant_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            initializeGlobalVariables();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to initialize blank linear layout", Toast.LENGTH_SHORT).show();
        }
        Log.d("Bundle is", " " + (savedInstanceState != null));
        Log.d("Bundle", getIntent().getStringExtra("plant") + (savedInstanceState != null));
        displayContents(getIntent().getStringExtra("plant"));
    }

    public void displayContents(String name) {
        Log.d("XML", "Searching for details on " + name);
        Element element = dictionaryAssets.getFirstElement("name", name, false);

        Attributes attributes = element.getAttributes();
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals("image")) {
                if (!(attribute.getValue().equals(""))) {
                    // Delete all child views and make pager visible
                    pager.setVisibility(View.VISIBLE);
                    while (pagerAdapter.getCount() != 0) {
                        pagerAdapter.removeView(pager, 0);
                    }
                    pagerAdapter.notifyDataSetChanged();
                    for (String filename : Arrays.asList(attribute.getValue().split(" "))) {
                        ImageView imageView = new ImageView(context);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.init(config);
                        imageLoader.displayImage(BioWiki.getCurrentBlog().getHomeURL() + "repo/IMG/" + filename.toUpperCase(), imageView);
                        addView(imageView);
                    }
                } else {
                    pager.setVisibility(View.GONE);
                }
            } else {
                infoContainer.addView(plantDetails(attribute.getName(), attribute.getValue()));
            }
        }
        TextView plantName = (TextView) infoContainer.findViewById(R.id.plant_name);
        plantName.setText(name);
    }

    View plantDetails(String token, String value) {
        LinearLayout plantDetails = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.plant_detail_adapter, null);
        TextView name = (TextView) plantDetails.findViewById(R.id.name);
        TextView details = (TextView) plantDetails.findViewById(R.id.details);
        if (token.equals("name")) {
            name.setText("이름");
        } else if (token.equals("stump")) {
            name.setText("줄기");
        } else if (token.equals("leaf")) {
            name.setText("잎");
        } else if (token.equals("flower")) {
            name.setText("꽃");
        } else if (token.equals("fruit")) {
            name.setText("열매");
        } else if (token.equals("chromo")) {
            name.setText("핵상");
        } else if (token.equals("place")) {
            name.setText("서식지");
        } else if (token.equals("horizon")) {
            name.setText("수평분포");
        } else if (token.equals("vertical")) {
            name.setText("수직분포");
        } else if (token.equals("geograph")) {
            name.setText("식생지리");
        } else if (token.equals("vegetat")) {
            name.setText("식생형");
        } else if (token.equals("preserve")) {
            name.setText("종보존등급");
        } else {
            name.setText("기타");
        }
        name.setTextColor(getResources().getColor(R.color.black));
        details.setText(value);
        details.setTextColor(getResources().getColor(R.color.black));
        return plantDetails;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Menu is defined inside 'res/menu/...xml
        getSupportMenuInflater().inflate(R.menu.plant_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.gotoChart:
                Intent intent = new Intent(this, ChartViewerActivity.class);
                startActivityWithDelay(intent);
                return true;
            default: return super.onOptionsItemSelected(menu);
        }
    }

    // Create global configuration and initialize ImageLoader with this configuration
    public void initializeGlobalVariables() throws IOException {
        dictionaryAssets = new Source(getResources().getAssets().open("xmls/kingdom.xml"));
        // Direct child of ScrollView
        infoContainer = (LinearLayout) findViewById(R.id.details);
        // Image pagers
        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pagerAdapter);
        /*
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        */
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
        // Get context
        context = getApplicationContext();
        // AUIL stuff
        options = Constants.imageOptions;
        config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(4)
                .discCache(new UnlimitedDiscCache(getCacheDir()))
                .discCacheSize(200 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .writeDebugLogs()
                .defaultDisplayImageOptions(options)
                .discCacheFileCount(1000)
                .build();
    }

    /**
     * Below are methods for pagers
     */
    // Here's what the app should do to add a view to the ViewPager.
    public void addView(View newPage) {
        pagerAdapter.addView(newPage);
        pagerAdapter.notifyDataSetChanged();
    }

    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView(View defunctPage) {
        int pageIndex = pagerAdapter.removeView(pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem(pageIndex);
        pagerAdapter.notifyDataSetChanged();
    }

    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage() {
        return pagerAdapter.getView(pager.getCurrentItem());
    }

    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage(View pageToShow) {
        pager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
    }

    /**
     * Pager methods ends here
     */

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
