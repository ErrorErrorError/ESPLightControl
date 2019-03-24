package com.errorerrorerror.esplightcontrol.devices.rx;

import com.errorerrorerror.esplightcontrol.devices.Devices;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface DevicesDaoRx {
    @Insert
    void insert(Devices devices);


    @Query("SELECT * FROM devices")
    Flowable<List<Devices>> getAllDevicesRx();

    @Delete
    void delete(Devices device);

    @Update
    void update(Devices device);


    @Query("UPDATE devices SET device_connectivity = :connectivity WHERE id = :id")
    void updateConnectivity(String connectivity, long id);


    @Query("UPDATE devices SET device_on = :bool WHERE id = :id")
    Completable updateSwitch(Boolean bool, long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrUpdateDevice(Devices devices);
}

