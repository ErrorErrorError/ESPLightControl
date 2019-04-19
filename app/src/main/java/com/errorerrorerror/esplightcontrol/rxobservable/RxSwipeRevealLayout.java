package com.errorerrorerror.esplightcontrol.rxobservable;

import androidx.annotation.CheckResult;

import com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe.OnIconLeftClickOnSubscribe;
import com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe.OnIconRightClickOnSubscribe;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RxSwipeRevealLayout {

    @CheckResult
    @NonNull
    public static Observable<Object> rightClickedIcon(@NonNull SwipeRevealLayout view, int mode) {
        checkNotNull(view, "view == null");
        return new OnIconRightClickOnSubscribe(view, mode);
    }

    @CheckResult @NonNull
    public static Observable<Object> leftClickedIcon(@NonNull SwipeRevealLayout view, int mode) {
        checkNotNull(view, "view == null");
        return new OnIconLeftClickOnSubscribe(view, mode);
    }

    private RxSwipeRevealLayout() {
        throw new AssertionError("No instances.");
    }


}
