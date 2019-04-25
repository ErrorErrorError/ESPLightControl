package com.errorerrorerror.esplightcontrol.rxobservable.rxevent;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.errorerrorerror.iosstyleslider.IOSStyleSlider;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SliderStartChangeEvent extends SliderChangeEvent{
    @CheckResult
    @NonNull
    public static SliderStartChangeEvent create(@NonNull IOSStyleSlider view) {
        return new AutoValue_SliderStartChangeEvent(view);
    }

    SliderStartChangeEvent() { }
}
