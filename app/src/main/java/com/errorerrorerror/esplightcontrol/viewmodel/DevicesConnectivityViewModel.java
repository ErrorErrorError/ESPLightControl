package com.errorerrorerror.esplightcontrol.viewmodel;

import com.errorerrorerror.esplightcontrol.devices.DevicesDataSource;

public class DevicesConnectivityViewModel extends DisposableViewModel {

    private DevicesDataSource devicesDataSource;

    DevicesConnectivityViewModel(DevicesDataSource devicesDataSource) {
        this.devicesDataSource = devicesDataSource;
    }


    public void setConnectivity(String connectivity, long id) {
        devicesDataSource.setConnectivity(connectivity, id);
    }

    public void setSwitch(Boolean bool, long id) {
        devicesDataSource.setSwitch(bool, id);
    }
}
