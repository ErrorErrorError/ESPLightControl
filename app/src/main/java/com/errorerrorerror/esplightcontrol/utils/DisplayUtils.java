package com.errorerrorerror.esplightcontrol.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.errorerrorerror.esplightcontrol.R;

public class DisplayUtils {

    private final Context context;
    private final ViewGroup.LayoutParams params;
    public DisplayUtils(ViewGroup.LayoutParams params, Context context ){
        this.context = context;
        this.params = params;
    }

    //this code is from http://stackoverflow.com/a/29609679 it is stripped down to items needed
    @NonNull
    private Point getAppUsableScreenSize() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @NonNull
    private Point getRealScreenSize() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        display.getRealSize(size);

        return size;
    }

    public ViewGroup.LayoutParams getRecyclerViewHeight() {
        //Gets the height from bottom nav, top half
        int bottomNavBarHeight = (int) context.getResources().getDimension(R.dimen.bottomNavHeight);
        int viewTopHalf = (int) context.getResources().getDimension(R.dimen.topHalfScreen);

        //gets usable screen size

        Point appUsableSize = getAppUsableScreenSize();
        Point realScreenSize = getRealScreenSize();

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            params.height = ((appUsableSize.y - viewTopHalf - bottomNavBarHeight) + (bottomNavBarHeight / 7));
        }
        //If no navigation bar is set
        else {
            params.height = ((realScreenSize.y - viewTopHalf - bottomNavBarHeight) + (bottomNavBarHeight / 7));
        }
        return params;
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @NonNull
    private static int[] calculateNoOfColumns(Context context, int width) {
        int[] ye = new int[2];
        int widthPixels= context.getResources().getDisplayMetrics().widthPixels;
        int numberOfColumns = widthPixels / width;
        int remaining = widthPixels - (numberOfColumns * width);
//        System.out.println("\nRemaining\t" + remaining + "\nNumber Of Columns\t" + numberOfColumns);
        if (remaining / (2 * numberOfColumns) < 15) {
            numberOfColumns--;
            remaining = widthPixels - (numberOfColumns * width);
        }

        ye[0] = numberOfColumns;
        ye[1] = remaining;
        return ye;
    }
    public static int calculateSpacing(@NonNull Context context, int width) {

        int[] test = calculateNoOfColumns(context, width);
//        System.out.println("\nNumber Of Columns\t"+ numberOfColumns+"\nRemaining Space\t"+remaining+"\nSpacing\t"+remaining/(2*numberOfColumns)+"\nWidth\t"+width+"\nHeight\t"+height+"\nDisplay DPI\t"+displayMetrics.densityDpi+"\nDisplay Metrics Width\t"+displayMetrics.widthPixels);

        return test[1] / (2 * test[0]);
    }

}
