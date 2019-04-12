package com.errorerrorerror.esplightcontrol.utils;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;


public class ValidationUtil {

    //private static final String TAG = "ValidationUtil";
    private final List<Devices> devicesList;
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

    public ValidationUtil(List<Devices> devicesList, int colorAccent) {
        this.devicesList = devicesList;
        this.colorAccent = colorAccent;
    }

    //Testing User Input Device name Add
    private boolean deviceNameValidationAdd(String name, TextInputLayout textInputLayoutName) //Validates name and checks if the name is already used
    {
        boolean isValid;
        boolean isRepeated = false;

        if (!name.isEmpty()) {

            isValid = true;

            if (!devicesList.isEmpty()) {
                for (int i = 0; i < devicesList.size(); i++) {
                    String deviceMatch = devicesList.get(i).getDevice();

                    if (!name.equals(deviceMatch)) {
                        isRepeated = false;

                    } else {
                        isRepeated = true;
                        break;
                    }
                }
            }

        } else {
            isValid = false;
        }

        return setColors(textInputLayoutName, isValid, isRepeated, 1);
        //setColors(textInputLayoutName, );
/*
        if (isValid && !isRepeated) {
            setColors(textInputLayoutName, true);

            return true;

        } else if (isValid & isRepeated) {
            setColors(textInputLayoutName, false);
            textInputLayoutName.setError("Enter An Unused Device Name");

            return false;

        } else {
            setColors(textInputLayoutName, false);
            textInputLayoutName.setError("Enter A Valid Device Name");

            return false;
        }
        */
    }

    //Testing User Input Device ip Add
    private boolean ipAddressValidationAdd(String ip, TextInputLayout textInputLayoutIP) //Validates ipAddress as either ipV4 or ipV6
    {
        boolean ipValid;
        boolean ipRepeated = false;

        if (ip.matches(ipV4Regex)) {
            ipValid = true;
        } else ipValid = ip.matches(ipV6Regex);

        if (ipValid) {
            for (int i = 0; i < devicesList.size(); i++) {
                String ipMatch = devicesList.get(i).getIp();

                if (ipMatch.isEmpty()) {
                    break;
                } else {
                    if (ip.equals(ipMatch)) {
                        ipRepeated = true;
                        break;
                    }
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
        boolean isValid;
        boolean isRepeated = false;

        if (!name.isEmpty()) {

            isValid = true;

            if (!devicesList.isEmpty()) {
                for (int i = 0; i < devicesList.size(); i++) {

                    if (devicesList.get(i).getId() != id) {
                        String deviceMatch = devicesList.get(i).getDevice();
                        if (!name.equals(deviceMatch)) {
                            isRepeated = false;

                        } else {
                            isRepeated = true;
                            break;
                        }
                    }
                }
            }

        } else {
            isValid = false;
        }

        return setColors(textInputLayoutName, isValid, isRepeated, 1);

    }

    //Testing User Input Device ip Edit
    private boolean ipAddressValidationEdit(String ip, long id, TextInputLayout textInputLayoutIP) //Validates ipAddress as either ipV4 or ipV6
    {
        boolean ipValid;
        boolean ipRepeated = false;


        if (ip.matches(ipV4Regex)) {
            ipValid = true;
        } else ipValid = ip.matches(ipV6Regex);

        if (ipValid) {
            for (int i = 0; i < devicesList.size(); i++) {
                if (devicesList.get(i).getId() != id) {
                    String ipMatch = devicesList.get(i).getIp();
                    if (ipMatch.isEmpty()) {
                        break;
                    } else {
                        if (ip.equals(ipMatch)) {
                            ipRepeated = true;
                            break;
                        }
                    }
                }

            }
        }

        return setColors(textInputLayoutIP, ipValid, ipRepeated, 2);
    }

    public boolean testAllAdd(String name, String ip, String port, TextInputLayout textInputLayoutName, TextInputLayout textInputLayoutIP, TextInputLayout textInputLayoutPort) //tests if the input is valid
    {
        boolean testReturn;
        boolean nameTest = deviceNameValidationAdd(name, textInputLayoutName);
        boolean ipTest = ipAddressValidationAdd(ip, textInputLayoutIP);
        boolean portTest = portValidationAdd(port, textInputLayoutPort);
        testReturn = nameTest && ipTest && portTest;

        return testReturn;
    }

    //Tests edit
    public boolean testAllEdit(String name, String ip, String port, long id, TextInputLayout textInputLayoutName, TextInputLayout textInputLayoutIP, TextInputLayout textInputLayoutPort) //tests if the input is valid
    {
        boolean testReturn;
        boolean nameTest = deviceNameValidationEdit(name, id, textInputLayoutName);
        boolean ipTest = ipAddressValidationEdit(ip, id, textInputLayoutIP);
        boolean portTest = portValidationAdd(port, textInputLayoutPort);
        testReturn = nameTest && ipTest && portTest;

        return testReturn;
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

            test.setBoxStrokeColor(colorAccent);

        } else if (valid && repeated) {
            if (mode == 1) {
                if (!test.isErrorEnabled()) {
                    test.setErrorEnabled(true);
                    test.setError(UNUSED_NAME);
                }

                if (!test.isEndIconVisible()) {
                    test.setEndIconDrawable(R.drawable.ic_error_black_24dp);
                    test.setEndIconVisible(true);
                    test.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                }
            } else if (mode == 2) {
                if (!test.isErrorEnabled()) {
                    test.setErrorEnabled(true);
                    test.setError(UNUSED_IP);
                }

                if (!test.isEndIconVisible()) {
                    test.setEndIconDrawable(R.drawable.ic_error_black_24dp);
                    test.setEndIconVisible(true);
                    test.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                }
            }
        } else {
            if (mode == 1) {
                if (!test.isErrorEnabled()) {
                    test.setErrorEnabled(true);
                    test.setError(INVALID_NAME);
                }

                if (!test.isEndIconVisible()) {
                    test.setEndIconDrawable(R.drawable.ic_error_black_24dp);
                    test.setEndIconVisible(true);
                    test.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                }
            } else if (mode == 2) {
                if (!test.isErrorEnabled()) {
                    test.setErrorEnabled(true);
                    test.setError(INVALID_IP);
                }

                if (!test.isEndIconVisible()) {
                    test.setEndIconDrawable(R.drawable.ic_error_black_24dp);
                    test.setEndIconVisible(true);
                    test.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                }
            } else if (mode == 3) {
                if (!test.isErrorEnabled()) {
                    test.setErrorEnabled(true);
                    test.setError(INVALID_PORT);
                }

                if (!test.isEndIconVisible()) {
                    test.setEndIconDrawable(R.drawable.ic_error_black_24dp);
                    test.setEndIconVisible(true);
                    test.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                }
            }

        }

        return valid && !repeated;
    }
}
