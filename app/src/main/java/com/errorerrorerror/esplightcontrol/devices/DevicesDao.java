package com.errorerrorerror.esplightcontrol.devices;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DevicesDao {

    @Insert
    void insert(Devices... devices);


    @Query("SELECT * FROM devices")
    LiveData<List<Devices>> getAllDevices();

    @Delete
    void delete(Devices... device);


    @Update
    void update(Devices... device);


    @Query("UPDATE devices SET device_connectivity = :connectivity WHERE id = :id")
    void updateConnectivity(String connectivity, long id);


    @Query("UPDATE devices SET device_on = :bool WHERE id = :id")
    void updateSwitch(Boolean bool, long id);


    @Query("SELECT * FROM devices WHERE id = :id")
    Devices getDevicesWithId(long id);

/*
    @Query("SELECT * FROM devices AT conn")
*/
}
