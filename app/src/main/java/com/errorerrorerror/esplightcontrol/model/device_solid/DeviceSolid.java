package com.errorerrorerror.esplightcontrol.model.device_solid;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.errorerrorerror.esplightcontrol.model.device.Device;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity(tableName = "device_solid")
public class DeviceSolid extends Device {

    @PrimaryKey
    private long deviceId;

    @ColumnInfo(name = "color")
    private int color;

    public DeviceSolid() {
    }

    public DeviceSolid(@NonNull Device device, int color) {
        super(device);
        this.color = color;
    }

    public DeviceSolid(@NonNull Device device, long deviceId, int color) {
        super(device);
        this.deviceId = deviceId;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    @NotNull
    @Override
    public String toString() {
        return "DeviceSolid{" +
                "color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceSolid)) return false;
        if (!super.equals(o)) return false;
        DeviceSolid that = (DeviceSolid) o;
        return getColor() == that.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getColor());
    }
}
