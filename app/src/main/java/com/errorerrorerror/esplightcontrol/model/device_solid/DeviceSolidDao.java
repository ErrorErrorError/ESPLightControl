package com.errorerrorerror.esplightcontrol.model.device_solid;

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
public interface DeviceSolidDao {

    @Query("SELECT * FROM device_solid")
    Observable<List<DeviceSolid>> getAllSolidDevices();

    @Insert
    Single<Long> insert(DeviceSolid deviceSolid);

    @Update
    Completable update(DeviceSolid deviceSolid);


    @Delete
    Completable delete(DeviceSolid deviceSolid);

    @Query("UPDATE device_solid SET device_on = :bool WHERE id = :id")
    Completable updateSwitch(Boolean bool, long id);

    @Query("UPDATE device_solid SET brightness_level = :progress WHERE id = :id")
    Completable updateBrightnessLevel(int progress, long id);

    @Query("SELECT * FROM  device_solid WHERE id =:id")
    Single<DeviceSolid> getDevice(long id);

    @Query("UPDATE device_solid SET id = :id WHERE deviceId = :id")
    Completable insertId(long id);

}
