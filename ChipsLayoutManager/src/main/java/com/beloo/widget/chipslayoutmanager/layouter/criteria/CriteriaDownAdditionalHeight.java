package com.beloo.widget.chipslayoutmanager.layouter.criteria;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.layouter.AbstractLayouter;

class CriteriaDownAdditionalHeight extends FinishingCriteriaDecorator {

    private int additionalHeight;

    CriteriaDownAdditionalHeight(IFinishingCriteria finishingCriteria, int additionalHeight) {
        super(finishingCriteria);
        this.additionalHeight = additionalHeight;
    }

    @Override
    public boolean isFinishedLayouting(@NonNull AbstractLayouter abstractLayouter) {
        int bottomBorder = abstractLayouter.getCanvasBottomBorder();
        return super.isFinishedLayouting(abstractLayouter) &&
                //if additional height filled
                abstractLayouter.getViewTop() > bottomBorder + additionalHeight;
    }

}
