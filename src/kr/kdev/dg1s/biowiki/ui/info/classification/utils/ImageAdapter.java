package kr.kdev.dg1s.biowiki.ui.info.classification.utils;

import android.content.Context;
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

import java.io.File;

import kr.kdev.dg1s.biowiki.Constants;
import kr.kdev.dg1s.biowiki.R;

public class ImageAdapter extends BaseAdapter {

    public RadioButton mSelectedRB = null;
    //File cache;
    Context context;
    int[] mImg;
    String[] mText;
    LayoutInflater layoutInflater;
    RadioGroup radioGroup;
    ImageLoaderConfiguration config;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    private int mSelectedPosition = -1;

    public ImageAdapter(Context context, int[] imgs, String[] tags, File file) {
        //cache = file;
        /*
        List<String> nonBlank = new ArrayList<String>();
        for (String s : tags) {
            if (!s.trim().isEmpty()) {
                nonBlank.add(s);
            }
        }
        // nonBlank will have all the elements which contain some characters.
        this.mText = nonBlank.toArray(new String[nonBlank.size()]);
        **/
        this.mText = tags;
        this.context = context;
        this.mImg = imgs;
        this.radioGroup = new RadioGroup(context);
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        options = Constants.imageOptions;

        config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(Runtime.getRuntime().availableProcessors())
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .writeDebugLogs()
                .defaultDisplayImageOptions(options)
                .build();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ImageView plantImage;
        final RadioButton radioButton;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.dictionary_gridview_adapter, parent, false);
            plantImage = (ImageView) convertView.findViewById(R.id.plant_image);
            radioButton = (RadioButton) convertView.findViewById(R.id.radiobtn);
            convertView.setTag(new ViewHolder(plantImage, radioButton));

            ////*******************************************************
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

            imageLoader.displayImage("drawable://" + String.valueOf(mImg[position]), plantImage);
            Log.d("Holder", "Image ID @position " + position + "/" + mImg.length + " : " + mImg[position]);
            radioButton.setId(mImg[position]);
            radioButton.setText(mText[position]);
            //***************************//

        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            plantImage = viewHolder.plantImage;
            radioButton = viewHolder.radioButton;
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton.performClick();
            }
        });

        radioButton.setOnClickListener(new View.OnClickListener() {
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
            radioButton.setChecked(false);
        } else {
            radioButton.setChecked(true);
            if (mSelectedRB != null && radioButton != mSelectedRB) {
                mSelectedRB = radioButton;
            }
        }

        return convertView;

    }

    private static class ViewHolder {

        public final ImageView plantImage;
        public final RadioButton radioButton;

        public ViewHolder(ImageView image, RadioButton button) {
            this.plantImage = image;
            this.radioButton = button;
        }
    }

}
