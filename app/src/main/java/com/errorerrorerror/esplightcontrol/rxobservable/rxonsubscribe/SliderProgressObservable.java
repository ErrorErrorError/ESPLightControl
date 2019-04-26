package com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe;

import com.errorerrorerror.iosstyleslider.IOSStyleSlider;
import com.jakewharton.rxbinding3.InitialValueObservable;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding3.internal.Preconditions.checkMainThread;

public class SliderProgressObservable extends InitialValueObservable<Integer> {

    private final IOSStyleSlider view;

    public SliderProgressObservable(IOSStyleSlider view){
        this.view = view;
    }

    @Override
    protected Integer getInitialValue() {
        return view.getSliderProgress();
    }

    @Override
    protected void subscribeListener(@NotNull Observer<? super Integer> observer) {
        if(!checkMainThread(observer)){
            return;
        }

        Listener listener = new Listener(view, observer);
        view.addOnProgressChanged(listener);
        observer.onSubscribe(listener);
    }

    private static class Listener extends MainThreadDisposable implements IOSStyleSlider.OnProgressChangedListener{

        private final IOSStyleSlider view;
        private final Observer<? super Integer> observer;

        Listener(IOSStyleSlider view, Observer<? super Integer> observer){
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onProgressChanged(IOSStyleSlider slider, int progress) {
            observer.onNext(progress);
        }

        @Override
        public void onStartTrackingTouch(IOSStyleSlider slider) {

        }

        @Override
        public void onStopTrackingTouch(IOSStyleSlider slider) {

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
