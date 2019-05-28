package com.errorerrorerror.esplightcontrol.model.device_music;

import com.errorerrorerror.esplightcontrol.model.DeviceRepo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class MusicRepository implements DeviceRepo<DeviceMusic> {

    private DeviceMusicDao dao;

    @Inject
    public MusicRepository(DeviceMusicDao dao) {
        this.dao = dao;
    }

    @Override
    public Completable deleteDevice(DeviceMusic device) {
        return dao.delete(device);
    }

    @Override
    public Observable<List<DeviceMusic>> getAllDevices() {
        return dao.getAllMusicDevices();
    }

    @Override
    public Single<Long> insertDevice(DeviceMusic device) {
        return dao.insert(device);
    }

    @Override
    public Completable updateDevice(DeviceMusic device) {
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
    public Single<DeviceMusic> getDevice(long id) {
        return dao.getDevice(id);
    }
}
