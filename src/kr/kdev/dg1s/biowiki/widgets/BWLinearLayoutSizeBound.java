package kr.kdev.dg1s.biowiki.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import kr.kdev.dg1s.biowiki.R;


public class BWLinearLayoutSizeBound extends LinearLayout {
    private final int mMaxWidth;
    private final int mMaxHeight;

    public BWLinearLayoutSizeBound(Context context) {
        super(context);
        mMaxWidth = 0;
        mMaxHeight = 0;
    }

    public BWLinearLayoutSizeBound(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.BWLinearLayoutSizeBound);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.BWLinearLayoutSizeBound_maxWidth,
                Integer.MAX_VALUE);
        mMaxHeight = a.getDimensionPixelSize(R.styleable.BWLinearLayoutSizeBound_maxHeight,
                Integer.MAX_VALUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (mMaxWidth > 0 && mMaxWidth < measuredWidth) {
            int measureMode = MeasureSpec.getMode(widthMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, measureMode);
        }
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mMaxHeight > 0 && mMaxHeight < measuredHeight) {
            int measureMode = MeasureSpec.getMode(heightMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, measureMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
