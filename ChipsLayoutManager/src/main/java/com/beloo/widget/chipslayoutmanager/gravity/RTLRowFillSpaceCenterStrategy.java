package com.beloo.widget.chipslayoutmanager.gravity;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.layouter.AbstractLayouter;
import com.beloo.widget.chipslayoutmanager.layouter.Item;

import java.util.List;

class RTLRowFillSpaceCenterStrategy implements IRowStrategy {

    @Override
    public void applyStrategy(@NonNull AbstractLayouter abstractLayouter, @NonNull List<Item> row) {
        int difference = GravityUtil.getHorizontalDifference(abstractLayouter) / (abstractLayouter.getRowSize() + 1);
        int offsetDifference = 0;

        for (Item item : row) {
            Rect childRect = item.getViewRect();

            offsetDifference += difference;

            childRect.right -= offsetDifference;
            childRect.left -= offsetDifference;
        }

    }
}
