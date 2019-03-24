package com.errorerrorerror.esplightcontrol.async;

import android.os.AsyncTask;

import com.errorerrorerror.esplightcontrol.devices.DevicesDao;
import com.errorerrorerror.esplightcontrol.devices.Devices;

public class DeleteDeviceAsync extends AsyncTask<Devices, Void, Void> {

    private final DevicesDao devicesDao;

    public DeleteDeviceAsync(DevicesDao dao) {
        devicesDao = dao;
    }

    @Override
    protected Void doInBackground(Devices... devices) {
        devicesDao.delete(devices[0]);
        return null;
    }
}