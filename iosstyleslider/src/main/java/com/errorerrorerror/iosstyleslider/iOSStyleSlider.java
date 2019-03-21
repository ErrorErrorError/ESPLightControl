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
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class iOSStyleSlider extends View {

    /* Default Values */
    private final static int DEFAULT_SLIDER_RADIUS = 75;
    private final static int DEFAULT_SLIDER_COLOR = Color.parseColor("#7673E7");
    private final static int DEFAULT_BACKGROUND_SLIDER_COLOR = Color.parseColor("#efefef");
    private final static int DEFAULT_WIDTH = 100; //Wrapped Default
    private final static int DEFAULT_HEIGHT = 250; //Wrapped default
    private final static int DEFAULT_SLIDER_VALUE = 80;
    private final static int DEFAULT_MIN_VALUE = 0;
    private final static int DEFAULT_MAX_VALUE = 0;

    //Do not change this
    private Paint mPaint;
    //private int mTouchSlop;
    //private int mClickTimeout;
    private Path mSliderPath;
    private RectF mSliderBackgroundF;
    private float mSliderRadius;
    private int mSliderValue = DEFAULT_SLIDER_VALUE;
    private SliderPoints sliderPoints = new SliderPoints();
    private boolean sliderDragged;
    private boolean sliderTouched;
    private int widthSliderWPadding;
    private int heightSliderWPadding;


    //Users Can Change these values
    private int mSliderColor = DEFAULT_SLIDER_COLOR;
    private int mSiderBackgroundColor = DEFAULT_BACKGROUND_SLIDER_COLOR;
    private float desiredWidth; //Default Width if Wrapped
    private float desiredHeight; //Default Height if Wrapped
    private int mSliderMin = DEFAULT_MIN_VALUE;
    private int mSliderMax = DEFAULT_MAX_VALUE;


    public iOSStyleSlider(Context context) {
        super(context);
        init(null);
    }

    public iOSStyleSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public iOSStyleSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setSliderValue(int value) {
        if(value > 100)
        {
            value = 100;
        }
        else if (value < 0)
        {
            value = 0;
        }

        this.mSliderValue = value;
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
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.iOSStyleSlider);

        mSliderRadius = ta.getDimension(R.styleable.iOSStyleSlider_issCornerRadius, DEFAULT_SLIDER_RADIUS);
        mSliderColor = ta.getColor(R.styleable.iOSStyleSlider_issColorSlider, mSliderColor);
        mSiderBackgroundColor = ta.getColor(R.styleable.iOSStyleSlider_issColorBackgroundSlider, mSiderBackgroundColor);
        mSliderMin = ta.getInteger(R.styleable.iOSStyleSlider_issSetMinValue, mSliderMin);
        mSliderMax = ta.getInteger(R.styleable.iOSStyleSlider_issSetMaxValue, mSliderMax);

        ta.recycle();
    }

    private void setup() {
        widthSliderWPadding = getMeasuredWidth() - getPaddingRight();
        heightSliderWPadding = getMeasuredHeight() - getPaddingBottom();
    }

    @SuppressLint("DrawAllocation")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Backgound Slider
        mPaint.reset();
        mPaint.setColor(mSiderBackgroundColor);
        mSliderBackgroundF.set(0, 0, widthSliderWPadding, heightSliderWPadding);
        canvas.drawRoundRect(mSliderBackgroundF, mSliderRadius, mSliderRadius, mPaint);


        Log.d("TESTVAL", "Val: " + mSliderValue);

        //Slider
        mSliderPath.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSliderColor);
        sliderPoints.setSliderSize(widthSliderWPadding, heightSliderWPadding, (int) mSliderRadius, (int) mSliderValue);
        mSliderPath.set(sliderPoints.getSliderPath());
        canvas.drawPath(mSliderPath, mPaint);

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean performLongClick() {
        return super.performLongClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                calculateSliderHeight(event.getY());
                sliderDragged = true;
                sliderTouched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d("IOSSTYLE", "onTouchEvent: " + event.getY());

                calculateSliderHeight(event.getY());
                sliderDragged = true;
                sliderTouched = true;
            default:
                sliderTouched = false;
                sliderDragged = false;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Elevation
        setOutlineProvider(new CustomOutline(w, h, mSliderRadius));
        setClipToOutline(true);
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

    private void calculateSliderHeight(float fingerSlide) {
        if (fingerSlide > heightSliderWPadding) {
            fingerSlide = heightSliderWPadding;
        } else if (fingerSlide < 0) {
            fingerSlide = 0;
        }
        mSliderValue = (int) (((heightSliderWPadding - fingerSlide) * 100) / heightSliderWPadding);


    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = (int) mSliderValue;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        setSliderValue((int) ss.value);
        super.onRestoreInstanceState(ss.getSuperState());
    }

    private static class SavedState extends BaseSavedState {
        float value; //this will store the current value from ValueBar

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            value = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeDouble(value);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
