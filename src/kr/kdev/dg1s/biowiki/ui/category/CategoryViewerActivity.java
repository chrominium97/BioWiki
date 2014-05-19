package kr.kdev.dg1s.biowiki.ui.category;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.simonvt.menudrawer.MenuDrawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;
import kr.kdev.dg1s.biowiki.ui.info.ElementAdapter;
import kr.kdev.dg1s.biowiki.ui.info.PlantInformationFragment;

public class CategoryViewerActivity extends BIActionBarActivity {

    GridView gridView;
    LinearLayout layout;
    ViewPager pager = null;
    PlantInformationFragment.MainPagerAdapter pagerAdapter = null;

    Element currentElement;
    List<Element> displayedElements;

    ImageLoaderConfiguration config;
    DisplayImageOptions options;

    Context context;

    Source source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuDrawer(R.layout.category);
        context = getApplicationContext();
        setupViews();
        try {
            initializeCategory();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to initialize category", Toast.LENGTH_SHORT).show();
        }

        // Create global configuration and initialize ImageLoader with this configuration
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
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

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        getSupportMenuInflater().inflate(R.menu.hierarchy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.go_up:
                if (layout.getVisibility() == View.VISIBLE) {
                    layout.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    return true;
                }
                try {
                    parseXML(null, -2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if (mMenuDrawer != null) {
            final int drawerState = mMenuDrawer.getDrawerState();
            if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
                mMenuDrawer.closeMenu();
                return;
            }
        }
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            return;
        } else {
            if (!(currentElement.getAttributeValue("name") == null)) {
                try {
                    parseXML(null, -2);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        super.onBackPressed();
    }

    public void setupViews() {
        // Lists plants and their categories
        gridView = (GridView) findViewById(R.id.list_view);
        // Details of plants
        layout = (LinearLayout) findViewById(R.id.showdetails);
        // Image pagers
        pagerAdapter = new PlantInformationFragment.MainPagerAdapter();
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pagerAdapter);
        /*pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/

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

        // Create an initial view to display; must be a subclass of FrameLayout.
        /*FrameLayout v0 = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.imageframe, null);
        pagerAdapter.addView(v0, 0);
        pagerAdapter.notifyDataSetChanged();
        */
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addView(View newPage) {
        pagerAdapter.addView(newPage);
        pagerAdapter.notifyDataSetChanged();
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView(View defunctPage) {
        int pageIndex = pagerAdapter.removeView(pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem(pageIndex);
        pagerAdapter.notifyDataSetChanged();
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage() {
        return pagerAdapter.getView(pager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage(View pageToShow) {
        pager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
    }

    public void initializeCategory() throws IOException {
        // Instance of ImageAdapter Class
        source = new Source(getResources().openRawResource(R.raw.categories));
        Log.d("XML", source.toString());
        currentElement = source.getFirstElement("repo");
        parseXML(null, -1);
        Log.d("", "Setting gridView listener");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) view;
                Log.d("Button", "Clicked " + position);
                try {
                    parseXML(textView.getText().toString(), position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ArrayList<String> getDetails(String name) throws IOException {
        ArrayList<String> export = new ArrayList<String>();
        Source source1 = new Source(getAssets().open("xmls/kingdom.xml"));
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
            name.setText("핵형");
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
            layout.removeViews(2, layout.getChildCount() - 2);
            for (int i = 0; i < (details.size() / 2); i++) {
                if (details.get(2 * i).equals("image")) {
                    if (!(details.get(2 * i + 1).equals(""))) {
                        pager.setVisibility(View.VISIBLE);
                        for (; 0 != pagerAdapter.getCount(); ) {
                            pagerAdapter.removeView(pager, 0);
                        }
                        pagerAdapter.notifyDataSetChanged();
                        for (String filename : Arrays.asList(details.get(2 * i + 1).split(" "))) {
                            ImageView imageView = new ImageView(context);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            ImageLoader imageLoader = ImageLoader.getInstance();
                            imageLoader.init(config);
                            imageLoader.displayImage(BioWiki.getCurrentBlog().getHomeURL() + "repo/IMG/" + filename, imageView);
                            addView(imageView);
                        }
                    } else {
                        pager.setVisibility(View.GONE);
                    }
                } else {
                    layout.addView(plantDetails(details.get(2 * i), details.get(2 * i + 1)));
                }
            }
            TextView plantName = (TextView) layout.findViewById(R.id.plant_name);
            plantName.setText(tag);
            gridView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
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
        gridView.setAdapter(new ElementAdapter(this, names));
        if (tag != null) {
            getSupportActionBar().setTitle(tag);
        } else if (!(currentElement.getAttributeValue("name") == null)) {
            getSupportActionBar().setTitle(currentElement.getAttributeValue("name"));
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
