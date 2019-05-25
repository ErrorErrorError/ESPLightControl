package com.beloo.widget.chipslayoutmanager.layouter.breaker;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.layouter.AbstractLayouter;

/** this is basis row breaker for {@link com.beloo.widget.chipslayoutmanager.layouter.LTRDownLayouter} */
class LTRForwardRowBreaker implements ILayoutRowBreaker {

    @Override
    public boolean isRowBroke(@NonNull AbstractLayouter al) {
        return al.getViewLeft() > al.getCanvasLeftBorder()
                && al.getViewLeft() + al.getCurrentViewWidth() > al.getCanvasRightBorder();
    }
}
