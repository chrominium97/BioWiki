package kr.kdev.dg1s.biowiki.ui.info;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.R;

public class ElementAdapter extends BaseAdapter {
    public List<String> elements = new ArrayList<String>();
    private Context mContext;

    // Constructor
    public ElementAdapter(Context c, ArrayList<String> arrayList) {
        mContext = c;
        elements = arrayList;
        for (String element : elements) {
            Log.d("Adapter", element);
        }
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.adapter_category, null);
        //textView.setPadding(R.dimen.settings_list_item_padding, R.dimen.settings_list_item_padding,
        //        R.dimen.settings_list_item_padding, R.dimen.settings_list_item_padding);
        textView.setText(elements.get(position));
        textView.setTextColor(mContext.getResources().getColor(R.color.black));
        return textView;
    }
}
