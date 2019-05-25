package com.beloo.widget.chipslayoutmanager.layouter.criteria;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.layouter.AbstractLayouter;

class CriteriaUpAdditionalHeight extends FinishingCriteriaDecorator {

    private int additionalHeight;

    CriteriaUpAdditionalHeight(IFinishingCriteria finishingCriteria, int additionalHeight) {
        super(finishingCriteria);
        this.additionalHeight = additionalHeight;
    }

    @Override
    public boolean isFinishedLayouting(@NonNull AbstractLayouter abstractLayouter) {
        int topBorder = abstractLayouter.getCanvasTopBorder();
        return super.isFinishedLayouting(abstractLayouter) &&
                //if additional height filled
                abstractLayouter.getViewBottom() < topBorder - additionalHeight;
    }

}
