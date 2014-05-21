package kr.kdev.dg1s.biowiki.ui.dictionary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import kr.kdev.dg1s.biowiki.R;

public class ImageAdapter extends BaseAdapter {

    Context context;
    int[] mImg;
    char[] mText;
    LayoutInflater layoutInflater;
    RadioGroup radioGroup;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;

    public ImageAdapter(Context context, int[] img) {
        this.context = context;
        this.mImg = img;
        this.
                radioGroup = new RadioGroup(context);
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mImg.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        View view = layoutInflater.inflate(R.layout.dictionary_gridview_adapter, null);
        final Holder holder;
        holder = new Holder();

        Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), mImg[position]));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
        bitmap.recycle();
        holder.image = (ImageView) view.findViewById(R.id.plant_image);
        holder.image.setImageBitmap(scaledBitmap);
        Log.d("Holder", "Image ID : " + mImg[position]);
        holder.radioButton = (RadioButton) view
                .findViewById(R.id.radiobtn);
        //holder.radioButton.setText();
        view.setTag(holder);
        //holder = (Holder) view.getTag();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.radioButton.performClick();
            }
        });
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((position != mSelectedPosition && mSelectedRB != null)) {
                    mSelectedRB.setChecked(false);
                }
                mSelectedPosition = position;
                mSelectedRB = (RadioButton) v;
            }
        });
        if (mSelectedPosition != position) {
            holder.radioButton.setChecked(false);
        } else {
            holder.radioButton.setChecked(true);
            if (mSelectedRB != null && holder.radioButton != mSelectedRB) {
                mSelectedRB = holder.radioButton;
            }
        }
        return view;
    }

    private class Holder {
        ImageView image;
        RadioButton radioButton;
    }
}
