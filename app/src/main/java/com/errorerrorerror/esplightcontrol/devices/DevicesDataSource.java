package com.errorerrorerror.esplightcontrol.devices;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class DevicesDataSource implements DevicesRepository {

    private final DevicesDao devicesDao;

    @Inject
    public DevicesDataSource(DevicesDao devicesDao) {
        this.devicesDao = devicesDao;
    }


    @Override
    public void deleteDevice(Devices devices) {
        devicesDao.delete(devices);
    }

    @Override
    public Flowable<List<Devices>> getAllDevices() {
        return devicesDao.getAllDevices();
    }

    @Override
    public void setConnectivity(String connectivity, long id) {
        devicesDao.updateConnectivity(connectivity, id);
    }

    @Override
    public void setSwitch(Boolean bool, long id) {
            devicesDao.updateSwitch(bool, id);
    }

    @Override
    public Single<Devices> getDevice(long id) {
        return devicesDao.getDeviceWithId(id);
    }

    @Override
    public void insertEditDevice(Devices device) {
        devicesDao.insertEditDevice(device);
    }

    @Override
    public void setBrightnessLevel(int progress, long id) {
        devicesDao.updateBrightnessLevel(progress, id);
    }

}
