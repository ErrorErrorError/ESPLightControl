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
import android.os.Parcelable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import icepick.Icepick;
import icepick.State;


public class IOSStyleSlider extends LinearLayout {

    /* Default Values */

    private final static int DEFAULT_SLIDER_COLOR = Color.parseColor("#7673E7");
    private final static int DEFAULT_BACKGROUND_SLIDER_COLOR = Color.parseColor("#efefef");
    private final static int DEFAULT_MIN_VALUE = 0;
    private final static int DEFAULT_MAX_VALUE = 100;
    private final static int DEFAULT_PROGRESS = 80; //Default progress on a 0 - 100 scale
    private static final String TAG = "iosstyleslider";
    private static final float DEFAULT_BORDER_STROKE = 10f;

    @State
    float mSliderProgress = DEFAULT_PROGRESS;
    //Do not change this
    private Paint mPaint;
    private Path mSliderPath;
    private RectF mSliderBackgroundF;
    private int mSliderRadius;
    private SliderPoints sliderPoints;
    private boolean mSliderEnabled;
    private LottieAnimationView iconView;
    private PorterDuff.Mode mIconTintMode = null;
    private boolean hasIconTintMode = false;
    private TextView textView;
    private int iconResource;
    private int iconTint = 0;
    private boolean hasIconTint = false;
    private List<OnProgressChangedListener> onProgressChangedListener;
    private int textColor = 0;
    private int iconSize = 0;
    private int textSize = 0;
    private GestureDetector gestureDetector;
    private int showIconText;

    //Users Can Change these values
    private int mSliderColor = DEFAULT_SLIDER_COLOR;
    private int mSliderBackgroundColor = DEFAULT_BACKGROUND_SLIDER_COLOR;
    private int mSliderBackgroundColorDisabled = 0;
    private int mSliderColorDisabled = 0;
    private int mSliderMin = DEFAULT_MIN_VALUE;
    private int mSliderMax = DEFAULT_MAX_VALUE;
    private static final float scale = 1.04f;
    private String mText;
    private int mProgressInitialValue = -1;
    private boolean hasSetInitialVal = false;
    private int animatedIconResId = 0;
    private float animatedIconProgress = -1;

    public enum IOSStyleView {
        icon,
        text,
        textIcon,
        iconText
    }

    public interface OnProgressChangedListener {
        /*
         * Detects changes on progress
         */
        void onProgressChanged(IOSStyleSlider slider, int progress);

        void onStartTrackingTouch(IOSStyleSlider slider);

        void onStopTrackingTouch(IOSStyleSlider slider);
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
        setWeightSum(.5f);

        mPaint = new Paint();
        mSliderBackgroundF = new RectF();
        mSliderPath = new Path();
        sliderPoints = new SliderPoints();
        //mState = IOSStyleStates.IDLE;


        //Attributes
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.IOSStyleSlider);
        mSliderRadius = ta.getDimensionPixelSize(R.styleable.IOSStyleSlider_issCornerRadius, getResources().getDimensionPixelSize(R.dimen.radius));
        mSliderColor = ta.getColor(R.styleable.IOSStyleSlider_issColorSlider, mSliderColor);
        mSliderBackgroundColor = ta.getColor(R.styleable.IOSStyleSlider_issColorBackgroundSlider, mSliderBackgroundColor);
        setSliderMin(ta.getInteger(R.styleable.IOSStyleSlider_issSetMinValue, mSliderMin));
        setSlidertMax(ta.getInteger(R.styleable.IOSStyleSlider_issSetMaxValue, mSliderMax));

        if(ta.hasValue(R.styleable.IOSStyleSlider_issAnimatedIcon)){
            animatedIconResId = ta.getResourceId(R.styleable.IOSStyleSlider_issAnimatedIcon, 0);
        }

        if(ta.hasValue(R.styleable.IOSStyleSlider_issAnimatedIconProgress)){
            animatedIconProgress = ta.getFloat(R.styleable.IOSStyleSlider_issAnimatedIconProgress, -1);
        }

        if(ta.hasValue(R.styleable.IOSStyleSlider_issProgressInitialValue)){
            mProgressInitialValue = ta.getInt(R.styleable.IOSStyleSlider_issProgressInitialValue, (int) mSliderProgress);
        } else {
            setSliderProgress(ta.getInt(R.styleable.IOSStyleSlider_issProgress, (int) mSliderProgress));
        }

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

        iconResource = ta.getResourceId(R.styleable.IOSStyleSlider_issIcon, R.drawable.ic_brightness_icon);

        textColor = ta.getColor(R.styleable.IOSStyleSlider_issTextColor, ContextCompat.getColor(getContext(), R.color.textColor));
        iconSize = ta.getDimensionPixelSize(R.styleable.IOSStyleSlider_issIconSize, getResources().getDimensionPixelSize(R.dimen.iconSize));

        textSize = ta.getDimensionPixelSize(R.styleable.IOSStyleSlider_issTextSize, getResources().getDimensionPixelSize(R.dimen.textSize));

        mSliderEnabled = ta.getBoolean(R.styleable.IOSStyleSlider_issSliderEnabled, true);
        mSliderColorDisabled = ta.getColor(R.styleable.IOSStyleSlider_issSliderColorDisabled, 0);
        mSliderBackgroundColorDisabled = ta.getColor(R.styleable.IOSStyleSlider_issColorBackgroundSliderDisabled, 0);

        if (ta.hasValue(R.styleable.IOSStyleSlider_issText)) {
            mText = ta.getText(R.styleable.IOSStyleSlider_issText).toString();
        }

        showIconText = ta.getInteger(R.styleable.IOSStyleSlider_issShowIconText, 3);


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
        if (showIconText == IOSStyleView.icon.ordinal()) {
            addIconView();
        } else if (showIconText == IOSStyleView.text.ordinal()) {
            addTextView();
        } else if (showIconText == IOSStyleView.textIcon.ordinal()) {
            addTextView();
            addIconView();
        } else if(showIconText == IOSStyleView.iconText.ordinal()){
            addIconView();
            addTextView();
        }
    }

    public void setAnimatedIcon(@RawRes int res){
        iconView.setAnimation(res);
        if(res != animatedIconResId){
            animatedIconResId = res;
        }
    }

    public void setAnimatedIconProgress(float progress){
        iconView.setProgress(progress);
        if(animatedIconProgress != progress) {
            animatedIconProgress = progress;
        }
    }

    public int getAnimatedIconResId() {
        return animatedIconResId;
    }

    public void setAnimatedIconResId(int animatedIconResId) {
        this.animatedIconResId = animatedIconResId;
    }

    public float getAnimatedIconProgress() {
        return animatedIconProgress;
    }

    public void enableSlider(boolean enable){
        if(mSliderEnabled != enable){
            mSliderEnabled = enable;
            invalidate();
        }
    }

    public void setDisabledSliderColor(@ColorInt int color){
        if(mSliderColorDisabled != color){
            mSliderColorDisabled = color;
            invalidate();
        }
    }

    public void setDisabledSliderBackgroundColor(@ColorInt int color){
        if(mSliderBackgroundColorDisabled != color){
            mSliderBackgroundColorDisabled = color;
            invalidate();
        }
    }

    private void addIconView() {
        iconView = new LottieAnimationView(getContext());

        if (hasIconTintMode()) {
            iconView.setImageTintMode(mIconTintMode);
        }

        iconView.setImageResource(iconResource);
        if (hasIconTint()) {
            iconView.setImageTintList(ColorStateList.valueOf(iconTint));
        }

        if(animatedIconResId != 0){
            iconView.setAnimation(animatedIconResId);
        }

        if(animatedIconProgress != -1){
            iconView.setProgress(animatedIconProgress);
        }

        addView(iconView);
    }

    private void addTextView() {
        textView = new TextView(getContext());

        textView.setGravity(Gravity.CENTER);
        if (mText != null) {
            textView.setText(mText);
        }
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setId(generateViewId());

        addView(textView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mSliderEnabled) {
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                onStopTrackingTouch();
                getParent().requestDisallowInterceptTouchEvent(false);
                animateView(1.0f, 200);
            }

            if (this.gestureDetector.onTouchEvent(event)) {
                Log.d(TAG, "onTouchEvent: ");
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        } else {
            return false;
        }
    }

    public void animateView(float scale, int duration) {
        animate().scaleX(scale).setDuration(duration).start();
        animate().scaleY(scale).setDuration(duration).start();
    }

    void onStartTrackingTouch() {
        if (onProgressChangedListener != null) {
            for (int i = 0; i < onProgressChangedListener.size(); i++) {
                onProgressChangedListener.get(i).onStartTrackingTouch(this);
            }
        }
    }

    void onStopTrackingTouch() {
        if (onProgressChangedListener != null) {
            for (int i = 0; i < onProgressChangedListener.size(); i++) {
                onProgressChangedListener.get(i).onStopTrackingTouch(this);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupGestureDetectors();
    }

    private void setupGestureDetectors() {
        //This allows for other gestures to occur
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            //This allows for other gestures to occur
            @Override
            public boolean onDown(MotionEvent e) {
                animateView(scale, 200);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                onStartTrackingTouch();
                setSliderProgress(calculateDistance(distanceY));
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
        };

        gestureDetector = new GestureDetector(getContext(), gestureListener);

        //Disable this to have a long click and be able to scroll
        gestureDetector.setIsLongpressEnabled(false);
    }


    private float calculateDistance(float distance) {
        return mSliderProgress + ((distance / getMeasuredHeight()) * 100);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Slider Background
        drawBackgroundSlider(canvas);
        //Slider
        drawSlider(canvas);
    }

    private void drawBackgroundSlider(Canvas canvas){
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSliderBackgroundColor);
        if(!mSliderEnabled && mSliderBackgroundColorDisabled != 0) {
            mPaint.setColor(mSliderBackgroundColorDisabled);
        }
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSliderBackgroundF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRoundRect(mSliderBackgroundF, getSliderRadius(), getSliderRadius(), mPaint);
    }

    public boolean isSliderEnabled() {
        return mSliderEnabled;
    }

    private void drawSlider(Canvas canvas){
        mSliderPath.reset();
        mPaint.setColor(mSliderColor);
        if(!mSliderEnabled && mSliderColorDisabled != 0){
            mPaint.setColor(mSliderColorDisabled);
        }

        if(!hasSetInitialVal && mProgressInitialValue >= 0){
            mSliderProgress = mProgressInitialValue;
            hasSetInitialVal = true;
        }

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

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec),
                measureDimension(desiredHeight, heightMeasureSpec));

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child instanceof LottieAnimationView) {
                child.getLayoutParams().width = iconSize;
            } else if (child instanceof TextView) {
                child.getLayoutParams().width = getMeasuredWidth();
            }
            child.getLayoutParams().height = getMeasuredHeight() / getChildCount();
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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

        if (value != mSliderProgress) {
            this.mSliderProgress = value;
        }

        if (onProgressChangedListener != null) {
            for (int i = 0; i < onProgressChangedListener.size(); i++) {
                onProgressChangedListener.get(i).onProgressChanged(this, (int) mSliderProgress);
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

    public void setProgressInitialValue(int initialProgress){
        this.mProgressInitialValue = initialProgress;
    }

    public boolean hasSetInitialVal(){
        return hasSetInitialVal;
    }
}
