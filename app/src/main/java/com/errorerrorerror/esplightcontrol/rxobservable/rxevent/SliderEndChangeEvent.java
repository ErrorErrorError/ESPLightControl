package com.errorerrorerror.esplightcontrol.rxobservable.rxevent;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.errorerrorerror.iosstyleslider.IOSStyleSlider;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SliderEndChangeEvent extends SliderChangeEvent {
    @CheckResult
    @NonNull
    public static SliderEndChangeEvent create(@NonNull IOSStyleSlider view) {
        return new AutoValue_SliderEndChangeEvent(view);
    }

    SliderEndChangeEvent() { }
}
