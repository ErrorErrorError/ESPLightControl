package com.beloo.widget.chipslayoutmanager.layouter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.beloo.widget.chipslayoutmanager.IBorder;

public interface ICanvas extends IBorder {
    @NonNull
    Rect getCanvasRect();

    @NonNull
    Rect getViewRect(View view);

    boolean isInside(Rect rectCandidate);

    boolean isInside(View viewCandidate);

    boolean isFullyVisible(View view);

    boolean isFullyVisible(Rect rect);

    /** calculate border state of layout manager after filling children*/
    void findBorderViews();

    @Nullable
    View getTopView();

    @Nullable
    View getBottomView();

    @Nullable
    View getLeftView();

    @Nullable
    View getRightView();

    Integer getMinPositionOnScreen();

    Integer getMaxPositionOnScreen();

    boolean isFirstItemAdded();
}
