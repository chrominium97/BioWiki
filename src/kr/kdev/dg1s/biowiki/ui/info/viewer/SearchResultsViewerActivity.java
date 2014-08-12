package kr.kdev.dg1s.biowiki.ui.info.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.Constants;
import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.info.viewer.utils.CustomListActivity;
import kr.kdev.dg1s.biowiki.ui.info.viewer.utils.PlantInfoHolder;

public class SearchResultsViewerActivity extends CustomListActivity {

    ExpandableCardAdapter mExpandableListItemAdapter;

    boolean hasResults = false;
    ArrayList<String> results;

    @Override
    public void onCreate(final Bundle savedInstanceState) { // original
        super.onCreate(savedInstanceState);

        validateData(getIntent());
        if (false) {

        } else {
            ArrayList<PlantInfoHolder> holders = new ArrayList<PlantInfoHolder>();
            holders.add(new PlantInfoHolder("검색결과 없음", "drawable://" + String.valueOf(R.drawable.remote_image),
                    "검색결과가 없습니다. 검색조건을 줄여서 다시 시도해 보세요."));
            mExpandableListItemAdapter = new ExpandableCardAdapter(this, holders);
        }

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
        alphaInAnimationAdapter.setAbsListView(getListView());
        alphaInAnimationAdapter.setInitialDelayMillis(500);
        getListView().setAdapter(alphaInAnimationAdapter);

        Toast.makeText(this, "한번 고민해보세요", Toast.LENGTH_LONG).show();

        // Added 20140805

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
            LinearLayout linearLayout = (LinearLayout) convertView;


            if (linearLayout == null) {
                linearLayout = new LinearLayout(mContext);

                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                //imageView.setLayoutParams();

                imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(getItem(position).imageURI, imageView);

                TextView textView = new TextView(mContext);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                textView.setText(getItem(position).plantName);

                linearLayout.addView(imageView);
                linearLayout.addView(textView);
            }
            return linearLayout;
        }

        @Override
        public View getContentView(final int position, final View convertView, final ViewGroup parent) {
            TextView textView = (TextView) convertView;
            if (textView == null) {
                //ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT)

                textView = new TextView(mContext);
                //imageView.setLayoutParams();
                textView.setText(getItem(position).plantDesc);
            }
            return textView;
        }
    }

    private void validateData(Intent intent) {
        try {
            final ArrayList<String> list = intent.getExtras().getStringArrayList("plants");
            if (list.size() > 0) {
                hasResults = true;
                results = list;
                Log.v("Data validation", results.toString());
            }
        } catch (NullPointerException e) {
            Log.e("Data validation", "NO APPEND DATA");
        }
    }

}

