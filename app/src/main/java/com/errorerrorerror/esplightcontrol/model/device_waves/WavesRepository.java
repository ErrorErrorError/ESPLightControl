package com.errorerrorerror.esplightcontrol.model.device_waves;

import com.errorerrorerror.esplightcontrol.model.DeviceRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class WavesRepository implements DeviceRepo<DeviceWaves> {

    private DeviceWavesDao dao;

    @Inject
    public WavesRepository(DeviceWavesDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable deleteDevice(DeviceWaves device) {
        return dao.delete(device);
    }

    @Override
    public Observable<List<DeviceWaves>> getAllDevices() {
        return dao.getAllWaveDevices();
    }

    @Override
    public Single<Long> insertDevice(DeviceWaves device) {
        return dao.insert(device);
    }

    @Override
    public Completable updateDevice(DeviceWaves device) {
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
    public Single<DeviceWaves> getDevice(long id) {
        return dao.getDevice(id);
    }

    @Override
    public Completable insertId(long id) {
        return dao.insertId(id);
    }
}
