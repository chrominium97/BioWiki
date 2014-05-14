package kr.kdev.dg1s.biowiki.ui.category;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        TextView textView = new TextView(mContext);
        textView.setText(elements.get(position));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        return textView;
    }
}
