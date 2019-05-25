package com.beloo.widget.chipslayoutmanager.layouter.breaker;

import androidx.annotation.NonNull;

public class ColumnBreakerFactory implements IBreakerFactory {
    @NonNull
    @Override
    public ILayoutRowBreaker createBackwardRowBreaker() {
        return new LTRBackwardColumnBreaker();
    }

    @NonNull
    @Override
    public ILayoutRowBreaker createForwardRowBreaker() {
        return new LTRForwardColumnBreaker();
    }
}
