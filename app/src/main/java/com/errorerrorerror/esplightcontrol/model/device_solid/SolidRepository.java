package com.errorerrorerror.esplightcontrol.model.device_solid;

import com.errorerrorerror.esplightcontrol.model.DeviceRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class SolidRepository implements DeviceRepo<DeviceSolid> {

    private DeviceSolidDao dao;

    @Inject
    public SolidRepository(DeviceSolidDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable deleteDevice(DeviceSolid device) {
        return dao.delete(device);
    }

    @Override
    public Observable<List<DeviceSolid>> getAllDevices() {
        return dao.getAllSolidDevices();
    }

    @Override
    public Single<Long> insertDevice(DeviceSolid device) {
        return dao.insert(device);
    }

    @Override
    public Completable updateDevice(DeviceSolid device) {
        return dao.update(device);
    }

    @Override
    public Completable setSwitch(Boolean bool, long id) {
        return dao.updateSwitch(bool, id);
    }

    @Override
    public Completable setBrightnessLevel(int progress, long id) {
        return dao.updateBrightnessLevel(progress, id);
    }

    @Override
    public Single<DeviceSolid> getDevice(long id) {
        return dao.getDevice(id);
    }
}
