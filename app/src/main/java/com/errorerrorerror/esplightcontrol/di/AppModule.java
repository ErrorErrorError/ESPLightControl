package com.errorerrorerror.esplightcontrol.di;

import com.errorerrorerror.esplightcontrol.EspApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final EspApp application;

    public AppModule(EspApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    EspApp provideEspApplication() {
        return application;
    }
}
