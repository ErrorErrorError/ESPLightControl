package com.beloo.widget.chipslayoutmanager;

import android.view.Gravity;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Gravity.TOP,
        Gravity.BOTTOM,
        Gravity.CENTER,
        Gravity.CENTER_VERTICAL,
        Gravity.CENTER_HORIZONTAL,
        Gravity.LEFT,
        Gravity.RIGHT,
        Gravity.FILL
})
@Retention(RetentionPolicy.SOURCE)
public @interface SpanLayoutChildGravity {}
