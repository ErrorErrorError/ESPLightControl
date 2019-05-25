package com.errorerrorerror.esplightcontrol.model.device_waves;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.errorerrorerror.esplightcontrol.model.device.Device;

import java.util.Objects;

@Entity(tableName = "device_waves")
public class DeviceWaves extends Device {

    @PrimaryKey
    private long deviceId;

    @ColumnInfo(name = "speed")
    private int speed;

    @ColumnInfo(name = "primaryColor")
    private int primaryColor;

    public DeviceWaves() {
    }

    public DeviceWaves(@NonNull Device device,
                       int speed,
                       @ColorInt int primaryColor) {
        super(device);
        this.primaryColor = primaryColor;
        this.speed = speed;
    }

    public DeviceWaves(long id,
                       @NonNull Device device,
                       int speed,
                       @ColorInt int primaryColor) {
        super(device);
        this.deviceId = id;
        this.primaryColor = primaryColor;
        this.speed = speed;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
        this.setId(deviceId);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int color) {
        this.primaryColor = color;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeviceWaves{" +
                "deviceId=" + deviceId +
                ", speed=" + speed +
                ", primaryColor=" + primaryColor +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceWaves)) return false;
        if (!super.equals(o)) return false;
        DeviceWaves that = (DeviceWaves) o;
        return getSpeed() == that.getSpeed() &&
                getPrimaryColor() == that.getPrimaryColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSpeed(), getPrimaryColor());
    }
}
