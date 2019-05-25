package com.beloo.widget.chipslayoutmanager.gravity;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.layouter.AbstractLayouter;
import com.beloo.widget.chipslayoutmanager.layouter.Item;

import java.util.List;

class LTRRowFillSpaceStrategy implements IRowStrategy {

    @Override
    public void applyStrategy(@NonNull AbstractLayouter abstractLayouter, @NonNull List<Item> row) {
        if (abstractLayouter.getRowSize() == 1) return;
        int difference = GravityUtil.getHorizontalDifference(abstractLayouter) / (abstractLayouter.getRowSize() - 1);
        int offsetDifference = 0;

        for (Item item : row) {
            Rect childRect = item.getViewRect();

            if (childRect.left == abstractLayouter.getCanvasLeftBorder()) {
                //left view of row

                int leftDif = childRect.left - abstractLayouter.getCanvasLeftBorder();
                //press view to left border
                childRect.left = abstractLayouter.getCanvasLeftBorder();
                childRect.right -= leftDif;
                continue;
            }
            offsetDifference += difference;

            childRect.left += offsetDifference;
            childRect.right += offsetDifference;
        }
    }
}
