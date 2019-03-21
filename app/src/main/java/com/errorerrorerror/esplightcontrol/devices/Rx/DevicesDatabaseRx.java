package com.errorerrorerror.esplightcontrol.devices.Rx;

import com.errorerrorerror.esplightcontrol.devices.Devices;

import androidx.room.Database;
import androidx.room.RoomDatabase;


/**
 * Singleton pattern
 */
@Database(entities = {Devices.class}, version = 3)
public abstract class DevicesDatabaseRx extends RoomDatabase {

    public abstract DevicesDaoRx getDevicesDaoRx();

    //How to setup Migration
    /*
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