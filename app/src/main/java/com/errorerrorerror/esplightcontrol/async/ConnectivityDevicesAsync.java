package com.errorerrorerror.esplightcontrol.async;

import android.os.AsyncTask;

import com.errorerrorerror.esplightcontrol.devices.DevicesDao;

public class ConnectivityDevicesAsync extends AsyncTask<SetSwitchClass, Void, Void> {

    private final DevicesDao devicesDao;

    public ConnectivityDevicesAsync(DevicesDao dao) {
        devicesDao = dao;
    }

    @Override
    protected Void doInBackground(SetSwitchClass... devices) {
         devicesDao.updateConnectivity(
                 devices[0].getConnectivity(),
                 devices[0].getId());
         return null;
    }
}
