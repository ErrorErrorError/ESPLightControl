package com.errorerrorerror.esplightcontrol.di;

import android.app.Application;

import com.errorerrorerror.esplightcontrol.devices.DevicesDao;
import com.errorerrorerror.esplightcontrol.devices.DevicesDataSource;
import com.errorerrorerror.esplightcontrol.devices.DevicesDatabase;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesViewModelFactory;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
/*
    //Added new column called 'device_connectivity'
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // If we alter alter the table, edit like this:.
            database.execSQL("ALTER TABLE devices "
                    + " ADD COLUMN device_connectivity TEXT");
        }
    };

    //Adds radio switch
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // If we alter alter the table, edit like this:.
            database.execSQL("ALTER TABLE devices "
                    + " ADD COLUMN device_on INTEGER");
        }
    };

    */
    private static final String DATABASE_NAME = "devices_registered";
    private DevicesDatabase devicesDatabase;

    public RoomModule(Application application) {
        devicesDatabase = Room.databaseBuilder(
                application.getApplicationContext(),
                DevicesDatabase.class,
                DATABASE_NAME)
                //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build();
    }

    @Singleton
    @Provides
    DevicesDatabase providesDevicesDatabase() {
        return devicesDatabase;
    }

    @Singleton
    @Provides
    DevicesDao providesDevicesDao(DevicesDatabase devicesDatabase) {
        return devicesDatabase.getDevicesDao();
    }

    @Singleton
    @Provides
    DevicesDataSource providesDevicesRepository(DevicesDao devicesDao) {
        return new DevicesDataSource(devicesDao);
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(DevicesDataSource devicesDataSource){
        return new DevicesViewModelFactory(devicesDataSource);
    }
}
