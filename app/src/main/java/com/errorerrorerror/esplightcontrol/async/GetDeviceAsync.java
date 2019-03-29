package com.errorerrorerror.esplightcontrol.async;

import android.os.AsyncTask;

import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.devices.DevicesDao;

public class GetDeviceAsync extends AsyncTask<Long, Void, Devices> {

    private final DevicesDao devicesDao;

    public GetDeviceAsync(DevicesDao dao) {
        devicesDao = dao;
    }

    @Override
    protected Devices doInBackground(Long... longs) {
        return devicesDao.getDevicesWithId(longs[0]);
    }
}
