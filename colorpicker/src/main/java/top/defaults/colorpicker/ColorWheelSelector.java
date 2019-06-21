package top.defaults.colorpicker;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class ColorWheelSelector extends ImageView {

    private int color = 0;
    Bitmap bm;
    private ColorObserver colorListener;
    private static final String TAG = "ColorWheelSelector";

    public ColorWheelSelector(Context context) {
        super(context);
        init();
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wheel));
    }

    private float xDelta;
    private float yDelta;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                xDelta = getX() - event.getRawX();
                yDelta = getY() - event.getRawY();

                ((ColorWheelView) getParent()).setColor((((ColorWheelView) getParent()).getColorAtPoint(event.getRawX() + xDelta, event.getRawY() + yDelta)), this);
                return true;

            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                ((ColorWheelView) getParent()).setColor((((ColorWheelView) getParent()).getColorAtPoint(event.getRawX() + xDelta, event.getRawY() + yDelta)), this);
                return true;

            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                ((ColorWheelView) getParent()).setColor((((ColorWheelView) getParent()).getColorAtPoint(event.getRawX() + xDelta, event.getRawY() + yDelta)), this);
                return true;
        }

        ((ColorWheelView) getParent()).invalidate();
        return false;
    }


    public void setCurrentPoint(PointF currentPoint) {
        setX(currentPoint.x - getWidth()/2);
        setY(currentPoint.y - getHeight()/2);
    }

    public void setColorListener(@NonNull ColorObserver colorListener) {
        this.colorListener = colorListener;
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int color) {
        this.color = color;
        if (colorListener != null) {
            colorListener.onColorSelected(color);
        }
    }

    public final ColorObserver getColorObserver() {
        return this.colorListener;
    }

}
