package kr.kdev.dg1s.biowiki.ui.info.fragments.utils;

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

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.kdev.dg1s.biowiki.R;

public class ImageAdapter extends BaseAdapter {

    File cache;
    Context context;
    int[] mImg;
    String[] mText;
    LayoutInflater layoutInflater;
    RadioGroup radioGroup;
    public RadioButton mSelectedRB = null;
    private int mSelectedPosition = -1;

    public ImageAdapter(Context context, int[] img, String[] tags, File file) {
        cache = file;
        List<String> nonBlank = new ArrayList<String>();
        for (String s : tags) {
            if (!s.trim().isEmpty()) {
                nonBlank.add(s);
            }
        }
        // nonBlank will have all the elements which contain some characters.
        this.mText = nonBlank.toArray(new String[nonBlank.size()]);
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
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 2;
        View view = layoutInflater.inflate(R.layout.dictionary_gridview_adapter, null);
        final Holder holder;
        holder = new Holder();

        /**
        Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), mImg[position]));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
        bitmap.recycle();
         */

        ImageLoaderConfiguration config;
        DisplayImageOptions options;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.remote_image)
                .showImageOnFail(R.drawable.remote_failed)
                .build();
        config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(Runtime.getRuntime().availableProcessors())
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .writeDebugLogs()
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        holder.image = (ImageView) view.findViewById(R.id.plant_image);
        imageLoader.displayImage("drawable://" + mImg[position], holder.image);
        //holder.image.setImageBitmap(scaledBitmap);
        Log.d("Holder", "Image ID : " + mImg[position]);
        holder.radioButton = (RadioButton) view
                .findViewById(R.id.radiobtn);
        //holder.radioButton.setText();
        view.setTag(holder);
        //holder = (Holder) view.getTag();

        holder.radioButton.setText(mText[position]);

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
