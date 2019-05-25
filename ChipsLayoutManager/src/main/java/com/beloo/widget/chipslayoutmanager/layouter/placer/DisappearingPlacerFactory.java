package com.beloo.widget.chipslayoutmanager.layouter.placer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class DisappearingPlacerFactory implements IPlacerFactory {

    private RecyclerView.LayoutManager layoutManager;

    DisappearingPlacerFactory(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @NonNull
    @Override
    public IPlacer getAtStartPlacer() {
        return new DisappearingViewAtStartPlacer(layoutManager);
    }

    @NonNull
    @Override
    public IPlacer getAtEndPlacer() {
        return new DisappearingViewAtEndPlacer(layoutManager);
    }
}
