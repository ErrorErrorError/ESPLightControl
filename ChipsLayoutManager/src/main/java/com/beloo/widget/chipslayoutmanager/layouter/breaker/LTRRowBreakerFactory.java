package com.beloo.widget.chipslayoutmanager.layouter.breaker;

import androidx.annotation.NonNull;

public class LTRRowBreakerFactory implements IBreakerFactory {
    @NonNull
    @Override
    public ILayoutRowBreaker createBackwardRowBreaker() {
        return new LTRBackwardRowBreaker();
    }

    @NonNull
    @Override
    public ILayoutRowBreaker createForwardRowBreaker() {
        return new LTRForwardRowBreaker();
    }
}
