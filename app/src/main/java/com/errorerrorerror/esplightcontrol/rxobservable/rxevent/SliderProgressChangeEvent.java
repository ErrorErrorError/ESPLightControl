package com.errorerrorerror.esplightcontrol.rxobservable.rxevent;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.errorerrorerror.iosstyleslider.IOSStyleSlider;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SliderProgressChangeEvent extends SliderChangeEvent{
    @CheckResult
    @NonNull
    public static SliderProgressChangeEvent create(@NonNull IOSStyleSlider view, int position) {
        return new AutoValue_SliderProgressChangeEvent(view, position);
    }

    SliderProgressChangeEvent() { }

    public abstract int position();
}
