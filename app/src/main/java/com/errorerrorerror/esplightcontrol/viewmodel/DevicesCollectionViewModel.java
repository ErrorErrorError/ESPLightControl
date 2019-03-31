package com.errorerrorerror.esplightcontrol.viewmodel;

import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.devices.DevicesDataSource;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DevicesCollectionViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private DevicesDataSource devicesDataSource;

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

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
