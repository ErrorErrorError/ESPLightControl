package com.errorerrorerror.esplightcontrol.model;

import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;

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
    public void deleteDevice(Device device) {
        devicesDao.delete(device);
    }

    @Override
    public Flowable<List<Device>> getAllDevices() {
        return devicesDao.getAllDevices();
    }

    @Override
    public void setSwitch(Boolean bool, long id) {
            devicesDao.updateSwitch(bool, id);
    }

    @Override
    public Single<Device> getDevice(long id) {
        return devicesDao.getDeviceWithId(id);
    }

    @Override
    public void insertDevice(Device device) {
        devicesDao.insertDevice(device);
    }

    @Override
    public void setBrightnessLevel(int progress, long id) {
        devicesDao.updateBrightnessLevel(progress, id);
    }

    @Override
    public void updateDevice(Device device) {
        devicesDao.updateDevice(device);
    }

    @Override
    public void insertUpdateDeviceMusic(DeviceMusic deviceMusic) {
        devicesDao.insertUpdateMusic(deviceMusic);
    }

    @Override
    public Flowable<List<DeviceMusic>> getAllDeviceMusic() {
        return devicesDao.getAllMusicDevices();
    }
}
