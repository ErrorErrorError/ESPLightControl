package com.errorerrorerror.esplightcontrol.model;


import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Singleton pattern
 */

public interface DevicesRepository {

    void deleteDevice(Device device);
    Flowable<List<Device>> getAllDevices();
    void setSwitch(Boolean bool, long id);
    Single<Device> getDevice(long id);
    void insertDevice(Device device);
    void setBrightnessLevel(int progress, long id);
    void updateDevice(Device device);

    void insertUpdateDeviceMusic(DeviceMusic deviceMusic);
    Flowable<List<DeviceMusic>> getAllDeviceMusic();

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