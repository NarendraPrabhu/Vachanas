package com.vachanasaahitya.vachanas.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vachanasaahitya.vachanas.R;

public class PageIndicatorView extends TextView implements ViewPager.OnPageChangeListener{

    private int count = 0;

    public PageIndicatorView(Context context) {
        super(context);
    }

    public PageIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PageIndicatorView,
                0, 0);
        try {
            count = a.getInteger(R.styleable.PageIndicatorView_count, 0);
        }finally {
            a.recycle();
        }
    }

    public PageIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PageIndicatorView,
                0, 0);
        try {
            count = a.getInteger(R.styleable.PageIndicatorView_count, 0);
        }finally {
            a.recycle();
        }
    }

    @SuppressWarnings("NewApi")
    public PageIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PageIndicatorView,
                0, 0);
        try {
            count = a.getInteger(R.styleable.PageIndicatorView_count, 0);
        }finally {
            a.recycle();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        String text = (position+1)+"";
        if(count > 0){
            text += "-"+(count+1);
        }
        setText(text);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
