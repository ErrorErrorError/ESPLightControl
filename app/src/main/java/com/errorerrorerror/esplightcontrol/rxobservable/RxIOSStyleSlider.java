package com.errorerrorerror.esplightcontrol.rxobservable;

import androidx.annotation.CheckResult;

import com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe.ProgressChangedOnSubscribe;
import com.errorerrorerror.iosstyleslider.IOSStyleSlider;
import com.jakewharton.rxbinding3.InitialValueObservable;

import io.reactivex.annotations.NonNull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RxIOSStyleSlider {
    @CheckResult
    @NonNull
    public static InitialValueObservable<Integer> progressChanged(@NonNull IOSStyleSlider view) {
        checkNotNull(view, "view == null");
        return new ProgressChangedOnSubscribe(view);
    }

    private RxIOSStyleSlider() {
        throw new AssertionError("No instances.");
    }

}
