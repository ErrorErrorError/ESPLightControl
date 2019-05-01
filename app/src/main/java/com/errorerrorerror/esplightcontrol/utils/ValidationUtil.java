package com.errorerrorerror.esplightcontrol.utils;

import com.errorerrorerror.esplightcontrol.databinding.DialogFragmentDevicesBinding;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;


public class ValidationUtil {

    //private static final String TAG = "ValidationUtil";
    private List<Devices> devicesList;
    private int colorAccent;

    private static final String UNUSED_IP = "Enter An Unused IP Address";
    private static final String UNUSED_NAME = "Enter An Unused Device Name";
    private static final String INVALID_NAME = "Enter A Valid Device Name";
    private static final String INVALID_IP = "Enter A Valid IP Address";
    private static final String INVALID_PORT = "Enter A Valid Port";

    //Port Regex
    private static final String portRegex = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

    //Ipv4 regex
    private final String ipV4Regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    //IpV6 regex
    private final String ipV6Regex = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";

    public ValidationUtil() {
    }

    /*
        public ValidationUtil(List<Devices> devicesList, int colorAccent) {
            this.devicesList = devicesList;
            this.colorAccent = colorAccent;
        }
    */
    public void updateDeviceList(List<Devices> devicesList) {
        this.devicesList = devicesList;
    }

    public void setColorError(int colorAccent){
        this.colorAccent = colorAccent;
    }

    //Testing User Input Device name Add
    private boolean deviceNameValidationAdd(String name, TextInputLayout textInputLayoutName) //Validates name and checks if the name is already used
    {
        boolean isValid = false;
        boolean isRepeated = false;

        if (!name.isEmpty()) {
            isValid = true;

            for (int i = 0; i < devicesList.size(); i++) {
                String deviceMatch = devicesList.get(i).getDeviceName();

                if (name.equals(deviceMatch)) {
                    isRepeated = true;
                    break;
                }
            }
        }

        return setColors(textInputLayoutName, isValid, isRepeated, 1);
    }

    //Testing User Input Device ip Add
    private boolean ipAddressValidationAdd(String ip, TextInputLayout textInputLayoutIP) //Validates ipAddress as either ipV4 or ipV6
    {
        boolean ipValid = false;
        boolean ipRepeated = false;

        if (ip.matches(ipV4Regex) || ip.matches(ipV6Regex)) {
            ipValid = true;
        }

        if (ipValid) {
            for (int i = 0; i < devicesList.size(); i++) {
                String ipMatch = devicesList.get(i).getIp();

                if (ip.equals(ipMatch)) {
                    ipRepeated = true;
                    break;
                }
            }
        }

        return setColors(textInputLayoutIP, ipValid, ipRepeated, 2);
    }

    //Testing User Input Device port Add
    private boolean portValidationAdd(String port, TextInputLayout textInputLayoutPort) //validates port
    {
        return setColors(textInputLayoutPort, port.matches(portRegex), false, 3);
    }

    //Testing User Input Device name Edit
    private boolean deviceNameValidationEdit(String name, long id, TextInputLayout textInputLayoutName) //Validates name and checks if the name is already used
    {
        boolean isValid = false;
        boolean isRepeated = false;

        if (!name.isEmpty()) {
            isValid = true;

            for (int i = 0; i < devicesList.size(); i++) {
                if (devicesList.get(i).getId() != id) {
                    String deviceMatch = devicesList.get(i).getDeviceName();
                    if (name.equals(deviceMatch)) {
                        isRepeated = true;
                        break;
                    }
                }
            }
        }

        return setColors(textInputLayoutName, isValid, isRepeated, 1);

    }

    //Testing User Input Device ip Edit
    private boolean ipAddressValidationEdit(String ip, long id, TextInputLayout textInputLayoutIP) //Validates ipAddress as either ipV4 or ipV6
    {
        boolean ipValid = false;
        boolean ipRepeated = false;


        if (ip.matches(ipV4Regex) || ip.matches(ipV6Regex)) {
            ipValid = true;
        }
        if (ipValid) {
            for (int i = 0; i < devicesList.size(); i++) {
                if (devicesList.get(i).getId() != id) {
                    String ipMatch = devicesList.get(i).getIp();
                    if (ip.equals(ipMatch)) {
                        ipRepeated = true;
                        break;
                    }
                }
            }
        }

        return setColors(textInputLayoutIP, ipValid, ipRepeated, 2);
    }

    public boolean testAllAdd(DialogFragmentDevicesBinding devicesBinding) //tests if the input is valid
    {
        boolean nameTest = deviceNameValidationAdd(Objects.requireNonNull(devicesBinding.deviceName.getText()).toString(), devicesBinding.deviceNameTextLayout);
        boolean ipTest = ipAddressValidationAdd(Objects.requireNonNull(devicesBinding.IPAddressInput.getText()).toString(), devicesBinding.ipAddressTextLayout);
        boolean portTest = portValidationAdd(Objects.requireNonNull(devicesBinding.portInput.getText()).toString(), devicesBinding.portTextLayout);

        return nameTest && ipTest && portTest;
    }

    //Tests edit
    public boolean testAllEdit(DialogFragmentDevicesBinding devicesBinding, long id) //tests if the input is valid
    {
        boolean nameTest = deviceNameValidationEdit(Objects.requireNonNull(devicesBinding.deviceName.getText()).toString(), id, devicesBinding.deviceNameTextLayout);
        boolean ipTest = ipAddressValidationEdit(Objects.requireNonNull(devicesBinding.IPAddressInput.getText()).toString(), id, devicesBinding.ipAddressTextLayout);
        boolean portTest = portValidationAdd(Objects.requireNonNull(devicesBinding.portInput.getText()).toString(), devicesBinding.portTextLayout);

        return nameTest && ipTest && portTest;
    }


    private boolean setColors(TextInputLayout test, boolean valid, boolean repeated, int mode) {
        test.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        if (valid && !repeated) {
            if (test.isErrorEnabled()) {
                test.setErrorEnabled(false);
            }

            if (test.isEndIconVisible()) {
                test.setEndIconVisible(false);
            }

        } else if (repeated) {
            if (mode == 1) {
                test.setErrorEnabled(true);
                test.setError(UNUSED_NAME);
            } else if (mode == 2) {
                test.setErrorEnabled(true);
                test.setError(UNUSED_IP);
            }
        } else {
            if (mode == 1) {
                test.setErrorEnabled(true);
                test.setError(INVALID_NAME);
            } else if (mode == 2) {
                test.setErrorEnabled(true);
                test.setError(INVALID_IP);
            } else if (mode == 3) {
                test.setErrorEnabled(true);
                test.setError(INVALID_PORT);
            }

        }

        return valid && !repeated;
    }
}
