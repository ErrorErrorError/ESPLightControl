package com.errorerrorerror.iosstyleslider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.Nullable;
import icepick.Icepick;
import icepick.State;


public class IOSStyleSlider extends View {

    /* Default Values */
    private final static int DEFAULT_SLIDER_RADIUS = 75;
    private final static int DEFAULT_SLIDER_COLOR = Color.parseColor("#7673E7");
    private final static int DEFAULT_BACKGROUND_SLIDER_COLOR = Color.parseColor("#efefef");
    private final static int DEFAULT_WIDTH = 100; //Wrapped Default
    private final static int DEFAULT_HEIGHT = 250; //Wrapped default
    private final static int DEFAULT_MIN_VALUE = 0;
    private final static int DEFAULT_MAX_VALUE = 100;
    private final static int DEFAULT_PROGRESS = 80;
    //private static final String TAG = "iosstyleslider";

    @State
    int mSliderProgress = DEFAULT_PROGRESS;
    //Do not change this
    private Paint mPaint;
    private Path mSliderPath;
    private RectF mSliderBackgroundF;
    private float mSliderRadius;
    private SliderPoints sliderPoints;
    private float widthSliderWPadding;
    private float heightSliderWPadding;
    private VelocityTracker mVelocityTracker;

    //Users Can Change these values
    private int mSliderColor = DEFAULT_SLIDER_COLOR;
    private int mSliderBackgroundColor = DEFAULT_BACKGROUND_SLIDER_COLOR;
    private float desiredWidth; //Default Width if Wrapped
    private float desiredHeight; //Default Height if Wrapped
    private int mSliderMin = DEFAULT_MIN_VALUE;
    private int mSliderMax = DEFAULT_MAX_VALUE;

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

    private void init(@Nullable AttributeSet set) {

        setSaveEnabled(true);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSliderBackgroundF = new RectF();
        mSliderPath = new Path();

        Resources res = getResources();
        float density = res.getDisplayMetrics().density;
        mSliderRadius = DEFAULT_SLIDER_RADIUS * density;
        desiredWidth = DEFAULT_WIDTH * density;
        desiredHeight = DEFAULT_HEIGHT * density;

        //Attributes
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.IOSStyleSlider);

        mSliderRadius = ta.getDimension(R.styleable.IOSStyleSlider_issCornerRadius, DEFAULT_SLIDER_RADIUS);
        mSliderColor = ta.getColor(R.styleable.IOSStyleSlider_issColorSlider, mSliderColor);
        mSliderBackgroundColor = ta.getColor(R.styleable.IOSStyleSlider_issColorBackgroundSlider, mSliderBackgroundColor);
        setSliderMin(ta.getInteger(R.styleable.IOSStyleSlider_issSetMinValue, mSliderMin));
        setSlidertMax(ta.getInteger(R.styleable.IOSStyleSlider_issSetMaxValue, mSliderMax));
        setSliderProgress(ta.getInt(R.styleable.IOSStyleSlider_issSetProgressBar, mSliderProgress));
        ta.recycle();
    }

    private void setup() {
        sliderPoints = new SliderPoints();
        widthSliderWPadding = getMeasuredWidth() - getPaddingRight();
        heightSliderWPadding = getMeasuredHeight() - getPaddingBottom();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Backgound Slider
        mPaint.reset();
        mPaint.setColor(mSliderBackgroundColor);
        mSliderBackgroundF.set(0, 0, widthSliderWPadding, heightSliderWPadding);
        canvas.drawRoundRect(mSliderBackgroundF, mSliderRadius, mSliderRadius, mPaint);

        //Slider
        mSliderPath.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSliderColor);
        sliderPoints.setSliderSize(widthSliderWPadding,
                heightSliderWPadding,
                mSliderRadius,
                mSliderProgress,
                mSliderMin,
                mSliderMax);
        mSliderPath.set(sliderPoints.getSliderPath());
        canvas.drawPath(mSliderPath, mPaint);

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                animate().translationZ(10);
                if (mVelocityTracker == null) {

                    // Retrieve a new VelocityTracker object to watch the velocity
                    // of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {

                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }

                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker.computeCurrentVelocity(1, 10);

                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                //Log.d(TAG, "\nY velocity: " + mVelocityTracker.getYVelocity(pointerId));

                float test = mVelocityTracker.getYVelocity(pointerId);
                if (test >= .4 || test <= -.4) {
                    setSliderProgress((int) (mSliderProgress - test));
                }
                break;
            case MotionEvent.ACTION_UP:
                animate().translationZ(0);
                break;
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
            default:
        }
        return true;
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureDimension((int) desiredWidth, widthMeasureSpec),
                measureDimension((int) desiredHeight, heightMeasureSpec));

        setup();
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
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
        return mSliderProgress;
    }

    public void setSliderProgress(int value) {
        if (value > 100) {
            value = 100;
        } else if (value < 0) {
            value = 0;
        }

        if (value != mSliderProgress) {
            this.mSliderProgress = value;
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
