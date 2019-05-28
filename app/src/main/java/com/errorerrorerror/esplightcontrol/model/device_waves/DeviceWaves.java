package com.errorerrorerror.esplightcontrol.model.device_waves;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.errorerrorerror.esplightcontrol.model.device.Device;

import java.util.Objects;

@Entity(tableName = "device_waves")
public class DeviceWaves extends Device {

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
                "speed=" + speed +
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
