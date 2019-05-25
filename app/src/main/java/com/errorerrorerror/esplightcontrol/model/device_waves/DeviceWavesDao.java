package com.errorerrorerror.esplightcontrol.model.device_waves;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface DeviceWavesDao {

    @Query("SELECT * FROM device_waves")
    Observable<List<DeviceWaves>> getAllWaveDevices();

    @Insert
    Single<Long> insert(DeviceWaves deviceWaves);

    @Update
    Completable update(DeviceWaves deviceWaves);

    @Delete
    Completable delete(DeviceWaves deviceWaves);

    @Query("UPDATE device_waves SET device_on = :bool WHERE id = :id")
    Completable updateSwitch(Boolean bool, long id);

    @Query("UPDATE device_waves SET brightness_level = :progress WHERE id = :id")
    Completable updateBrightnessLevel(int progress, long id);

    @Query("SELECT * FROM  device_waves WHERE id =:id")
    Single<DeviceWaves> getDevice(long id);

    @Query("UPDATE device_music SET id = :id WHERE deviceId = :id")
    Completable insertId(long id);


}
