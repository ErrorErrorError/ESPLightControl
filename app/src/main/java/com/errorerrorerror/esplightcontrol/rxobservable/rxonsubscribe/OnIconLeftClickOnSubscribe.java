package com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe;

import com.tenclouds.swipeablerecyclerviewcell.swipereveal.SwipeRevealLayout;
import com.tenclouds.swipeablerecyclerviewcell.swipereveal.interfaces.OnLeftIconClicked;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding3.internal.Preconditions.checkMainThread;

public class OnIconLeftClickOnSubscribe extends Observable<Object> {
    private final SwipeRevealLayout view;
    private final int mode;

    public OnIconLeftClickOnSubscribe(SwipeRevealLayout view, int mode) {
        this.view = view;
        this.mode = mode;
    }

    @Override
    protected void subscribeActual(Observer<? super Object> observer) {
        if (!checkMainThread(observer)) {
            return;
        }

        Listener listener = new Listener(view, observer);
        observer.onSubscribe(listener);
        view.setOnLeftIconClickedListener(listener, mode);
    }

    private static final class Listener extends MainThreadDisposable implements OnLeftIconClicked {

        private final SwipeRevealLayout view;
        private final Observer<? super Object> observer;

        Listener(SwipeRevealLayout view, Observer<? super Object> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        protected void onDispose() {
            view.removeLeftIconClickListener();
        }

        @Override
        public void onLeftIconClicked() {
            if (!isDisposed()) {
                observer.onNext(this);
            }
        }
    }

}
