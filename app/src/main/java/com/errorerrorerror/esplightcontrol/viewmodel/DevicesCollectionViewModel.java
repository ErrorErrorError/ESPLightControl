package com.errorerrorerror.esplightcontrol.viewmodel;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;

import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.devices.DevicesDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DevicesCollectionViewModel extends ViewModel implements Observable{
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private DevicesDataSource devicesDataSource;
    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    DevicesCollectionViewModel(DevicesDataSource devicesDataSource) {
        this.devicesDataSource = devicesDataSource;
    }

    public Flowable<List<Devices>> getAllDevices() {
        return devicesDataSource.getAllDevices();
    }

    public Completable deleteDevice(Devices devices) {
        return Completable.fromAction(() ->
                devicesDataSource.deleteDevice(devices));
    }

    public Completable setSwitch(Boolean bool, long id) {
        return Completable.fromAction(() ->
                devicesDataSource.setSwitch(bool, id));
    }

    public Single<Devices> getDeviceWithId(long id) {
        return devicesDataSource.getDevice(id);
    }

    public Completable insertEditDevice(Devices device) {
        return Completable.fromAction(() ->
                devicesDataSource.insertEditDevice(device));
    }

    /*
    public void setConnectivity(String connectivity, long id) {
        devicesDataSource.setConnectivity(connectivity, id);
    }
    */

    public Completable updateBrightnessLevel(int progress, long id){
        return Completable.fromAction(() ->
                devicesDataSource.setBrightnessLevel(progress, id));
    }

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public final ObservableField<List<Devices>> devicesXML = new ObservableField<>();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }

    void notifyChange() {
        callbacks.notifyCallbacks(this, 0, null);
    }

    void notifyPropertyChanged(int fieldId) {
        callbacks.notifyCallbacks(this, fieldId, null);
    }


}
