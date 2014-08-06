package kr.kdev.dg1s.biowiki.ui.info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;

public class ViewPlantsActivity extends BIActionBarActivity implements OnDismissCallback {

    private ListView mListView;

    private MyExpandableListItemAdapter mExpandableListItemAdapter;
    private CardsAdapter cardsAdapter;
    private boolean hasResults = false;
    private ArrayList<String> results;

    @Override
    public void onCreate(final Bundle savedInstanceState) { // original
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_cardlist);

        validateData(getIntent());

        mListView = (ListView) findViewById(R.id.default_info_window);
        mListView.setClickable(false);
        mListView.setDivider(null);

        /**
         cardsAdapter = new CardsAdapter(this);
         SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(cardsAdapter, this));
         swingBottomInAnimationAdapter.setInitialDelayMillis(300);
         swingBottomInAnimationAdapter.setAbsListView(mListView);

         mListView.setAdapter(swingBottomInAnimationAdapter);

         cardsAdapter.addAll(getItems());
         */

        // Added 20140805

        mExpandableListItemAdapter = new MyExpandableListItemAdapter(this, getItems());
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
        alphaInAnimationAdapter.setAbsListView(mListView);
        alphaInAnimationAdapter.setInitialDelayMillis(500);
        mListView.setAdapter(alphaInAnimationAdapter);

        Toast.makeText(this, "한번 고민해보세요", Toast.LENGTH_LONG).show();

    }

    private ArrayList<Integer> getItems() { // original
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            items.add(i);
        }
        return items;
    }

    private void validateData(Intent intent) {
        final ArrayList<String> list = intent.getExtras().getStringArrayList("plants");
        if (list.size() > 0) {
            hasResults = true;
            results = list;
            Log.v("Data validation", results.toString());
        }
    }

    @Override
    public void onDismiss(final AbsListView listView, final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            cardsAdapter.remove(position);
        }
    }

    private static class MyExpandableListItemAdapter extends ExpandableListItemAdapter<Integer> {

        private final Context mContext;
        private final LruCache<Integer, Bitmap> mMemoryCache;

        /**
         * Creates a new ExpandableListItemAdapter with the specified list, or an empty list if
         * items == null.
         */
        private MyExpandableListItemAdapter(final Context context, final List<Integer> items) {
            super(context, R.layout.information_cardlist, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content, items);
            mContext = context;

            final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
            mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(final Integer key, final Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }
            };
        }

        @Override
        public View getTitleView(final int position, final View convertView, final ViewGroup parent) {
            TextView tv = (TextView) convertView;
            if (tv == null) {
                tv = new TextView(mContext);
            }
            tv.setText(mContext.getString(android.R.string.emptyPhoneNumber, getItem(position)));
            return tv;
        }

        @Override
        public View getContentView(final int position, final View convertView, final ViewGroup parent) {
            ImageView imageView = (ImageView) convertView;
            if (imageView == null) {
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            int imageResId;
            switch (getItem(position) % 5) {
                case 0:
                    imageResId = R.drawable.d1101;
                    break;
                case 1:
                    imageResId = R.drawable.d1102;
                    break;
                case 2:
                    imageResId = R.drawable.d1103;
                    break;
                case 3:
                    imageResId = R.drawable.d1104;
                    break;
                default:
                    imageResId = R.drawable.d1105;
            }

            Bitmap bitmap = getBitmapFromMemCache(imageResId);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
                addBitmapToMemoryCache(imageResId, bitmap);
            }
            imageView.setImageBitmap(bitmap);

            return imageView;
        }

        private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        private Bitmap getBitmapFromMemCache(final int key) {
            return mMemoryCache.get(key);
        }
    }

    private static class CardsAdapter extends ArrayAdapter<Integer> {

        private final Context mContext;
        private final LruCache<Integer, Bitmap> mMemoryCache;

        public CardsAdapter(final Context context) {
            mContext = context;

            final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
            mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(final Integer key, final Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }
            };
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            ViewHolder viewHolder;
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.information_cards, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) view.findViewById(R.id.card_textview);
                view.setTag(viewHolder);

                viewHolder.imageView = (ImageView) view.findViewById(R.id.card_imageview);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.textView.setText("This is card " + (getItem(position) + 1));
            setImageView(viewHolder, position);

            return view;
        }

        private void setImageView(final ViewHolder viewHolder, final int position) {
            int imageResId;
            switch (getItem(position) % 5) {
                default:
                    imageResId = R.drawable.media_image_placeholder;
            }

            Bitmap bitmap = getBitmapFromMemCache(imageResId);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
                addBitmapToMemoryCache(imageResId, bitmap);
            }
            viewHolder.imageView.setImageBitmap(bitmap);
        }

        private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        private Bitmap getBitmapFromMemCache(final int key) {
            return mMemoryCache.get(key);
        }

        private static class ViewHolder {
            TextView textView;
            ImageView imageView;
        }
    }
}

