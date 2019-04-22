package com.errorerrorerror.esplightcontrol.devices;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Singleton pattern
 */

public interface DevicesRepository {

    void deleteDevice(Devices devices);
    Flowable<List<Devices>> getAllDevices();
    void setConnectivity(String connectivity, long id);
    void setSwitch(Boolean bool, long id);
    Single<Devices> getDevice(long id);
    void insertEditDevice(Devices device);
    void setBrightnessLevel(int progress, long id);
}