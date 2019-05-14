package com.errorerrorerror.esplightcontrol.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusic;


/**
 * Singleton pattern
 */
@Database(entities = {Device.class, DeviceMusic.class}, version = 1)
public abstract class DevicesDatabase extends RoomDatabase {

    public abstract DevicesDao getDevicesDao();

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