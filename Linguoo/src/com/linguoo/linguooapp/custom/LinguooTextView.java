package com.linguoo.linguooapp.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LinguooTextView extends TextView {
    public LinguooTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LinguooTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinguooTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/aleoregular.otf");
            setTypeface(tf);
        }
    }
}
