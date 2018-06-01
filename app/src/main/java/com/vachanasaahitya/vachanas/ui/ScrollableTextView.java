package com.vachanasaahitya.vachanas.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScrollableTextView extends TextView{

    public ScrollableTextView(Context context) {
        super(context);
        init();
    }

    public ScrollableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("NewApi")
    public ScrollableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setScrollContainer(true);
        setMovementMethod(new ScrollingMovementMethod());
    }
}
