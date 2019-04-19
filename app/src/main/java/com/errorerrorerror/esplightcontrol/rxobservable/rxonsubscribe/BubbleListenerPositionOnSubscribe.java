package com.errorerrorerror.esplightcontrol.rxobservable.rxonsubscribe;

import android.view.View;

import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.jakewharton.rxbinding3.InitialValueObservable;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding3.internal.Preconditions.checkMainThread;

public final class BubbleListenerPositionOnSubscribe extends InitialValueObservable<Integer> {
    private final BubbleNavigationConstraintView viewConstraint;
    private final BubbleNavigationLinearView viewLinear;

    public BubbleListenerPositionOnSubscribe(Object view) {
        if (view instanceof BubbleNavigationConstraintView) {
            this.viewConstraint = (BubbleNavigationConstraintView) view;
            this.viewLinear = null;
        } else if (view instanceof BubbleNavigationLinearView) {
            this.viewLinear = (BubbleNavigationLinearView) view;
            this.viewConstraint = null;
        } else {
            throw new RuntimeException("Cannot cast an object to BubbleNavigation");
        }
    }

    @Override
    protected Integer getInitialValue() {
        if (viewLinear == null && viewConstraint != null) {
            return viewConstraint.getCurrentActiveItemPosition();
        } else if (viewConstraint == null && viewLinear != null) {
            return viewLinear.getCurrentActiveItemPosition();
        }
        return -1;
    }

    @Override
    protected void subscribeListener(@NotNull Observer<? super Integer> observer) {
        if(!checkMainThread(observer))
        {
            return;
        }

        if (viewLinear == null && viewConstraint != null) {
            Listener listener = new Listener(viewConstraint, observer);
            observer.onSubscribe(listener);
            viewConstraint.setNavigationChangeListener(listener);
        } else if (viewConstraint == null && viewLinear != null) {
            Listener listener = new Listener(viewLinear, observer);
            observer.onSubscribe(listener);
            viewLinear.setNavigationChangeListener(listener);
        }
    }


    private static final class Listener extends MainThreadDisposable implements BubbleNavigationChangeListener {
        private final Object view;
        private final Observer<? super Integer> observer;

        Listener(Object view, Observer<? super Integer> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onNavigationChanged(View view, int position) {
            if (!isDisposed()) {
                observer.onNext(position);
            }
        }

        @Override
        protected void onDispose() {
            if (view instanceof BubbleNavigationConstraintView) {
                ((BubbleNavigationConstraintView) view).setNavigationChangeListener(null);
            } else if (view instanceof BubbleNavigationLinearView) {
                ((BubbleNavigationLinearView) view).setNavigationChangeListener(null);
            } else {
                throw new RuntimeException("Cannot cast an object to BubbleNavigation");
            }
        }
    }
}
