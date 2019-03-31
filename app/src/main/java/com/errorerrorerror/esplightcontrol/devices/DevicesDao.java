package com.errorerrorerror.esplightcontrol.devices;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface DevicesDao {

    @Query("SELECT * FROM devices")
    Flowable<List<Devices>> getAllDevices();

    @Delete
    void delete(Devices... device);


    @Query("UPDATE devices SET device_connectivity = :connectivity WHERE id = :id")
    void updateConnectivity(String connectivity, long id);


    @Query("UPDATE devices SET device_on = :bool WHERE id = :id")
    void updateSwitch(Boolean bool, long id);


    @Query("SELECT * FROM devices WHERE id = :id")
    Single<Devices> getDeviceWithId(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEditDevice(Devices device);


}
