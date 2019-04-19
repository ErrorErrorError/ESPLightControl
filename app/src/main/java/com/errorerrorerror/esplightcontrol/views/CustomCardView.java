package com.errorerrorerror.esplightcontrol.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.utils.ViewUtils;
import com.google.android.material.card.MaterialCardView;

public class CustomCardView extends MaterialCardView {
    public CustomCardView(Context context) {
        super(context);
        initBackground();
    }

    public CustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackground();
    }

    public CustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }


    private void initBackground() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.contentColor,typedValue, true);
        int color = typedValue.data;

        setBackground(ViewUtils.generateBackgroundWithShadow(this, color,
                R.dimen.corner_radius ,R.color.colorPrimary, (int) getElevation(), Gravity.BOTTOM));
    }
}
