package com.errorerrorerror.esplightcontrol.rxobservable;

import androidx.annotation.CheckResult;

import com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe.BubbleListenerPositionOnSubscribe;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.jakewharton.rxbinding3.InitialValueObservable;

import io.reactivex.annotations.NonNull;

import static androidx.core.util.Preconditions.checkNotNull;

/*
I edited this code from RxBinding and implemented on BubbleNavigation.
 */

public final class RxBubbleNavigation {

    @CheckResult @NonNull
    public static InitialValueObservable<Integer> bubbleSelections(@NonNull BubbleNavigationLinearView view) {
        checkNotNull(view, "view == null");
        return new BubbleListenerPositionOnSubscribe(view);
    }

    @CheckResult @NonNull
    public static InitialValueObservable<Integer> bubbleSelections(@NonNull BubbleNavigationConstraintView view) {
        checkNotNull(view, "view == null");
        return new BubbleListenerPositionOnSubscribe(view);
    }
    private RxBubbleNavigation() {
        throw new AssertionError("No instances.");
    }

}
