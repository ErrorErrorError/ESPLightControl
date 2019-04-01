package com.errorerrorerror.esplightcontrol;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.errorerrorerror.esplightcontrol.devices.DevicesRepository;
import com.errorerrorerror.esplightcontrol.di.AppModule;
import com.errorerrorerror.esplightcontrol.di.RoomModule;
import com.errorerrorerror.esplightcontrol.di.component.ApplicationComponent;
import com.errorerrorerror.esplightcontrol.di.component.DaggerApplicationComponent;

import javax.inject.Inject;

public class EspApp extends Application {

    public static final String CHANNEL_ID = "networkServiceChannel";
    @Inject
    public DevicesRepository devicesRepository;
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        dagger();
    }

    void dagger() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Testing service Esp",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setSound(null, null);
            serviceChannel.enableVibration(false);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
