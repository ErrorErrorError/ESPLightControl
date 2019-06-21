package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static top.defaults.colorpicker.Constants.SELECTOR_RADIUS_DP;

/**
 * HSV color wheel
 */
public class ColorWheelView extends FrameLayout {

    private float radius;
    private float centerX;
    private float centerY;
    private static final String TAG = "ColorWheelView";

    private float mainSelectorRadiusPx = 10 * 3;

    private PointF currentPoint = new PointF();
    private ColorWheelSelector mainSelector;
    private List<ColorWheelSelector> selectorList = new ArrayList<>();

    public ColorWheelView(Context context) {
        this(context, null);
    }

    public ColorWheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ColorWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mainSelectorRadiusPx = SELECTOR_RADIUS_DP * getResources().getDisplayMetrics().density;

        {
            FrameLayout.LayoutParams layoutParams = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            ColorWheelPalette palette = new ColorWheelPalette(context);
            int padding = (int) mainSelectorRadiusPx;
            palette.setPadding(padding, padding, padding, padding);
            addView(palette, layoutParams);
        }

        {
            FrameLayout.LayoutParams layoutParams = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            mainSelector = new ColorWheelSelector(context);
            addView(mainSelector, layoutParams);
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                setColor(Color.WHITE, null);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        width = height = Math.min(maxWidth, maxHeight);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int netWidth = w - getPaddingLeft() - getPaddingRight();
        int netHeight = h - getPaddingTop() - getPaddingBottom();
        radius = Math.min(netWidth, netHeight) * 0.5f - mainSelectorRadiusPx;
        if (radius < 0) return;
        centerX = netWidth * 0.5f;
        centerY = netHeight * 0.5f;
    }

    int getColorAtPoint(float eventX, float eventY) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        float[] hsv = {0, 0, 1};
        hsv[0] = (float) (Math.atan2(y, -x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (r / radius)));
        return Color.HSVToColor(hsv);
    }

    public ColorWheelSelector getMainSelector() {
        return mainSelector;
    }

    public void setColor(int color, ColorWheelSelector selector) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float r = hsv[1] * radius;
        float radian = (float) (hsv[0] / 180f * Math.PI);
        updateSelector((float) (r * Math.cos(radian) + centerX), (float) (-r * Math.sin(radian) + centerY), selector);
        if (selector != null) {
            selector.setColor(color);
        } else {
            mainSelector.setColor(color);
        }
    }

    void updateSelector(float eventX, float eventY, ColorWheelSelector selector) {
        float x = eventX - centerX;
        float y = eventY - centerY;
        double r = Math.sqrt(x * x + y * y);
        if (r > radius) {
            x *= radius / r;
            y *= radius / r;
        }
        currentPoint.x = x + centerX;
        currentPoint.y = y + centerY;
        if (selector != null) {
            selector.setCurrentPoint(currentPoint);
        } else {
            mainSelector.setCurrentPoint(currentPoint);
        }
    }

    public List<ColorWheelSelector> getSelectorList() {
        return selectorList;
    }

    public ColorWheelSelector addSelector(final int color, Context context) {
        FrameLayout.LayoutParams layoutParams = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        final ColorWheelSelector selector = new ColorWheelSelector(context);
        addView(selector, layoutParams);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                setColor(color, selector);
            }
        });
        selectorList.add(selector);
        return selector;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        if (view instanceof ColorWheelSelector) {
            selectorList.remove(view);
        }
    }
}
