package com.errorerrorerror.esplightcontrol.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusicDao;
import com.errorerrorerror.esplightcontrol.model.device_solid.DeviceSolid;
import com.errorerrorerror.esplightcontrol.model.device_solid.DeviceSolidDao;
import com.errorerrorerror.esplightcontrol.model.device_waves.DeviceWaves;
import com.errorerrorerror.esplightcontrol.model.device_waves.DeviceWavesDao;

@Database(entities = {DeviceMusic.class, DeviceWaves.class, DeviceSolid.class}, version = 1)
public abstract class DevicesDatabase extends RoomDatabase {

    public abstract DeviceMusicDao getMusicDao();
    public abstract DeviceWavesDao getWavesDao();
    public abstract DeviceSolidDao getSolidDao();

    /*
    //How to setup Migration

    //P = Previous Version
    //N = New Version

    static final Migration MIGRATION_P_N = new Migration(P,N) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // If we alter alter the table, edit like this:.
         database.execSQL("ALTER TABLE _____ "
                + " ADD COLUMN _____ ___");
    };

    //then add before build
    .addMigrations(OLD MIGRATION, NEW MIGRATION)
     */
}