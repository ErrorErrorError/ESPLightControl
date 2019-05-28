package com.errorerrorerror.esplightcontrol.model;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Singleton pattern
 */

public interface DeviceRepo<T> {

    Completable deleteDevice(T device);
    Observable<List<T>> getAllDevices();
    Single<Long> insertDevice(T device);
    Completable updateDevice(T device);
    Completable setSwitch(Boolean bool, long id);
    Completable setBrightnessLevel(int progress,  long id);
    Single<T> getDevice(long id);
}

/*

    void deleteDevice(Device device);
    Flowable<List<Device>> getAllDevices();
    void setConnectivity(String connectivity, long id);
    void setSwitch(Boolean bool, long id);
    Single<Device> getDevice(long id);
    void insertDevice(Device device);
    void setBrightnessLevel(int progress, long id);
    void updateDevice(Device device);

    void insertUpdateDeviceMusic(DeviceMusic deviceMusic);
    Flowable<List<DeviceMusic>> getAllDeviceMusic();

 */