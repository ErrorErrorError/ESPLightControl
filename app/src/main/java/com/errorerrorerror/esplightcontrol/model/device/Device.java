package com.errorerrorerror.esplightcontrol.model.device;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import org.jetbrains.annotations.NotNull;

public class Device {

    private long id;

    @ColumnInfo(name = "device_name")
    private String deviceName;

    @ColumnInfo(name = "device_ip")
    private String ip;

    @ColumnInfo(name = "device_port")
    private String port;

    @ColumnInfo(name = "device_connectivity")
    private String connectivity;

    @ColumnInfo(name = "device_on")
    private Boolean on;

    @ColumnInfo(name = "brightness_level")
    private int brightness;

    //This is just for Swiping items
    @Ignore
    private boolean open;

    public Device() {
    }

    @Ignore
    public Device(String deviceName, String ip, String port, String connectivity, Boolean on) {
        this.deviceName = deviceName;
        this.ip = ip;
        this.port = port;
        this.connectivity = connectivity;
        this.on = on;
    }

    public Device(Device device) {
        this.id = device.getId();
        this.deviceName = device.getDeviceName();
        this.ip = device.getIp();
        this.port = device.getPort();
        this.connectivity = device.getConnectivity();
        this.on = device.getOn();
        this.open = device.isOpen();
        this.brightness = device.getBrightness();
    }

    public Device(String deviceName, String ip, String port, String connectivity, Boolean on, boolean opened, int brightness) {
        this.deviceName = deviceName;
        this.ip = ip;
        this.port = port;
        this.connectivity = connectivity;
        this.on = on;
        this.open = opened;
        this.brightness = brightness;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //Setters and getters
    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    @NotNull
    @Override
    public String toString() {
        return "Device{" +
                ", deviceName='" + deviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", connectivity='" + connectivity + '\'' +
                ", on=" + on +
                ", brightness=" + brightness +
                ", open=" + open +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof Device))
            return false;

        Device device = (Device) obj;

        return this.getDeviceName().equals(device.getDeviceName()) &&
                this.getIp().equals(device.getIp()) &&
                this.getPort().equals(device.getPort()) &&
                this.getBrightness() == device.getBrightness();
    }

    @Override
    public int hashCode() {
        int mult = 31;
        int hash = 17;
        hash = mult * (hash + this.getDeviceName().hashCode());
        hash = mult * (hash + this.getIp().hashCode());
        hash = mult * (hash + this.getPort().hashCode());
        hash = mult * (hash + this.getBrightness());
        return hash;
    }
}
