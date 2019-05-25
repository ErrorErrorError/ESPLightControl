package com.beloo.widget.chipslayoutmanager.layouter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beloo.widget.chipslayoutmanager.gravity.IRowStrategyFactory;
import com.beloo.widget.chipslayoutmanager.gravity.LTRRowStrategyFactory;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IBreakerFactory;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.LTRRowBreakerFactory;

class LTRRowsOrientationStateFactory implements IOrientationStateFactory {

    @NonNull
    @Override
    public ILayouterCreator createLayouterCreator(RecyclerView.LayoutManager lm) {
        return new LTRRowsCreator(lm);
    }

    @NonNull
    @Override
    public IRowStrategyFactory createRowStrategyFactory() {
        return new LTRRowStrategyFactory();
    }

    @NonNull
    @Override
    public IBreakerFactory createDefaultBreaker() {
        return new LTRRowBreakerFactory();
    }
}
