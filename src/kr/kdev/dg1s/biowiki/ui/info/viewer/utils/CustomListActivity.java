package kr.kdev.dg1s.biowiki.ui.info.viewer.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.ui.BIActionBarActivity;

public class CustomListActivity extends BIActionBarActivity {

    public ListView mListView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);
        mListView = (ListView) findViewById(R.id.activity_mylist_listview);
        mListView.setDivider(null);
    }

    public ListView getListView() {
        return mListView;
    }

    /**
     * protected ArrayAdapter<PlantInfoHolder> createListAdapter() {
     * return new MyListAdapter(this, getItems());
     * }
     * <p/>
     * public static ArrayList<PlantInfoHolder> getItems() {
     * ArrayList<Integer> items = new ArrayList<Integer>();
     * for (int i = 0; i < 1000; i++) {
     * items.add(i);
     * }
     * return items;
     * }
     */

    private static class MyListAdapter extends ArrayAdapter<PlantInfoHolder> {

        public MyListAdapter(final Context context, final ArrayList<PlantInfoHolder> items) {
            super(items, context);
        }

        @Override
        public long getItemId(final int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            TextView tv = (TextView) convertView;
            if (tv == null) {
                tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.list_row, parent, false);
            }
            tv.setText("This is row number " + getItem(position));
            return tv;
        }
    }
}