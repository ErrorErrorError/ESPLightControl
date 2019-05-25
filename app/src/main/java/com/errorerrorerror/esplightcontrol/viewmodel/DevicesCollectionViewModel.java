package com.errorerrorerror.esplightcontrol.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.errorerrorerror.esplightcontrol.model.MainRepository;
import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;
import com.errorerrorerror.esplightcontrol.model.device_solid.DeviceSolid;
import com.errorerrorerror.esplightcontrol.model.device_waves.DeviceWaves;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DevicesCollectionViewModel extends ViewModel {
    @NonNull
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MainRepository devicesDataSource;


    DevicesCollectionViewModel(MainRepository devicesDataSource) {
        this.devicesDataSource = devicesDataSource;
    }

    public Observable<List<Device>> getAllDevices() {
        return Observable.combineLatest(getAllDeviceMusic()
                        .map(i -> (List<Device>) new ArrayList<Device>(i))
                , getAllDeviceWaves()
                        .map(i -> (List<Device>) new ArrayList<Device>(i))
                , getAllDeviceSolid()
                        .map(i -> (List<Device>) new ArrayList<Device>(i)),
                (devices, devices2, devices3) -> {
                    List<Device> t = new ArrayList<>(devices);
                    t.addAll(devices2);
                    t.addAll(devices3);
                    Collections.sort(t, (o1, o2) -> (int) (o2.getId() - o1.getId()));
                    return t;
                });
    }

    public Completable setSwitch(Boolean bool, Device device) {
        return (device instanceof DeviceMusic) ?
                devicesDataSource.getMusicRepository().setSwitch(bool, device.getId()) :
                (device instanceof DeviceSolid) ?
                        devicesDataSource.getSolidRepository().setSwitch(bool, device.getId()) :
                        (device instanceof DeviceWaves) ?
                                devicesDataSource.getWavesRepository().setSwitch(bool, device.getId()) : Completable.error(() -> new Throwable("Cannot Update Switch With Unknown Device"));
    }

    public Single<Device> getDeviceWithId(long id) {
        return getAllDevices()
                .flatMapIterable(devices -> devices)
                .filter(device -> device.getId() == id).firstOrError();
    }

    public Completable updateBrightnessLevel(int progress, Device device) {
        return (device instanceof DeviceMusic) ?
                devicesDataSource.getMusicRepository().setBrightnessLevel(progress, device.getId()) :
                (device instanceof DeviceSolid) ?
                        devicesDataSource.getSolidRepository().setBrightnessLevel(progress, device.getId()) :
                        (device instanceof DeviceWaves) ?
                                devicesDataSource.getWavesRepository().setBrightnessLevel(progress, device.getId()) : Completable.error(() -> new Throwable("Cannot Update Brightness With Unknown Device"));
    }

    public void addDisposable(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public Observable<List<DeviceMusic>> getAllDeviceMusic() {
        return devicesDataSource.getMusicRepository().getAllDevices()
                .map(deviceMusics -> {
                    Collections.sort(deviceMusics, (o1, o2) -> (int) (o2.getId() - o1.getId()));
                    return deviceMusics;
                });
    }

    public Observable<List<DeviceSolid>> getAllDeviceSolid() {
        return devicesDataSource.getSolidRepository().getAllDevices()
                .map(deviceSolid -> {
                    Collections.sort(deviceSolid, (o1, o2) -> (int) (o2.getId() - o1.getId()));
                    return deviceSolid;
                });

    }

    public Observable<List<DeviceWaves>> getAllDeviceWaves() {
        return devicesDataSource.getWavesRepository().getAllDevices()
                .map(deviceWaves -> {
                    Collections.sort(deviceWaves, (o1, o2) -> (int) (o2.getId() - o1.getId()));
                    return deviceWaves;
                });

    }

    public Completable deleteDevice(Device device) {
        return (device instanceof DeviceMusic) ?
                devicesDataSource.getMusicRepository().deleteDevice((DeviceMusic) device) :
                (device instanceof DeviceSolid) ?
                        devicesDataSource.getSolidRepository().deleteDevice((DeviceSolid) device) :
                        (device instanceof DeviceWaves) ?
                                devicesDataSource.getWavesRepository().deleteDevice((DeviceWaves) device) : Completable.error(() -> new Throwable("Cannot Delete With Unknown Device"));
    }

    public Single<Long> insertDevice(Device device) {
        return (device instanceof DeviceMusic) ?
                devicesDataSource.getMusicRepository().insertDevice((DeviceMusic) device) :
                (device instanceof DeviceSolid) ?
                        devicesDataSource.getSolidRepository().insertDevice((DeviceSolid) device) :
                        (device instanceof DeviceWaves) ?
                                devicesDataSource.getWavesRepository().insertDevice((DeviceWaves) device) : Single.error(() -> new Throwable("Cannot Insert With Unknown Device"));

    }

    public Completable updateDevice(Device device) {
        if (device instanceof DeviceMusic) {
            return devicesDataSource.getMusicRepository().updateDevice((DeviceMusic) device);
        } else if (device instanceof DeviceSolid)
            return devicesDataSource.getSolidRepository().updateDevice((DeviceSolid) device);
        else if (device instanceof DeviceWaves)
            return devicesDataSource.getWavesRepository().updateDevice((DeviceWaves) device);
        else
            return Completable.error(() -> new Throwable("Cannot Update With Unknown Device"));
    }

    public Completable insertId(Device device) {
        return (device instanceof DeviceMusic) ?
                devicesDataSource.getMusicRepository().insertId(((DeviceMusic) device).getDeviceId()) :
                (device instanceof DeviceSolid) ?
                        devicesDataSource.getSolidRepository().insertId(((DeviceSolid) device).getDeviceId()) :
                        (device instanceof DeviceWaves) ?
                                devicesDataSource.getWavesRepository().insertId(((DeviceWaves) device).getDeviceId()) : Completable.error(() -> new Throwable("Cannot Insert Id From Unknown Device"));
    }
}
