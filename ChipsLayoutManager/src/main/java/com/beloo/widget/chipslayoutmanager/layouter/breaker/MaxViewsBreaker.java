package com.beloo.widget.chipslayoutmanager.layouter.breaker;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.layouter.AbstractLayouter;

/** brakes the row in case max views size in row reached */
public class MaxViewsBreaker extends RowBreakerDecorator {

    private int maxViewsInRow;

    MaxViewsBreaker(int maxViewsInRow, ILayoutRowBreaker decorate) {
        super(decorate);
        this.maxViewsInRow = maxViewsInRow;
    }

    @Override
    public boolean isRowBroke(@NonNull AbstractLayouter al) {
        return super.isRowBroke(al)
                || al.getRowSize() >= maxViewsInRow;
    }
}
