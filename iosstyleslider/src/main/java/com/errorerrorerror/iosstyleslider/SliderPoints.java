package com.errorerrorerror.iosstyleslider;

import android.graphics.Path;
import android.graphics.Point;

class SliderPoints {

    private int width;
    private int height;
    private int mSliderRadius;
    private int slider;
    /* Points config
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
    private Point mSliderP1 = new Point();
    private Point mSliderP2 = new Point();
    private Point mSliderP1ControlPoint = new Point();
    private Point mSliderP2ControlPoint = new Point();
    private Point mSliderP3 = new Point();
    private Point mSliderP4 = new Point();
    private Point mSliderP3ControlPoint = new Point();
    private Point mSliderP4ControlPoint = new Point();
    private Point mSliderP5 = new Point();
    private Point mSliderP6 = new Point();
    private Point mSliderP5ControlPoint = new Point();
    private Point mSliderP6ControlPoint = new Point();
    private Point mSliderP7 = new Point();
    private Point mSliderP8 = new Point();
    private Point mSliderP7ControlPoint = new Point();
    private Point mSliderP8ControlPoint = new Point();

    SliderPoints() {
    }


    void setSliderSize(int width, int height, int radius, int slider) {
        this.width = width;
        this.height = height;
        this.mSliderRadius = radius;
        this.slider = slider;
        sliderPoints();
    }

    private void sliderPoints() {

        int val = height - (height * slider) / 100;


        mSliderP1.set(0, mSliderRadius + val);
        mSliderP2.set(mSliderRadius, val);
        mSliderP1ControlPoint.set(0, (((mSliderRadius * 9) / 10) / 2) + val);
        mSliderP2ControlPoint.set((((mSliderRadius * 9) / 10) / 2), val);

        mSliderP3.set((width - mSliderRadius), val);
        mSliderP4.set(width, mSliderRadius + val);
        mSliderP3ControlPoint.set(width - (((mSliderRadius * 9) / 10) / 2), val);
        mSliderP4ControlPoint.set(width, (((mSliderRadius * 9) / 10) / 2) + val);

        mSliderP5.set(width, (height - mSliderRadius));
        mSliderP6.set((width - mSliderRadius), height);
        mSliderP5ControlPoint.set(width, (height - (((mSliderRadius * 9) / 10) / 2)));
        mSliderP6ControlPoint.set((width - ((mSliderRadius * 9 / 10) / 2)), height);

        mSliderP7.set(mSliderRadius, height);
        mSliderP8.set(0, (height - mSliderRadius));
        mSliderP7ControlPoint.set((((mSliderRadius * 9) / 10) / 2), height);
        mSliderP8ControlPoint.set(0, (height - (((mSliderRadius * 9) / 10) / 2)));


        //Plots Slider
        mSliderPath.reset();
        mSliderPath.moveTo(mSliderP2.x, mSliderP2.y);
        mSliderPath.lineTo(mSliderP3.x, mSliderP3.y);

        mSliderPath.cubicTo(mSliderP3ControlPoint.x, mSliderP3ControlPoint.y,
                mSliderP4ControlPoint.x, mSliderP4ControlPoint.y,
                mSliderP4.x, mSliderP4.y);

        mSliderPath.lineTo(mSliderP5.x, mSliderP5.y);

        mSliderPath.cubicTo(mSliderP5ControlPoint.x, mSliderP5ControlPoint.y,
                mSliderP6ControlPoint.x, mSliderP6ControlPoint.y,
                mSliderP6.x, mSliderP6.y);

        mSliderPath.lineTo(mSliderP7.x, mSliderP7.y);

        mSliderPath.cubicTo(mSliderP7ControlPoint.x, mSliderP7ControlPoint.y,
                mSliderP8ControlPoint.x, mSliderP8ControlPoint.y,
                mSliderP8.x, mSliderP8.y);

        mSliderPath.lineTo(mSliderP1.x, mSliderP1.y);

        mSliderPath.cubicTo(mSliderP1ControlPoint.x, mSliderP1ControlPoint.y,
                mSliderP2ControlPoint.x, mSliderP2ControlPoint.y,
                mSliderP2.x, mSliderP2.y);
        mSliderPath.close();
    }


    Path getSliderPath() {
        return mSliderPath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSliderRadius() {
        return mSliderRadius;
    }

    public void setSliderRadius(int mSliderRadius) {
        this.mSliderRadius = mSliderRadius;
    }

    public int getSliderValue() {
        return slider;
    }

    public void setSliderValue(int slider) {
        this.slider = slider;
    }
}
