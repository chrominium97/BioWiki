package kr.kdev.dg1s.biowiki.util;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class BWEditTextPreference extends EditTextPreference {
    public BWEditTextPreference(Context context) {
        super(context);
    }

    public BWEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BWEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (!positiveResult) {
            callChangeListener(null);
        }
    }
}

