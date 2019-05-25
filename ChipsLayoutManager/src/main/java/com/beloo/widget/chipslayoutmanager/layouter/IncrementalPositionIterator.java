package com.beloo.widget.chipslayoutmanager.layouter;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

class IncrementalPositionIterator extends AbstractPositionIterator {

    IncrementalPositionIterator(@IntRange(from = 0) int itemCount) {
        super(itemCount);
    }

    @Override
    public boolean hasNext() {
        return pos < itemCount;
    }

    @NonNull
    @Override
    public Integer next() {
        if (!hasNext()) throw new IllegalStateException("position out of bounds reached");
        return pos++;
    }

}
