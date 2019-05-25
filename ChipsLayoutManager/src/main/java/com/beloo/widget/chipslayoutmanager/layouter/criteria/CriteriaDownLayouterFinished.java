package com.beloo.widget.chipslayoutmanager.layouter.criteria;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.layouter.AbstractLayouter;

class CriteriaDownLayouterFinished implements IFinishingCriteria {

    private boolean isFinished;

    @Override
    public boolean isFinishedLayouting(@NonNull AbstractLayouter abstractLayouter) {
        isFinished = isFinished || abstractLayouter.getViewTop() >= abstractLayouter.getCanvasBottomBorder();
        return isFinished;
    }

}
