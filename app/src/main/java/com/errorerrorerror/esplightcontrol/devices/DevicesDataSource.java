package com.errorerrorerror.esplightcontrol.devices;

import com.errorerrorerror.esplightcontrol.Async.DeleteDeviceAsync;
import com.errorerrorerror.esplightcontrol.Async.InsertDeviceAsync;
import com.errorerrorerror.esplightcontrol.Async.SetSwitchClass;
import com.errorerrorerror.esplightcontrol.Async.SwitchDeviceAsync;
import com.errorerrorerror.esplightcontrol.Async.UpdateDeviceAsync;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

public class DevicesDataSource implements DevicesRepository {

    private final DevicesDao devicesDao;

    @Inject
    public DevicesDataSource(DevicesDao devicesDao) {
        this.devicesDao = devicesDao;
    }

    @Override
    public void addDevices(Devices devices) {
        new InsertDeviceAsync(devicesDao).execute(devices);
    }

    @Override
    public void updateDevice(Devices devices) {
        new UpdateDeviceAsync(devicesDao).execute(devices);
    }

    @Override
    public void deleteDevice(Devices devices) {
        new DeleteDeviceAsync(devicesDao).execute(devices);
    }

    @Override
    public void setConnectivity(String connectivity, long id) {
    }

    @Override
    public void setSwitch(Boolean bool, long id) {
        new SwitchDeviceAsync(devicesDao).execute(new SetSwitchClass(bool,id));
    }

    @Override
    public LiveData<List<Devices>> getAllDevices() {
        return this.devicesDao.getAllDevices();
    }

}
