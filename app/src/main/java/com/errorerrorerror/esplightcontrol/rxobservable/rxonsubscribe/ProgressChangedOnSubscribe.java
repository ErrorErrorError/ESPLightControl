package com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe;

import com.errorerrorerror.iosstyleslider.IOSStyleSlider;
import com.jakewharton.rxbinding3.InitialValueObservable;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding3.internal.Preconditions.checkMainThread;

public final class ProgressChangedOnSubscribe  extends InitialValueObservable<Integer> {
    final IOSStyleSlider view;

    public ProgressChangedOnSubscribe(IOSStyleSlider view){
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
        observer.onSubscribe(listener);
        view.addOnProgressChanged(listener);
    }

    private static final class Listener extends MainThreadDisposable implements IOSStyleSlider.OnProgressChangedListener {

        private final IOSStyleSlider view;
        private final Observer<? super Integer> observer;

        Listener(IOSStyleSlider view, Observer<? super Integer> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        protected void onDispose() {
            view.removeProgressChangedListener(this);
        }

        @Override
        public void onProgressChanged(int progress) {
            if(!isDisposed()){
                observer.onNext(progress);
            }
        }
    }

}
