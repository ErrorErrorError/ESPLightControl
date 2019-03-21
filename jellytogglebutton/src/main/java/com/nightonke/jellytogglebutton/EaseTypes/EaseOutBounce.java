package com.nightonke.jellytogglebutton.EaseTypes;

/**
 * Created by Weiping on 2016/3/3.
 */

public class EaseOutBounce extends CubicBezier {

    public EaseOutBounce() {

    }

    public float getOffset(float t) {
        float b = 0;
        float c = 1;
        float d = 1;
        if ((t/=d) < (1/2.75f)) {
            return c*(7.5625f*t*t) + b;
        } else if (t < (2/2.75f)) {
            return c*(7.5625f*(t-=(1.5f/2.75f))*t + .75f) + b;
        } else if (t < (2.5/2.75)) {
            return c*(7.5625f*(t-=(2.25f/2.75f))*t + .9375f) + b;
        } else {
            return c*(7.5625f*(t-=(2.625f/2.75f))*t + .984375f) + b;
        }
    }

}
