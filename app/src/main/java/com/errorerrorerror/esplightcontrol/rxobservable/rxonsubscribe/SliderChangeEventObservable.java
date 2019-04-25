package com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe;

import com.errorerrorerror.esplightcontrol.rxobservable.rxevent.SliderChangeEvent;
import com.errorerrorerror.esplightcontrol.rxobservable.rxevent.SliderEndChangeEvent;
import com.errorerrorerror.esplightcontrol.rxobservable.rxevent.SliderProgressChangeEvent;
import com.errorerrorerror.esplightcontrol.rxobservable.rxevent.SliderStartChangeEvent;
import com.errorerrorerror.iosstyleslider.IOSStyleSlider;
import com.jakewharton.rxbinding3.InitialValueObservable;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding3.internal.Preconditions.checkMainThread;

public final class SliderChangeEventObservable extends InitialValueObservable<SliderChangeEvent> {
    private final IOSStyleSlider view;

    public SliderChangeEventObservable(IOSStyleSlider view) {
        this.view = view;
    }

    @Override
    protected SliderChangeEvent getInitialValue() {
        return SliderProgressChangeEvent.create(view, view.getSliderProgress());
    }

    @Override
    protected void subscribeListener(@NotNull Observer<? super SliderChangeEvent> observer) {
        if (!checkMainThread(observer)) {
            return;
        }

        Listener listener = new Listener(view, observer);
        view.addOnProgressChanged(listener);
        observer.onSubscribe(listener);
    }

    private static final class Listener extends MainThreadDisposable implements IOSStyleSlider.OnProgressChangedListener {

        private final IOSStyleSlider view;
        private final Observer<? super SliderChangeEvent> observer;

        Listener(IOSStyleSlider view, Observer<? super SliderChangeEvent> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        protected void onDispose() {
            view.removeProgressChangedListener(this);
        }


        @Override
        public void onProgressChanged(IOSStyleSlider slider, int progress) {
            if(!isDisposed()) {
                observer.onNext(
                        SliderProgressChangeEvent.create(view, progress)
                );
            }
        }

        @Override
        public void onStartTrackingTouch(IOSStyleSlider slider) {
            if(!isDisposed()) {
                observer.onNext(SliderStartChangeEvent.create(slider));
            }
        }

        @Override
        public void onStopTrackingTouch(IOSStyleSlider slider) {
            if(!isDisposed()) {
                observer.onNext(SliderEndChangeEvent.create(slider));
            }
        }
    }

}
