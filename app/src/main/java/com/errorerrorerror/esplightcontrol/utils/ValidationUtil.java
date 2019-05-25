package com.errorerrorerror.esplightcontrol.utils;

import androidx.annotation.NonNull;

import com.errorerrorerror.esplightcontrol.model.device.Device;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import io.reactivex.Observable;

public class ValidationUtil {

    public static final String USED_IP = "Enter An Unused IP Address";
    public static final String USED_NAME = "Enter An Unused Device Name";
    public static final String INVALID_NAME = "Enter A Valid Device Name";
    public static final String INVALID_IP = "Enter A Valid IP Address";
    public static final String INVALID_PORT = "Enter A Valid Port";

    //Port Regex
    private static final String portRegex = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

    //Ipv4 regex
    private static final String ipV4Regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    //IpV6 regex
    private static final String ipV6Regex = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";

    public static boolean nameRepeated(List<Device> deviceList, String name, long exceptThis) {
        name = name.trim();
        for (Device device : deviceList) {
            if(device.getId() != exceptThis) {
                if (device.getDeviceName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean nameValid(String name) {
        return !name.isEmpty();
    }

    public static boolean ipRepeated(List<Device> deviceList, String ip, long exceptThis) {
        for (Device device : deviceList) {
            if (device.getId() != exceptThis) {
                if (device.getIp().equals(ip)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean ipValid(String ip) {
        return ip.matches(ipV4Regex) || ip.matches(ipV6Regex);
    }

    public static boolean portValid(String port) {
        return port.matches(portRegex);
    }


    public static Observable<Boolean> isAllValid(Observable<Boolean> name, Observable<Boolean> ip, Observable<Boolean> port) {
        return Observable.combineLatest(name, ip, port, (aBoolean, aBoolean2, aBoolean3) -> aBoolean && aBoolean2 && aBoolean3);
    }

    public static boolean validation(boolean valid, boolean repeated, @NonNull TextInputLayout input, String used, String invalid) {
        if (valid && !repeated) {
            input.setErrorEnabled(false);
            return true;
        } else if (repeated) {
            input.setError(used);
            return false;
        } else {
            input.setError(invalid);
            return false;
        }
    }

}