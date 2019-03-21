package com.errorerrorerror.esplightcontrol.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.errorerrorerror.esplightcontrol.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.errorerrorerror.esplightcontrol.EspApp.CHANNEL_ID;

public class NetworkService extends Service {

    private static final String TAG = "NetworkServices";
    private int count = 0;
    List<Thread> threadList = new ArrayList<>();


    public NetworkService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("TEstService")
                    .setContentText("Uhhhhhh")
                    .setSmallIcon(R.drawable.ic_microcontroller_icon)
                    .build();
            startForeground(1, notification);
        }


    }

    @Override
    public int onStartCommand(@NotNull Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
