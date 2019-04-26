package com.errorerrorerror.esplightcontrol.devices;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "devices")
public class Devices {

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
    @Ignore
    private boolean open;

    public Devices() {
    }

    @Ignore
    public Devices(long id, String deviceName, String ip, String port, String connectivity, Boolean on) {
        this.id = id;
        this.deviceName = deviceName;
        this.ip = ip;
        this.port = port;
        this.connectivity = connectivity;
        this.on = on;
    }

    public Devices(String deviceName, String ip, String port, String connectivity, Boolean on, boolean opened, int brightness) {
        this.deviceName = deviceName;
        this.ip = ip;
        this.port = port;
        this.connectivity = connectivity;
        this.on = on;
        this.open = opened;
        this.brightness = brightness;
    }

    //Setters and


    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Devices{" +
                "id=" + id +
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

        if (!(obj instanceof Devices))
            return false;

        Devices devices = (Devices) obj;

        return
                this.getDeviceName().equals(devices.getDeviceName()) &&
                        this.getIp().equals(devices.getIp()) &&
                        this.getPort().equals(devices.getPort());
    }

    @Override
    public int hashCode() {
        int mult = 31;
        int hash = 17;
        hash = mult * (hash + this.getDeviceName().hashCode());
        hash = mult * (hash + this.getIp().hashCode());
        hash = mult * (hash + this.getPort().hashCode());
        return hash;
    }

    public String getBrightnessPercentage(){
        return String.valueOf(brightness);
    }

    public void setBrightnessPercentage(String percentage){
        brightness = Integer.valueOf(percentage);
    }
}
