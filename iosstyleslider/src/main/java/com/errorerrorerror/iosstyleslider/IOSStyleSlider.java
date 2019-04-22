package com.errorerrorerror.iosstyleslider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import icepick.Icepick;
import icepick.State;


public class IOSStyleSlider extends LinearLayout {

    /* Default Values */

    private final static int DEFAULT_SLIDER_COLOR = Color.parseColor("#7673E7");
    private final static int DEFAULT_BACKGROUND_SLIDER_COLOR = Color.parseColor("#efefef");
    private final static int DEFAULT_WIDTH = 100; //Wrapped Default
    private final static int DEFAULT_HEIGHT = 250; //Wrapped default
    private final static int DEFAULT_MIN_VALUE = 0;
    private final static int DEFAULT_MAX_VALUE = 100;
    private final static int DEFAULT_PROGRESS = 80;
    private final static String DEFAULT_TEXT = "Test";
    private static final String TAG = "iosstyleslider";

    @State
    float mSliderProgress = DEFAULT_PROGRESS;
    //Do not change this
    private Paint mPaint;
    private Path mSliderPath;
    private RectF mSliderBackgroundF;
    private int mSliderRadius;
    private SliderPoints sliderPoints;
    private boolean mDisableSlider = false;
    private VelocityTracker mVelocityTracker;
    private ImageView iconView;
    private PorterDuff.Mode mIconTintMode = null;
    private boolean hasIconTintMode = false;
    private TextView textView;
    private int iconTint = 0;
    private boolean hasIconTint = false;
    private List<OnProgressChangedListener> onProgressChangedListener;
    private List<OnStateChangedListener> onStateChangedListener;
    private Drawable iconSource;
    private IOSStyleStates mState = null;
    private int textColor = 0;
    private int iconSize = 0;
    private int textSize = 0;
    private GestureDetector gestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener;
    private int minDistRequestDisallowParent = 0;

    private boolean isScrolling = false;

    //Users Can Change these values
    private int mSliderColor = DEFAULT_SLIDER_COLOR;
    private int mSliderBackgroundColor = DEFAULT_BACKGROUND_SLIDER_COLOR;
    private int mSliderMin = DEFAULT_MIN_VALUE;
    private int mSliderMax = DEFAULT_MAX_VALUE;
    private static final float scale = 1.04f;
    private static final long times = 200;
    private String mText;

    public enum IOSStyleStates {
        IDLE,
        SLIDING,
        DISABLED
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int progress);
    }

    public interface OnStateChangedListener {
        void onStateChanged(IOSStyleStates IOSStyleStates, int progress);
    }

    public IOSStyleSlider(Context context) {
        super(context);
        init(null);
    }

    public IOSStyleSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public IOSStyleSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void removeProgressChangedListener(OnProgressChangedListener listener) {
        if (this.onProgressChangedListener != null) {
            this.onProgressChangedListener.remove(listener);
        }
    }

    public void removeStateChangedListener(OnStateChangedListener listener) {
        if (this.onStateChangedListener != null) {
            this.onStateChangedListener.remove(listener);
        }
    }

    public void removeTextChangedListener(TextWatcher listener) {
        if (textView != null) {
            this.textView.removeTextChangedListener(listener);
        }
    }

    private void init(@Nullable AttributeSet set) {

        setSaveEnabled(true);
        setWillNotDraw(false);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        onProgressChangedListener = null;
        onStateChangedListener = null;

        mPaint = new Paint();
        mSliderBackgroundF = new RectF();
        mSliderPath = new Path();
        sliderPoints = new SliderPoints();
        mState = IOSStyleStates.IDLE;


        //Attributes
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.IOSStyleSlider);
        mSliderRadius = ta.getDimensionPixelSize(R.styleable.IOSStyleSlider_issCornerRadius, getResources().getDimensionPixelSize(R.dimen.radius));
        mSliderColor = ta.getColor(R.styleable.IOSStyleSlider_issColorSlider, mSliderColor);
        mSliderBackgroundColor = ta.getColor(R.styleable.IOSStyleSlider_issColorBackgroundSlider, mSliderBackgroundColor);
        setSliderMin(ta.getInteger(R.styleable.IOSStyleSlider_issSetMinValue, mSliderMin));
        setSlidertMax(ta.getInteger(R.styleable.IOSStyleSlider_issSetMaxValue, mSliderMax));
        setSliderProgress(ta.getInt(R.styleable.IOSStyleSlider_issSetProgressBar, (int) mSliderProgress));

        if (ta.hasValue(R.styleable.IOSStyleSlider_issIconTint)) {
            iconTint = ta.getColor(R.styleable.IOSStyleSlider_issIconTint, ContextCompat.getColor(getContext(), R.color.iconColor));
            hasIconTint = true;

            mIconTintMode = PorterDuff.Mode.SRC_ATOP;
            hasIconTintMode = true;
        }


        if (ta.hasValue(R.styleable.IOSStyleSlider_issIconTintMode)) {
            mIconTintMode = (PorterDuff.Mode.values()[ta.getInt(R.styleable.IOSStyleSlider_issIconTintMode, 0)]);
            hasIconTintMode = true;
        }

        textColor = ta.getColor(R.styleable.IOSStyleSlider_issTextColor, ContextCompat.getColor(getContext(), R.color.textColor));
        iconSize = ta.getDimensionPixelSize(R.styleable.IOSStyleSlider_issIconSize, getResources().getDimensionPixelSize(R.dimen.iconSize));

        textSize = ta.getDimensionPixelSize(R.styleable.IOSStyleSlider_issTextSize, getResources().getDimensionPixelSize(R.dimen.textSize));

        if (ta.hasValue(R.styleable.IOSStyleSlider_issText)) {
            mText = ta.getText(R.styleable.IOSStyleSlider_issText).toString();
        }

        iconSource = (ta.getDrawable(R.styleable.IOSStyleSlider_issIcon) != null) ? ta.getDrawable(R.styleable.IOSStyleSlider_issIcon) : ContextCompat.getDrawable(getContext(), R.drawable.ic_brightness_icon);


        setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.minWidth));
        setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.minHeight));
        ta.recycle();

        addViews();
    }

    public void addOnProgressChanged(OnProgressChangedListener progressChangedListener) {
        if (this.onProgressChangedListener == null) {
            onProgressChangedListener = new ArrayList<>();
        }
        this.onProgressChangedListener.add(progressChangedListener);
    }

    public List<OnProgressChangedListener> getOnProgressChanged() {
        return onProgressChangedListener;
    }

    public void addTextChangedListener(TextWatcher textChangedListener) {
        this.textView.addTextChangedListener(textChangedListener);
    }


    public void addOnStateChanged(OnStateChangedListener stateChangedListener) {
        if (this.onStateChangedListener == null) {
            onStateChangedListener = new ArrayList<>();
        }
        this.onStateChangedListener.add(stateChangedListener);
    }

    public List<OnStateChangedListener> getOnStateChanged() {
        return onStateChangedListener;
    }

    public boolean hasIconTintMode() {
        return hasIconTintMode;
    }

    public boolean hasIconTint() {
        return hasIconTint;
    }

    public void setIconTintMode(PorterDuff.Mode mIconTintMode) {
        this.mIconTintMode = mIconTintMode;
        hasIconTintMode = true;
        iconView.setImageTintMode(mIconTintMode);
    }

    private void addViews() {
        iconView = new ImageView(getContext());
        textView = new TextView(getContext());

        if (hasIconTintMode()) {
            iconView.setImageTintMode(mIconTintMode);
        }

        iconView.setImageDrawable(iconSource);
        if (hasIconTint()) {
            iconView.setImageTintList(ColorStateList.valueOf(iconTint));
        }


        iconView.setId(generateViewId());
        textView.setGravity(Gravity.CENTER);
        if (mText != null) {
            textView.setText(mText);
        }
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setId(generateViewId());

        addView(iconView);
        addView(textView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            isScrolling = false;
            getParent().requestDisallowInterceptTouchEvent(false);
            animate().scaleX(1.0f).setDuration(200).start();
            animate().scaleY(1.0f).setDuration(200).start();

        }

        if (this.gestureDetector.onTouchEvent(event)) {
            Log.d(TAG, "onTouchEvent: ");
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupGestureDetectors();
    }

    private void setupGestureDetectors() {
        gestureListener = new GestureDetector.SimpleOnGestureListener() {
            //This allows for other gestures to occur
            @Override
            public boolean onDown(MotionEvent e) {
                animate().scaleX(scale).setDuration(200).start();
                animate().scaleY(scale).setDuration(200).start();
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                isScrolling = true;
                setSliderProgress(calculateDistance(distanceY));
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
        };

        gestureDetector = new GestureDetector(getContext(), gestureListener);
    }


    private float calculateDistance(float distance) {
        return mSliderProgress + ((distance / getMeasuredHeight()) * 100);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Slider Background
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSliderBackgroundColor);
        mSliderBackgroundF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRoundRect(mSliderBackgroundF, getSliderRadius(), getSliderRadius(), mPaint);

        //Slider
        mSliderPath.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSliderColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        sliderPoints.setSliderSize(getMeasuredWidth(),
                getMeasuredHeight(),
                mSliderRadius,
                mSliderProgress,
                getSliderMin(),
                getSliderMax());
        mSliderPath.set(sliderPoints.getSliderPath());
        canvas.drawPath(mSliderPath, mPaint);
    }

    public int getSliderMin() {
        return this.mSliderMin;
    }

    public int getSliderMax() {
        return this.mSliderMax;
    }

    public int getSliderRadius() {
        return this.mSliderRadius;
    }

    public void setText(String text) {
        this.mText = text;
        textView.setText(text);
    }

    public String getText() {
        return this.mText;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Elevation
        setOutlineProvider(new CustomOutline(w, h, mSliderRadius));
        setClipToOutline(true);
    }

    public void addOnProgressState(IOSStyleStates state) {
        this.mState = state;

        if (onStateChangedListener != null) {
            for (int i = 0; i < onStateChangedListener.size(); i++) {
                onStateChangedListener.get(i).onStateChanged(state, (int) this.mSliderProgress);
            }
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();


        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec),
                measureDimension(desiredHeight, heightMeasureSpec));

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child instanceof ImageView) {
                child.getLayoutParams().width = iconSize;
            } else if (child instanceof TextView) {
                child.getLayoutParams().width = getMeasuredWidth();
            }
            child.getLayoutParams().height = getMeasuredHeight() / 2;
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }


    private int measureDimension(int desiredSize, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            desiredSize = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                desiredSize = Math.min(desiredSize, specSize);
            }
        }

        return desiredSize;
    }

    public void setSliderMin(int min) {
        if (min >= mSliderMax) {
            mSliderMax = min;
        } else {
            mSliderMin = min;
        }
    }

    public void setSlidertMax(int max) {
        if (max <= mSliderMin) {
            mSliderMin = max;
        } else {
            mSliderMax = max;
        }
    }

    public int getSliderProgress() {
        return Math.round(this.mSliderProgress);
    }

    public void setSliderProgress(float value) {
        if (value > mSliderMax) {
            value = mSliderMax;
        } else if (value < mSliderMin) {
            value = mSliderMin;
        }
        //Log.d(TAG, "setSliderProgress: " + value);

        if (value != mSliderProgress) {
            this.mSliderProgress = value;
        }

        if (onProgressChangedListener != null) {
            for (int i = 0; i < onProgressChangedListener.size(); i++) {
                onProgressChangedListener.get(i).onProgressChanged((int) value);
            }
        }
        invalidate();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
    }

    private class CustomOutline extends ViewOutlineProvider {
        int width;
        int height;
        float cornerRadius;

        CustomOutline(int width, int height, float cornerRadius) {
            this.width = width;
            this.height = height;
            this.cornerRadius = cornerRadius;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, width, height, cornerRadius);
        }
    }
}
