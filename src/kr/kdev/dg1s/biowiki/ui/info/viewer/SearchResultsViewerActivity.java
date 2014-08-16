package kr.kdev.dg1s.biowiki.ui.info.viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    LinearLayout infoContainer;

    boolean hasResults = false;
    ArrayList<String> results;

    View plantDetails(String token, String value) {
        LinearLayout plantDetails = (LinearLayout) LayoutInflater.from(this)
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
    public void onCreate(final Bundle savedInstanceState) { // original
        title = getResources().getString(R.string.searchResultsTitle);
        super.onCreate(savedInstanceState);
        validateData(getIntent());

        if (hasResults) {
            ArrayList<PlantInfoHolder> holders = new ArrayList<PlantInfoHolder>();
            for (String plant : results) {
                holders.add(new PlantInfoHolder(plant, "drawable://" + String.valueOf(R.drawable.remote_image),
                        "더더더더더더미"));
                Log.d("Holder", "added " + plant + " to stack");
            }
            mExpandableListItemAdapter = new ExpandableCardAdapter(this, holders);
            /**
             try {
             Source dictionaryAssets = new Source(getResources().getAssets().open("xmls/kingdom.xml"));
             int success = 0;
             for (String name : results) {
             try {
             Log.d("XML", "Searching for details on " + name);
             Element element = dictionaryAssets.getFirstElement("name", "미나리", false);
             Attributes attributes = element.getAttributes();
             for (Attribute attribute : attributes) {
             if (!attribute.getName().equals("image")) {
             infoContainer.addView(plantDetails(attribute.getName(), attribute.getValue()));
             }
             }
             success++;
             } catch (NullPointerException e) {
             Log.d("XML", "No results");
             }
             } if (success == 0){
             returnNoResults();
             }
             } catch (IOException e) {
             Log.e("Search", "ERROR LOADING ASSETS!!");
             returnNoResults();
             }
             */


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
        ArrayList<PlantInfoHolder> holders = new ArrayList<PlantInfoHolder>();
        holders.add(new PlantInfoHolder("검색결과 없음", "drawable://" + String.valueOf(R.drawable.remote_image),
                "검색결과가 없습니다. 검색조건을 줄여서 다시 시도해 보세요."));
        mExpandableListItemAdapter = new ExpandableCardAdapter(this, holders);
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
            LinearLayout
                    linearLayout = new LinearLayout(mContext);

            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setLayoutParams();

            imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(getItem(position).imageURI, imageView);

            TextView textView = new TextView(mContext);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setText(getItem(position).plantName);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            linearLayout.setTag(position);

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

}

