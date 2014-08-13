package kr.kdev.dg1s.biowiki.ui.info.viewer.utils;

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
import kr.kdev.dg1s.biowiki.ui.info.common.utils.ViewHolder;

public class InformationAdapter extends BaseAdapter {
    public List<String> elements = new ArrayList<String>();
    private Context context;

    // Constructor
    public InformationAdapter(Context c, ArrayList<String> arrayList) {
        context = c;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_category, parent, false);
        }
        TextView textView = ViewHolder.get(convertView, R.id.textView);
        textView.setText(elements.get(position));
        textView.setTextColor(context.getResources().getColor(android.R.color.black));
        return convertView;
    }
}
