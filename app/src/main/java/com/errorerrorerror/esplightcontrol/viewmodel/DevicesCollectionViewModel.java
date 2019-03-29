package com.errorerrorerror.esplightcontrol.viewmodel;

import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.errorerrorerror.esplightcontrol.devices.DevicesDataSource;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


//This viewmodel allows for the showed items to be deleted,
// edited, or even add new devices

public class DevicesCollectionViewModel extends ViewModel {

    private DevicesDataSource devicesDataSource;

    DevicesCollectionViewModel(DevicesDataSource devicesDataSource) {
        this.devicesDataSource = devicesDataSource;
    }

    public LiveData<List<Devices>> getAllDevices() {
        return devicesDataSource.getAllDevices();
    }

    public void addDevice(Devices devices) {
        devicesDataSource.addDevices(devices);
    }

    public void deleteDevice(Devices devices) {
        devicesDataSource.deleteDevice(devices);
    }

    public void editDevice(Devices devices) {
        devicesDataSource.updateDevice(devices);
    }

    public void setSwitchConnection(Boolean bool, long id) {
        devicesDataSource.setSwitch(bool, id);
    }

    public Devices getDeviceWithId(long id){
        return devicesDataSource.getDevice(id);
    }

}
