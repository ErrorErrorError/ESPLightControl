package com.errorerrorerror.iosstyleslider;

import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

class SliderPoints {

    private static final String TAG = "sliderpoints";
    private float width;
    private float height;
    private float mSliderRadius;
    private float slider;
    private int min;
    private int max;
    /* PointFs config
       p1 = first point
       p2 = first curve
       p3 = horizontal  point
       p4 = second curve point
       p5 = vertical point
       p6 = third curve
       p7 = horizontal point
       p8 = fourth curve
       */
    private Path mSliderPath = new Path();
    private PointF mSliderP1 = new PointF();
    private PointF mSliderP2 = new PointF();
    private PointF mSliderP1ControlPointF = new PointF();
    private PointF mSliderP2ControlPointF = new PointF();
    private PointF mSliderP3 = new PointF();
    private PointF mSliderP4 = new PointF();
    private PointF mSliderP3ControlPointF = new PointF();
    private PointF mSliderP4ControlPointF = new PointF();
    private PointF mSliderP5 = new PointF();
    private PointF mSliderP6 = new PointF();
    private PointF mSliderP5ControlPointF = new PointF();
    private PointF mSliderP6ControlPointF = new PointF();
    private PointF mSliderP7 = new PointF();
    private PointF mSliderP8 = new PointF();
    private PointF mSliderP7ControlPointF = new PointF();
    private PointF mSliderP8ControlPointF = new PointF();

    SliderPoints() {
    }


    void setSliderSize(float width, float height, float radius, float slider, int min, int max) {
        this.width = width;
        this.height = height;
        this.mSliderRadius = radius;
        this.slider = slider;
        this.min = min;
        this.max = max;
        sliderPoints();
    }

    private void sliderPoints() {

        int range = max - min;
        float val = height - (height * slider) / 100;


        Log.d(TAG, "\nval2: " + val +
                " \nheight: " + height +
                " \nslider: " + slider +
                " \nmax: " + max +
                " \nmin: " + min +
                " \nrange: " + range);

        mSliderP1.set(0, mSliderRadius + val);
        mSliderP2.set(mSliderRadius, val);
        mSliderP1ControlPointF.set(0, (((mSliderRadius * 9) / 10) / 2) + val);
        mSliderP2ControlPointF.set((((mSliderRadius * 9) / 10) / 2), val);

        mSliderP3.set((width - mSliderRadius), val);
        mSliderP4.set(width, mSliderRadius + val);
        mSliderP3ControlPointF.set(width - (((mSliderRadius * 9) / 10) / 2), val);
        mSliderP4ControlPointF.set(width, (((mSliderRadius * 9) / 10) / 2) + val);

        mSliderP5.set(width, (height - mSliderRadius));
        mSliderP6.set((width - mSliderRadius), height);
        mSliderP5ControlPointF.set(width, (height - (((mSliderRadius * 9) / 10) / 2)));
        mSliderP6ControlPointF.set((width - ((mSliderRadius * 9 / 10) / 2)), height);

        mSliderP7.set(mSliderRadius, height);
        mSliderP8.set(0, (height - mSliderRadius));
        mSliderP7ControlPointF.set((((mSliderRadius * 9) / 10) / 2), height);
        mSliderP8ControlPointF.set(0, (height - (((mSliderRadius * 9) / 10) / 2)));


        //Plots Slider
        mSliderPath.reset();
        mSliderPath.moveTo(mSliderP2.x, mSliderP2.y);
        mSliderPath.lineTo(mSliderP3.x, mSliderP3.y);

        mSliderPath.cubicTo(mSliderP3ControlPointF.x, mSliderP3ControlPointF.y,
                mSliderP4ControlPointF.x, mSliderP4ControlPointF.y,
                mSliderP4.x, mSliderP4.y);

        mSliderPath.lineTo(mSliderP5.x, mSliderP5.y);

        mSliderPath.cubicTo(mSliderP5ControlPointF.x, mSliderP5ControlPointF.y,
                mSliderP6ControlPointF.x, mSliderP6ControlPointF.y,
                mSliderP6.x, mSliderP6.y);

        mSliderPath.lineTo(mSliderP7.x, mSliderP7.y);

        mSliderPath.cubicTo(mSliderP7ControlPointF.x, mSliderP7ControlPointF.y,
                mSliderP8ControlPointF.x, mSliderP8ControlPointF.y,
                mSliderP8.x, mSliderP8.y);

        mSliderPath.lineTo(mSliderP1.x, mSliderP1.y);

        mSliderPath.cubicTo(mSliderP1ControlPointF.x, mSliderP1ControlPointF.y,
                mSliderP2ControlPointF.x, mSliderP2ControlPointF.y,
                mSliderP2.x, mSliderP2.y);
        mSliderPath.close();
    }


    Path getSliderPath() {
        return mSliderPath;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getSliderRadius() {
        return mSliderRadius;
    }

    public void setSliderRadius(int mSliderRadius) {
        this.mSliderRadius = mSliderRadius;
    }

    public float getSliderValue() {
        return slider;
    }

    public void setSliderValue(int slider) {
        this.slider = slider;
    }
}
