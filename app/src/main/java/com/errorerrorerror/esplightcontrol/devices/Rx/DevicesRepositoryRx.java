package com.errorerrorerror.esplightcontrol.devices.Rx;


import com.errorerrorerror.esplightcontrol.devices.Devices;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Singleton pattern
 */

public interface DevicesRepositoryRx {

    void addDevices(Devices devices);
    void updateDevice(Devices devices);
    void deleteDevice(Devices devices);
    Flowable<List<Devices>> getAllDevicesRx();
    void setConnectivity(String connectivity, long id);
    Completable setSwitch(Boolean bool, long id);
    Completable insertOrUpdateDevice(Devices devices);
}