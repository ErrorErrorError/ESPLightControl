package com.errorerrorerror.esplightcontrol.model.device_solid;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.errorerrorerror.esplightcontrol.model.device.Device;

import java.util.Objects;

@Entity(tableName = "device_solid")
public class DeviceSolid extends Device {

    @ColumnInfo(name = "color")
    private int color;

    public DeviceSolid() {
    }

    public DeviceSolid(@NonNull Device device, int color) {
        super(device);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    @NonNull
    @Override
    public String toString() {
        return "DeviceSolid{" +
                "color=" + color +
                "} " + super.toString();
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
