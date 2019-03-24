package com.errorerrorerror.esplightcontrol.devices.rx;

import com.errorerrorerror.esplightcontrol.devices.Devices;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Singleton
public class DevicesDataSourceRx implements DevicesRepositoryRx {

    private final DevicesDaoRx devicesDaoRx;

    @Inject
    public DevicesDataSourceRx(DevicesDaoRx devicesDaoRx) {
        this.devicesDaoRx = devicesDaoRx;
    }


    @Override
    public void addDevices(Devices devices) {
        devicesDaoRx.insert(devices);
    }

    @Override
    public void updateDevice(Devices devices) {
        devicesDaoRx.update(devices);
    }

    @Override
    public void deleteDevice(Devices devices) {
        devicesDaoRx.delete(devices);
    }

    @Override
    public Flowable<List<Devices>> getAllDevicesRx() {
        return devicesDaoRx.getAllDevicesRx();
    }

    @Override
    public void setConnectivity(String connectivity, long id) {

    }

    @Override
    public Completable setSwitch(Boolean bool, long id) {
        return devicesDaoRx.updateSwitch(bool,id);

    }

    @Override
    public Completable insertOrUpdateDevice(Devices devices) {
        return devicesDaoRx.insertOrUpdateDevice(devices);
    }
}
