package kr.kdev.dg1s.biowiki.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.actionbarsherlock.internal.widget.IcsSpinner;

import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.ui.media.MediaGridFragment.Filter;

import java.lang.reflect.Field;

public class CustomSpinner extends IcsSpinner {
    OnItemSelectedListener listener;

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        //only ignore if the old selection is custom date since we may want to click on it again
        if (position == Filter.CUSTOM_DATE.ordinal())
            ignoreOldSelectionByReflection();
        super.setSelection(position);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        this.listener = listener;
    }
    
    private void ignoreOldSelectionByReflection() {
        try {
            Class<?> c = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            Field reqField = c.getDeclaredField("mOldSelectedPosition");
            reqField.setAccessible(true);
            reqField.setInt(this, -1);
        } catch (Exception e) {
            AppLog.e(AppLog.T.MEDIA, e);
        }
    }
}
