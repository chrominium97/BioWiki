package kr.kdev.dg1s.biowiki.ui.info.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    ArrayList<String> results;
    ArrayList<PlantInfoFetcher> fetchers;
    Context context;

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Intent intent = new Intent(SearchResultsViewerActivity.this, PlantInformationViewerActivity.class);
            intent.putExtra("plant", message.getData().getString("plant"));
            startActivity(intent);
        }
    };

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<PlantInfoHolder> holders = new ArrayList<PlantInfoHolder>();
        for (int i = 0; results.size() != 0 && i < results.size(); i++) {
            if (fetchers.get(i).hasImages) {
                holders.add(new PlantInfoHolder(results.get(i),
                        BioWiki.getCurrentBlog().getHomeURL() + "repo/IMG/" + fetchers.get(i).imageThumbnail,
                        fetchers.get(i).plantDetails));
            } else {
                holders.add(new PlantInfoHolder(results.get(i),
                        "drawable://" + String.valueOf(R.drawable.remote_image),
                        fetchers.get(i).plantDetails));
            }
        }
        mExpandableListItemAdapter = new ExpandableCardAdapter(this, holders, handler);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
        alphaInAnimationAdapter.setAbsListView(getListView());
        alphaInAnimationAdapter.setInitialDelayMillis(500);
        getListView().setAdapter(alphaInAnimationAdapter);
        // Added 20140805

    }

    private void instantiateData(Intent intent) {
        context = this;
        fetchers = new ArrayList<PlantInfoFetcher>();
        results = intent.getExtras().getStringArrayList("plants");
        if (results.size() == 0) {
            results.add(getString(R.string.no_search_results));
            fetchers.add(new PlantInfoFetcher(Constants.VOID_PLANT, this));
        } else {
            Log.v("Data validation", results.toString());
            for (String name : results) {
                fetchers.add(new PlantInfoFetcher(name, this));
            }
        }
    }

    private static class ExpandableCardAdapter extends ExpandableListItemAdapter<PlantInfoHolder> {

        Handler handler;

        ImageLoader imageLoader;

        DisplayImageOptions options;
        ImageLoaderConfiguration config;

        /**
         * Creates a new ExpandableListItemAdapter with the specified list, or an empty list if
         * items == null.
         */
        private ExpandableCardAdapter(final Context context, final List<PlantInfoHolder> items, Handler importedHandler) {
            super(context, R.layout.activity_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content, items);
            handler = importedHandler;

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
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_search_cards, null);

            ImageView plantImage = (ImageView) relativeLayout.findViewById(R.id.plant_image);

            imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(getItem(position).imageURI, plantImage);

            final TextView textView = (TextView) relativeLayout.findViewById(R.id.plant_name);
            textView.setText(getItem(position).plantName);

            ImageView newWindow = (ImageView) relativeLayout.findViewById(R.id.new_window);
            if (get(position).plantDescArray.get(0).get(0).equals(mContext.getString(R.string.error) + "!")) {
                newWindow.setVisibility(View.GONE);
            } else {
                newWindow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence("plant", textView.getText());
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                });
            }

            return relativeLayout;
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

