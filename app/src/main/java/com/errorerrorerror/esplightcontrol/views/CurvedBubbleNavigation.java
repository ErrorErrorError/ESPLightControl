package com.errorerrorerror.esplightcontrol.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.errorerrorerror.esplightcontrol.R;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;

import java.util.Objects;

public class CurvedBubbleNavigation extends BubbleNavigationLinearView {
    private static final Point mFirstCurveStartPoint = new Point();
    private static final Point mFirstCurveEndPoint = new Point();
    private static final Point mSecondCurveStartPoint = new Point();
    private static final Point mSecondCurveEndPoint = new Point();
    private static final Point mFirstCurveControlPoint1 = new Point();
    private static final Point mFirstCurveControlPoint2 = new Point();
    private static final Point mSecondCurveControlPoint1 = new Point();
    private static final Point mSecondCurveControlPoint2 = new Point();
    private static Path mPath;
    private static Paint mPaint;
    private final int CURVE_RADIUS = getResources().getDimensionPixelOffset(R.dimen.corner_radius); //Do not exceed 200


    public CurvedBubbleNavigation(Context context) {
        super(context);
        init();
    }

    public CurvedBubbleNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedBubbleNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Objects.requireNonNull(getBackgroundTintList()).getDefaultColor());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
        }

        mPaint.setShadowLayer(getElevation(), 0, 0, Color.rgb(221, 221, 221)); // This set's color shadow to grey
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        int mNavigationBarWidth = getMeasuredWidth() - getPaddingRight();
        int mNavigationBarHeight = getMeasuredHeight() - getPaddingBottom();


        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(0, CURVE_RADIUS);
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(CURVE_RADIUS, 0);
        // same thing for the second curve
        mSecondCurveStartPoint.set((mNavigationBarWidth - CURVE_RADIUS), 0);
        mSecondCurveEndPoint.set(mNavigationBarWidth, CURVE_RADIUS);


        //Had to multiply by 9 then divide by 10 to find the point it needs to add up to .90. Then from there divide by 2.
        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mFirstCurveControlPoint1.set(0, (((CURVE_RADIUS * 9) / 10) / 2));
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mFirstCurveControlPoint2.set((((CURVE_RADIUS * 9) / 10) / 2), 0);

        mSecondCurveControlPoint1.set(mNavigationBarWidth - mFirstCurveControlPoint1.y, 0);
        mSecondCurveControlPoint2.set(mNavigationBarWidth, mFirstCurveControlPoint1.y);

        //point calculation
        mPath.reset();

        //There is a padding for the shadows, this prevent it from clipping on a custom view
        int padding = (int) getElevation();

        mPath.moveTo(CURVE_RADIUS, padding);
        mPath.lineTo(mSecondCurveStartPoint.x, mSecondCurveStartPoint.y + padding);

        mPath.cubicTo(mSecondCurveControlPoint1.x, mSecondCurveControlPoint1.y + padding,
                mSecondCurveControlPoint2.x, mSecondCurveControlPoint2.y + padding,
                mSecondCurveEndPoint.x, mSecondCurveEndPoint.y + padding);


        mPath.lineTo(mNavigationBarWidth, mNavigationBarHeight + padding);
        mPath.lineTo(0, mNavigationBarHeight + padding);
        mPath.lineTo(0, CURVE_RADIUS + padding);

        mPath.cubicTo(mFirstCurveControlPoint1.x, mFirstCurveControlPoint1.y + padding,
                mFirstCurveControlPoint2.x, mFirstCurveControlPoint2.y + padding,
                mFirstCurveEndPoint.x, mFirstCurveEndPoint.y + padding);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

    }


}


