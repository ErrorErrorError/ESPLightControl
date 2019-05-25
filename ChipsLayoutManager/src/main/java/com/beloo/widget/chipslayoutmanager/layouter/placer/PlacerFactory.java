package com.beloo.widget.chipslayoutmanager.layouter.placer;

import androidx.annotation.NonNull;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;

public class PlacerFactory {

    private ChipsLayoutManager lm;

    public PlacerFactory(ChipsLayoutManager lm) {
        this.lm = lm;
    }

    @NonNull
    public IPlacerFactory createRealPlacerFactory() {
        return new RealPlacerFactory(lm);
    }

    @NonNull
    public IPlacerFactory createDisappearingPlacerFactory() {
        return new DisappearingPlacerFactory(lm);
    }

}
