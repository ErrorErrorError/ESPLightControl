package com.errorerrorerror.esplightcontrol.viewmodel;

import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

abstract class DisposableViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String TAG = "DisposableViewModel";


    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
