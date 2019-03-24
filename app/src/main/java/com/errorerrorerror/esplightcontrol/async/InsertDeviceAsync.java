package com.errorerrorerror.esplightcontrol.async;

import android.os.AsyncTask;

import com.errorerrorerror.esplightcontrol.devices.DevicesDao;
import com.errorerrorerror.esplightcontrol.devices.Devices;

public class InsertDeviceAsync extends AsyncTask<Devices, Void, Void> {

    private final DevicesDao devicesDao;

    public InsertDeviceAsync(DevicesDao dao) {
        devicesDao = dao;
    }

    @Override
    protected Void doInBackground(Devices... devices) {
        devicesDao.insert(devices[0]);
        return null;
    }
}
