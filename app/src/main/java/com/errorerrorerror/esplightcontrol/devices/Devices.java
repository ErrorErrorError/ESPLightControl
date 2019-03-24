package com.errorerrorerror.esplightcontrol.devices;


import androidx.annotation.NonNull;
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
    private String device;

    @ColumnInfo(name = "device_ip")
    private String ip;

    @ColumnInfo(name = "device_port")
    private String port;

    @ColumnInfo(name = "device_connectivity")
    private String connectivity;

    @ColumnInfo(name = "device_on")
    private Boolean on;

    @Ignore
    public Devices() {
    }

    public Devices(String device, String ip, String port, String connectivity, Boolean on) {
        this.device = device;
        this.ip = ip;
        this.port = port;
        this.connectivity = connectivity;
        this.on = on;
    }

    //Setters and


    public Boolean isOn() {
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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
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

    @NonNull
    @Override
    public String toString() {
        return "Devices{" +
                "id=" + id +
                ", device='" + device + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", connectivity='" + connectivity + '\'' +
                ", on=" + on +
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

        return this.getDevice().equals(devices.getDevice()) &&
                this.getIp().equals(devices.getIp()) &&
                this.getPort().equals(devices.getPort())&&
                this.getConnectivity().equals(devices.getConnectivity());
    }

    @Override
    public int hashCode() {
        int mult = 31;
        int hash = 17;
        hash = mult * (hash + this.getDevice().hashCode());
        hash = mult * (hash + this.getIp().hashCode());
        hash = mult * (hash + this.getPort().hashCode());
        hash = mult * (hash + this.getConnectivity().hashCode());
        return hash;
    }

}
