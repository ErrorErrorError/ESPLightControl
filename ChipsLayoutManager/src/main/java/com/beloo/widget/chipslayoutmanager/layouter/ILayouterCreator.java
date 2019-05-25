package com.beloo.widget.chipslayoutmanager.layouter;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.anchor.AnchorViewState;

interface ILayouterCreator {
    //---- up layouter below
    @NonNull
    Rect createOffsetRectForBackwardLayouter(@NonNull AnchorViewState anchorRect);

    @NonNull
    AbstractLayouter.Builder createBackwardBuilder();

    @NonNull
    AbstractLayouter.Builder createForwardBuilder();

    @NonNull
    Rect createOffsetRectForForwardLayouter(AnchorViewState anchorRect);
}
