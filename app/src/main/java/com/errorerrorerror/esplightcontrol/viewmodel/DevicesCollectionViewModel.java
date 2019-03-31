package com.errorerrorerror.esplightcontrol.viewmodel;

import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.devices.DevicesDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DevicesCollectionViewModel extends DisposableViewModel {

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
}
