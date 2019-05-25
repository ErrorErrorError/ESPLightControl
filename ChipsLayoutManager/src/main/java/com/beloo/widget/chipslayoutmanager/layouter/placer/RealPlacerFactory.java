package com.beloo.widget.chipslayoutmanager.layouter.placer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RealPlacerFactory implements IPlacerFactory {

    private RecyclerView.LayoutManager layoutManager;

    RealPlacerFactory(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @NonNull
    @Override
    public IPlacer getAtStartPlacer() {
        return new RealAtStartPlacer(layoutManager);
    }

    @NonNull
    @Override
    public IPlacer getAtEndPlacer() {
        return new RealAtEndPlacer(layoutManager);
    }
}
