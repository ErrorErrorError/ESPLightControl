package com.errorerrorerror.esplightcontrol.Async;

import android.os.AsyncTask;

import com.errorerrorerror.esplightcontrol.devices.DevicesDao;

public class SwitchDeviceAsync extends AsyncTask<SetSwitchClass, Void, Void> {

    private final DevicesDao devicesDao;


    public SwitchDeviceAsync(DevicesDao dao) {
        devicesDao = dao;
    }


    @Override
    protected Void doInBackground(SetSwitchClass... setSwitchClasses) {
        devicesDao.updateSwitch(setSwitchClasses[0].getBool(),
                setSwitchClasses[0].getId());
        return null;
    }
}
