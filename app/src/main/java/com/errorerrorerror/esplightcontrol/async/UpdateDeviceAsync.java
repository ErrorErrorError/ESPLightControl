package com.errorerrorerror.esplightcontrol.async;

import android.os.AsyncTask;

import com.errorerrorerror.esplightcontrol.devices.DevicesDao;
import com.errorerrorerror.esplightcontrol.devices.Devices;

public class UpdateDeviceAsync extends AsyncTask<Devices, Void, Void> {

    private final DevicesDao devicesDao;

    public UpdateDeviceAsync(DevicesDao dao) {
        devicesDao = dao;
    }

    @Override
    protected Void doInBackground(Devices... devices) {
        devicesDao.update(devices[0]);
        return null;
    }
}