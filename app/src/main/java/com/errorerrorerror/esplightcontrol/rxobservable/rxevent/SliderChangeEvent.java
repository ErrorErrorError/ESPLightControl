package com.errorerrorerror.esplightcontrol.rxobservable.rxevent;

import com.errorerrorerror.iosstyleslider.IOSStyleSlider;

import org.jetbrains.annotations.NotNull;

public abstract class SliderChangeEvent {
    SliderChangeEvent(){}

    @NotNull public abstract IOSStyleSlider view();
}
