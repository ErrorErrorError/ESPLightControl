package com.beloo.widget.chipslayoutmanager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

interface IDisappearingViewsManager {
    @NonNull
    DisappearingViewsManager.DisappearingViewsContainer getDisappearingViews(RecyclerView.Recycler recycler);

    int calcDisappearingViewsLength(RecyclerView.Recycler recycler);

    int getDeletingItemsOnScreenCount();

    void reset();
}
