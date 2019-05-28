package com.errorerrorerror.esplightcontrol.model.device_music;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.errorerrorerror.esplightcontrol.model.device.Device;

import java.util.Objects;


@Entity(tableName = "device_music")
public class DeviceMusic extends Device {

    @ColumnInfo(name = "low")
    private int low;

    @ColumnInfo(name = "med")
    private int med;

    @ColumnInfo(name = "high")
    private int high;

    @ColumnInfo(name = "intensity")
    private int intensity;

    public DeviceMusic() {
    }

    public DeviceMusic(@NonNull Device device,
                       int low,
                       int med,
                       int high,
                       int intensity) {
        super(device);
        this.low = low;
        this.med = med;
        this.high = high;
        this.intensity = intensity;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getMed() {
        return med;
    }

    public void setMed(int med) {
        this.med = med;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeviceMusic{" +
                "low=" + low +
                ", med=" + med +
                ", high=" + high +
                ", intensity=" + intensity +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceMusic)) return false;
        if (!super.equals(o)) return false;
        DeviceMusic that = (DeviceMusic) o;
        return getLow() == that.getLow() &&
                getMed() == that.getMed() &&
                getHigh() == that.getHigh() &&
                getIntensity() == that.getIntensity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLow(), getMed(), getHigh(), getIntensity());
    }
}
