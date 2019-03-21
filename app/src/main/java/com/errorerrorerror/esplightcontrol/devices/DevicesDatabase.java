package com.errorerrorerror.esplightcontrol.devices;

import androidx.room.Database;
import androidx.room.RoomDatabase;


/**
 * Singleton pattern
 */
@Database(entities = {Devices.class}, version = 3)
public abstract class DevicesDatabase extends RoomDatabase {

    public abstract DevicesDao getDevicesDao();

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