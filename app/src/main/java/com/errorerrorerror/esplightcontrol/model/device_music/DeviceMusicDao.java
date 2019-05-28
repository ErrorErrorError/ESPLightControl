package com.errorerrorerror.esplightcontrol.model.device_music;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface DeviceMusicDao {

    @Query("SELECT * FROM device_music")
    Observable<List<DeviceMusic>> getAllMusicDevices();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(DeviceMusic deviceMusic);

    @Update
    Completable update(DeviceMusic deviceMusic);


    @Delete
    Completable delete(DeviceMusic deviceMusic);

    @Query("UPDATE device_music SET device_on = :bool WHERE id = :id")
    Completable updateSwitch(Boolean bool, long id);

    @Query("UPDATE device_music SET brightness_level = :progress WHERE id = :id")
    Completable updateBrightnessLevel(int progress, long id);

    @Query("SELECT * FROM  device_music WHERE id =:id")
    Single<DeviceMusic> getDevice(long id);
}
