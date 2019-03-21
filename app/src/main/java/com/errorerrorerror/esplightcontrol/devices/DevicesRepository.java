package com.errorerrorerror.esplightcontrol.devices;


import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Singleton pattern
 */

public interface DevicesRepository {

    void addDevices(Devices devices);
    void updateDevice(Devices devices);
    void deleteDevice(Devices devices);
    LiveData<List<Devices>> getAllDevices();
    void setConnectivity(String connectivity, long id);
    void setSwitch(Boolean bool, long id);
}