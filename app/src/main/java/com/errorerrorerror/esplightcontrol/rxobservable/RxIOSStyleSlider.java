package com.errorerrorerror.esplightcontrol.rxobservable;

import androidx.annotation.CheckResult;

import com.errorerrorerror.esplightcontrol.rxobservable.rxevent.SliderChangeEvent;
import com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe.SliderChangeEventObservable;
import com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe.SliderOnEndObservable;
import com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe.SliderOnStartObservable;
import com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe.SliderProgressObservable;
import com.errorerrorerror.iosstyleslider.IOSStyleSlider;
import com.jakewharton.rxbinding3.InitialValueObservable;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

import static androidx.core.util.Preconditions.checkNotNull;

public class RxIOSStyleSlider {

    @CheckResult
    @NonNull
    public static InitialValueObservable<SliderChangeEvent> eventChanged(@NonNull IOSStyleSlider view) {
        checkNotNull(view, "view == null");
        return new SliderChangeEventObservable(view);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<Integer> progressChanged(@NonNull IOSStyleSlider view) {
        checkNotNull(view, "view == null");
        return new SliderProgressObservable(view);
    }


    @CheckResult
    @NonNull
    public static Observable<IOSStyleSlider> startTouch(@NonNull IOSStyleSlider view) {
        checkNotNull(view, "view == null");
        return new SliderOnStartObservable(view);
    }

    @CheckResult
    @NonNull
    public static Observable<IOSStyleSlider> endTouch(@NonNull IOSStyleSlider view) {
        checkNotNull(view, "view == null");
        return new SliderOnEndObservable(view);
    }



    private RxIOSStyleSlider() {
        throw new AssertionError("No instances.");
    }

}
