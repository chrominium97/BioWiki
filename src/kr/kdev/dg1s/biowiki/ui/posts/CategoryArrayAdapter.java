package kr.kdev.dg1s.biowiki.ui.posts;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kr.kdev.dg1s.biowiki.R;
import kr.kdev.dg1s.biowiki.models.CategoryNode;

public class CategoryArrayAdapter extends ArrayAdapter<CategoryNode> {
    int mResourceId;

    public CategoryArrayAdapter(Context context, int resource, List<CategoryNode> objects) {
        super(context, resource, objects);
        mResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(mResourceId, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.categoryRowText);
        ImageView levelIndicatorView = (ImageView) rowView.findViewById(R.id.categoryRowLevelIndicator);
        textView.setText(Html.fromHtml(getItem(position).getName()));
        int level = getItem(position).getLevel();
        if (level == 1) { // hide ImageView
            levelIndicatorView.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams params = levelIndicatorView.getLayoutParams();
            params.width = (params.width / 2) * level;
            levelIndicatorView.setLayoutParams(params);
        }
        return rowView;
    }
}
