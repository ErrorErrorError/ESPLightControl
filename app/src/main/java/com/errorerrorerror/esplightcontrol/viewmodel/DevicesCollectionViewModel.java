package com.errorerrorerror.esplightcontrol.viewmodel;

import androidx.lifecycle.ViewModel;

import com.errorerrorerror.esplightcontrol.model.DevicesDataSource;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;

import java.util.List;

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

    public Flowable<List<Device>> getAllDevices() {
        return devicesDataSource.getAllDevices();
    }

    public Completable deleteDevice(Device device) {
        return Completable.fromAction(() ->
                devicesDataSource.deleteDevice(device));
    }

    public Completable setSwitch(Boolean bool, long id) {
        return Completable.fromAction(() ->
                devicesDataSource.setSwitch(bool, id));
    }

    public Single<Device> getDeviceWithId(long id) {
        return devicesDataSource.getDevice(id);
    }

    public Completable insertDevice(Device device) {
        return Completable.fromAction(() ->
                devicesDataSource.insertDevice(device));
    }

    public Completable updateDevice(Device device) {
        return Completable.fromAction(() ->
                devicesDataSource.updateDevice(device));
    }

    /*
    public void setConnectivity(String connectivity, long id) {
        devicesDataSource.setConnectivity(connectivity, id);
    }
    */

    public Completable updateBrightnessLevel(int progress, long id) {
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


    public Flowable<List<DeviceMusic>> getAllDeviceMusic() {
        return devicesDataSource.getAllDeviceMusic();
    }

    public Completable insertUpdateDeviceMusic(DeviceMusic deviceMusic) {
        return Completable.fromAction(() -> devicesDataSource.insertUpdateDeviceMusic(deviceMusic));
    }
}
