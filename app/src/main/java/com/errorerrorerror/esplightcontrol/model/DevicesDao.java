package com.errorerrorerror.esplightcontrol.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface DevicesDao {

    @Query("SELECT * FROM Device")
    Flowable<List<Device>> getAllDevices();

    @Delete
    void delete(Device... device);

    @Query("UPDATE Device SET device_connectivity = :connectivity WHERE id = :id")
    void updateConnectivity(String connectivity, long id);


    @Query("UPDATE Device SET device_on = :bool WHERE id = :id")
    void updateSwitch(Boolean bool, long id);


    @Query("SELECT * FROM Device WHERE id = :id")
    Single<Device> getDeviceWithId(long id);

    @Insert
    void insertDevice(Device device);

    @Update()
    void updateDevice(Device device);

    @Query("UPDATE Device SET brightness_level = :progress WHERE id = :id")
    void updateBrightnessLevel(int progress, long id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpdateMusic(DeviceMusic deviceMusic);

    @Delete
    void delete(DeviceMusic deviceMusic);

    @Query("SELECT * FROM device_music")
    Flowable<List<DeviceMusic>> getAllMusicDevices();

}

/*
    @Query("SELECT * FROM Device")
    Flowable<List<Device>> getAllDevices();

    @Delete
    void delete(Device... device);

    @Query("UPDATE Device SET device_connectivity = :connectivity WHERE id = :id")
    void updateConnectivity(String connectivity, long id);


    @Query("UPDATE Device SET device_on = :bool WHERE id = :id")
    void updateSwitch(Boolean bool, long id);


    @Query("SELECT * FROM Device WHERE id = :id")
    Single<Device> getDeviceWithId(long id);

    @Insert
    void insertDevice(Device device);

    @Update()
    void updateDevice(Device device);

    @Query("UPDATE Device SET brightness_level = :progress WHERE id = :id")
    void updateBrightnessLevel(int progress, long id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpdateMusic(DeviceMusic deviceMusic);

    @Delete
    void delete(DeviceMusic deviceMusic);

    @Query("SELECT * FROM device_music")
    Flowable<List<DeviceMusic>> getAllMusicDevices();
 */
