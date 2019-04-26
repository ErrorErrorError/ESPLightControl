package com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe;

import com.errorerrorerror.iosstyleslider.IOSStyleSlider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding3.internal.Preconditions.checkMainThread;

public class SliderOnEndObservable extends Observable<IOSStyleSlider> {
    public final IOSStyleSlider view;

    public SliderOnEndObservable(IOSStyleSlider view) {
        this.view = view;
    }

    @Override
    protected void subscribeActual(Observer<? super IOSStyleSlider> observer) {
        if(!checkMainThread(observer)){
            return;
        }

        Listener listener = new Listener(view, observer);
        observer.onSubscribe(listener);
        view.addOnProgressChanged(listener);

    }

    private static class Listener extends MainThreadDisposable implements IOSStyleSlider.OnProgressChangedListener{
        private final IOSStyleSlider view;
        private final Observer<? super IOSStyleSlider> observer;

        private Listener(IOSStyleSlider view, Observer<? super IOSStyleSlider> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onProgressChanged(IOSStyleSlider slider, int progress) {

        }

        @Override
        public void onStartTrackingTouch(IOSStyleSlider slider) {

        }

        @Override
        public void onStopTrackingTouch(IOSStyleSlider slider) {
            observer.onNext(slider);
        }

        @Override
        public void onSliderEnabled(boolean enabled) {

        }

        @Override
        protected void onDispose() {
            view.removeProgressChangedListener(this);
        }
    }
}
