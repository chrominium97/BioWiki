package kr.kdev.dg1s.biowiki.ui.info.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.nhaarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.BioWiki;
import kr.kdev.dg1s.biowiki.Constants;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.info.common.utils.PlantInfoFetcher;
import kr.kdev.dg1s.biowiki.ui.info.viewer.utils.CustomListActivity;
import kr.kdev.dg1s.biowiki.ui.info.viewer.utils.PlantInfoHolder;

public class SearchResultsViewerActivity extends CustomListActivity {

    ExpandableCardAdapter mExpandableListItemAdapter;

    boolean hasResults = false;
    ArrayList<String> results;
    ArrayList<PlantInfoFetcher> fetchers;
    Context context;

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) { // original
        title = getResources().getString(R.string.searchResultsTitle);
        super.onCreate(savedInstanceState);
        instantiateData(getIntent());

        if (hasResults) {
            ArrayList<PlantInfoHolder> holders = new ArrayList<PlantInfoHolder>();
            for (int i = 0; results.size() != 0 && i < results.size(); i++) {
                if (fetchers.get(i).hasImages) {
                    Log.d("Holder", "Added " + results.get(i) + " to stack WITH images");
                    Log.d("DATA", results.get(i) + " " + fetchers.get(i).plantDetails + "\n<" + fetchers.get(i).imageThumbnail);
                    holders.add(new PlantInfoHolder(results.get(i),
                            BioWiki.getCurrentBlog().getHomeURL() + "repo/IMG/" + fetchers.get(i).imageThumbnail,
                            fetchers.get(i).plantDetails));
                } else {
                    holders.add(new PlantInfoHolder(results.get(i),
                            "drawable://" + String.valueOf(R.drawable.remote_image),
                            fetchers.get(i).plantDetails));
                    Log.d("Holder", "Added " + results.get(i) + " to stack WITHOUT images");
                    Log.d("DATA", "이름 : " + results.get(i) + ", 상세정보 : " + fetchers.get(i).plantDetails);
                }
            }
            mExpandableListItemAdapter = new ExpandableCardAdapter(this, holders);
        } else {
            returnNoResults();
        }

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
        alphaInAnimationAdapter.setAbsListView(getListView());
        alphaInAnimationAdapter.setInitialDelayMillis(500);
        getListView().setAdapter(alphaInAnimationAdapter);
        // Added 20140805

    }

    void returnNoResults() {
        ArrayList<String> errorDesc = new ArrayList<String>();
        errorDesc.add(getString(R.string.error));
        errorDesc.add(getString(R.string.no_info));
        ArrayList<ArrayList<String>> plantDescs = new ArrayList<ArrayList<String>>();
        plantDescs.add(errorDesc);

        ArrayList<PlantInfoHolder> holders = new ArrayList<PlantInfoHolder>();
        holders.add(new PlantInfoHolder("검색결과 없음", "drawable://" + String.valueOf(R.drawable.remote_image), plantDescs));
        mExpandableListItemAdapter = new ExpandableCardAdapter(this, holders);
    }

    private void instantiateData(Intent intent) {
        context = this;
        fetchers = new ArrayList<PlantInfoFetcher>();
        ArrayList<String> list = intent.getExtras().getStringArrayList("plants");
        if (list.size() > 0) {
            results = list;
            hasResults = true;
            Log.v("Data validation", results.toString());
        }
        for (String name : results) {
            fetchers.add(new PlantInfoFetcher(name, this));
        }
    }

    private static class ExpandableCardAdapter extends ExpandableListItemAdapter<PlantInfoHolder> {

        ImageLoader imageLoader;

        DisplayImageOptions options;
        ImageLoaderConfiguration config;

        /**
         * Creates a new ExpandableListItemAdapter with the specified list, or an empty list if
         * items == null.
         */
        private ExpandableCardAdapter(final Context context, final List<PlantInfoHolder> items) {
            super(context, R.layout.activity_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content, items);

            setLimit(1);

            options = Constants.imageOptions;

            config = new ImageLoaderConfiguration.Builder(context)
                    .threadPoolSize(Runtime.getRuntime().availableProcessors())
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .writeDebugLogs()
                    .defaultDisplayImageOptions(options)
                    .build();
        }

        @Override
        public View getTitleView(final int position, final View convertView, final ViewGroup parent) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_search_cards, null);

            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.plant_image);

            imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(getItem(position).imageURI, imageView);

            TextView textView = (TextView) linearLayout.findViewById(R.id.plant_name);
            textView.setText(getItem(position).plantName);

            return linearLayout;
        }

        @Override
        public View getContentView(final int position, final View convertView, final ViewGroup parent) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.blank_linearlayout, null);
            for (ArrayList<String> descs : get(position).plantDescArray) {
                LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.plant_detail_adapter, null);
                TextView name = (TextView) layout.findViewById(R.id.name);
                TextView details = (TextView) layout.findViewById(R.id.details);
                name.setText(descs.get(0));
                details.setText(descs.get(1));
                linearLayout.addView(layout);
                Log.d("LAYOUT@" + get(position).plantName + "@position_" + position, "Added attribute [" + descs.get(0) + "] " + descs.get(1));
            }
            return linearLayout;
        }
    }

}

