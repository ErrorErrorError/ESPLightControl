package com.errorerrorerror.esplightcontrol.model.device;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.util.Objects;

public class Device {

    @PrimaryKey(autoGenerate = true)
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

    public Device() {
    }

    public Device(Device device) {
        this.id = device.getId();
        this.deviceName = device.getDeviceName();
        this.ip = device.getIp();
        this.port = device.getPort();
        this.connectivity = device.getConnectivity();
        this.on = device.isOn();
        this.brightness = device.getBrightness();
    }

    public Device(String deviceName, String ip, String port, String connectivity, Boolean on, int brightness) {
        this.deviceName = deviceName;
        this.ip = ip;
        this.port = port;
        this.connectivity = connectivity;
        this.on = on;
        this.brightness = brightness;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //Setters and getters
    public Boolean isOn() {
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

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    @NonNull
    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", deviceName='" + deviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", connectivity='" + connectivity + '\'' +
                ", on=" + on +
                ", brightness=" + brightness +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return getId() == device.getId() &&
                getBrightness() == device.getBrightness() &&
                Objects.equals(getDeviceName(), device.getDeviceName()) &&
                Objects.equals(getIp(), device.getIp()) &&
                Objects.equals(getPort(), device.getPort()) &&
                Objects.equals(getConnectivity(), device.getConnectivity()) &&
                Objects.equals(on, device.on);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDeviceName(), getIp(), getPort(), getConnectivity(), on, getBrightness());
    }
}
