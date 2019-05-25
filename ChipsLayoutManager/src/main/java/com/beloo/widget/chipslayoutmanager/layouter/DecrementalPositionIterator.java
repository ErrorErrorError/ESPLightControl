package com.beloo.widget.chipslayoutmanager.layouter;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

class DecrementalPositionIterator extends AbstractPositionIterator {

    DecrementalPositionIterator(@IntRange(from = 0) int itemCount) {
        super(itemCount);
    }

    @Override
    public boolean hasNext() {
        return pos >= 0;
    }

    @NonNull
    @Override
    public Integer next() {
        if (!hasNext()) throw new IllegalStateException("position out of bounds reached");
        return pos--;
    }

}
