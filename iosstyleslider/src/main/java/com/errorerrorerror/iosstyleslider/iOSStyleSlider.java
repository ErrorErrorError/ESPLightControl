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
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
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
    private final static int DEFAULT_MIN_VALUE = 0;
    private final static int DEFAULT_MAX_VALUE = 100;
    private final static int DEFAULT_PROGRESS = 80;
    private static final String TAG = "iosstyleslider";

    //Do not change this
    private Paint mPaint;
    //private int mTouchSlop;
    //private int mClickTimeout;
    private Path mSliderPath;
    private RectF mSliderBackgroundF;
    private float mSliderRadius;
    private SliderPoints sliderPoints;
    //private boolean sliderLongPressed;
    private boolean sliderTouched;
    private float widthSliderWPadding;
    private float heightSliderWPadding;
    private VelocityTracker mVelocityTracker;



    //private boolean mMaxInitialized;
    //private boolean mMinInitialized;


    //Users Can Change these values
    private int mSliderColor = DEFAULT_SLIDER_COLOR;
    private int mSliderBackgroundColor = DEFAULT_BACKGROUND_SLIDER_COLOR;
    private float desiredWidth; //Default Width if Wrapped
    private float desiredHeight; //Default Height if Wrapped
    private int mSliderMin = DEFAULT_MIN_VALUE;
    private int mSliderMax = DEFAULT_MAX_VALUE;
    private int mSliderProgress = DEFAULT_PROGRESS;
    //private boolean mSliderOrientation;

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
        mSliderBackgroundColor = ta.getColor(R.styleable.iOSStyleSlider_issColorBackgroundSlider, mSliderBackgroundColor);
        setSliderMin(ta.getInteger(R.styleable.iOSStyleSlider_issSetMinValue, mSliderMin));
        setSlidertMax(ta.getInteger(R.styleable.iOSStyleSlider_issSetMaxValue, mSliderMax));
        setSliderProgress(ta.getInt(R.styleable.iOSStyleSlider_issSetProgressBar, mSliderProgress));
        ta.recycle();
    }

    private void setup() {
        sliderPoints = new SliderPoints();
        widthSliderWPadding = getMeasuredWidth() - getPaddingRight();
        heightSliderWPadding = getMeasuredHeight() - getPaddingBottom();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Backgound Slider
        mPaint.reset();
        mPaint.setColor(mSliderBackgroundColor);
        mSliderBackgroundF.set(0, 0, widthSliderWPadding, heightSliderWPadding);
        canvas.drawRoundRect(mSliderBackgroundF, mSliderRadius, mSliderRadius, mPaint);

        // Log.d(TAG, "Val: " + mSliderProgress);

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
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
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
                if (test >= .4 || test <= -.4)
                {
                    setSliderProgress((int) (mSliderProgress - test));
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
            default:
                animate().translationZ(0);
        }
        return true;
    }

    /*

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        //Log.d(TAG, "onTouchEvent: " + event.getY() + " thumb position: " + getSliderThumbPosition());
                        //sliderLongPressed = true;
                        //sliderTouched = true;
                        //setSliderProgress(calculateSliderHeight(event.getY()));
                        if (event.getY() != test) {
                            if (event.getY() > getSliderThumbPosition()) {
                                mSliderProgress--;
                            } else {
                                mSliderProgress++;
                            }
                        }
                        test = event.getY();
                        break;
                    default:
                        sliderTouched = false;
                        //sliderLongPressed = false;
                        break;
                }
                invalidate();
                return true;
            }
        */

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

    /*
    private int calculateSliderHeight(float fingerSlide) {
        if (fingerSlide > heightSliderWPadding) {
            fingerSlide = heightSliderWPadding;
        } else if (fingerSlide < 0) {
            fingerSlide = 0;
        }
        return (int) (((heightSliderWPadding - fingerSlide) * 100) / heightSliderWPadding);
    }
    */

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

    @Override
    protected Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end
        ss.sliderProgress = this.mSliderProgress;

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        setSliderProgress(ss.sliderProgress);
    }

    static class SavedState extends BaseSavedState {
        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        int sliderProgress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.sliderProgress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.sliderProgress);
        }
    }

    private class CustomOutline extends ViewOutlineProvider {
        int width;
        int height;
        float cornerRadius;
        //int yShift;

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
