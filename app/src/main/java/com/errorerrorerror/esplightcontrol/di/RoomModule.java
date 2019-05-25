package com.errorerrorerror.esplightcontrol.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.errorerrorerror.esplightcontrol.model.DevicesDatabase;
import com.errorerrorerror.esplightcontrol.model.MainRepository;
import com.errorerrorerror.esplightcontrol.model.device_music.DeviceMusicDao;
import com.errorerrorerror.esplightcontrol.model.device_music.MusicRepository;
import com.errorerrorerror.esplightcontrol.model.device_solid.DeviceSolidDao;
import com.errorerrorerror.esplightcontrol.model.device_solid.SolidRepository;
import com.errorerrorerror.esplightcontrol.model.device_waves.DeviceWavesDao;
import com.errorerrorerror.esplightcontrol.model.device_waves.WavesRepository;
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
                .fallbackToDestructiveMigration()
                .fallbackToDestructiveMigrationOnDowngrade()
                .build();
    }

    @Singleton
    @Provides
    DevicesDatabase providesDevicesDatabase() {
        return devicesDatabase;
    }

    @Singleton
    @Provides
    DeviceMusicDao providesDevicesMusicDao(@NonNull DevicesDatabase devicesDatabase) {
        return devicesDatabase.getMusicDao();
    }

    @Singleton
    @Provides
    DeviceSolidDao providesDevicesSolidDao(@NonNull DevicesDatabase devicesDatabase) {
        return devicesDatabase.getSolidDao();
    }

    @Singleton
    @Provides
    DeviceWavesDao providesDevicesWavesDao(@NonNull DevicesDatabase devicesDatabase) {
        return devicesDatabase.getWavesDao();
    }

    @NonNull
    @Singleton
    @Provides
    WavesRepository providesWaveRepository(DeviceWavesDao deviceWavesDao) {
        return new WavesRepository(deviceWavesDao);
    }

    @NonNull
    @Singleton
    @Provides
    SolidRepository providesSolidRepository(DeviceSolidDao deviceSolidDao) {
        return new SolidRepository(deviceSolidDao);
    }

    @NonNull
    @Singleton
    @Provides
    MusicRepository providesMusicRepository(DeviceMusicDao deviceMusicDao) {
        return new MusicRepository(deviceMusicDao);
    }

    @NonNull
    @Singleton
    @Provides
    MainRepository providesMainRepository(WavesRepository waves, SolidRepository solid, MusicRepository music) {
        return new MainRepository(music, solid, waves);
    }

    @NonNull
    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(MainRepository mainRepository) {
        return new DevicesViewModelFactory(mainRepository);
    }
}
