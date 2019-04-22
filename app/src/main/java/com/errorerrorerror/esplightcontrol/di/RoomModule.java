package com.errorerrorerror.esplightcontrol.di;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.errorerrorerror.esplightcontrol.devices.DevicesDao;
import com.errorerrorerror.esplightcontrol.devices.DevicesDataSource;
import com.errorerrorerror.esplightcontrol.devices.DevicesDatabase;
import com.errorerrorerror.esplightcontrol.viewmodel.DevicesViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private static final String DATABASE_NAME = "devices_registered";
    private DevicesDatabase devicesDatabase;

    public RoomModule(Application application) {
        devicesDatabase = Room.databaseBuilder(
                application.getApplicationContext(),
                DevicesDatabase.class,
                DATABASE_NAME)
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
