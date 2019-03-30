package com.errorerrorerror.esplightcontrol.utils;

import android.content.Context;
import android.content.res.ColorStateList;

import com.errorerrorerror.esplightcontrol.R;
import com.errorerrorerror.esplightcontrol.devices.Devices;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;


public class ValidationUtil {

    //private static final String TAG = "ValidationUtil";
    private final List<Devices> devicesList;
    private final Context context;

    //Port Regex
    private static final String portRegex = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

    //Ipv4 regex
    private final String ipV4Regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "+([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    //IpV6 regex
    private final String ipV6Regex = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";

    public ValidationUtil(List<Devices> devicesList, Context context) {
        this.devicesList = devicesList;
        this.context = context;
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

        if (ipValid && !ipRepeated) {
            setColors(textInputLayoutIP, true);

            return true;
        } else if (ipValid) {
            setColors(textInputLayoutIP, false);
            textInputLayoutIP.setError("Enter An Unused IP Address");

            return false;
        } else {
            setColors(textInputLayoutIP, false);
            textInputLayoutIP.setError("Enter A Valid IP Address");

            return false;
        }

    }

    //Testing User Input Device port Add
    private boolean portValidationAdd(String port, TextInputLayout textInputLayoutPort) //validates port
    {
        boolean portValid;

        portValid = port.matches(portRegex);

        if (!portValid) {
            setColors(textInputLayoutPort, false);
            textInputLayoutPort.setError("Enter A Valid Port");

            return false;
        } else {
            setColors(textInputLayoutPort, true);

            return true;
        }

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

        if (ipValid && !ipRepeated) {
            setColors(textInputLayoutIP, true);

            return true;
        } else if (ipValid) {
            setColors(textInputLayoutIP, false);
            textInputLayoutIP.setError("Enter An Unused IP Address");

            return false;
        } else {
            setColors(textInputLayoutIP, false);
            textInputLayoutIP.setError("Enter A Valid IP Address");

            return false;
        }
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


    private void setColors(TextInputLayout test, boolean passed) {

        if (passed) {
            test.setErrorEnabled(false);
            test.setEndIconVisible(false);
            test.setBoxStrokeColor(context.getResources().getColor(R.color.colorAccent));
            test.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_NONE);
            test.setDefaultHintTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));

        } else {
            test.setErrorEnabled(true);
            test.setEndIconDrawable(R.drawable.ic_error_black_24dp);
            test.setEndIconTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.redDelete)));
            test.setEndIconVisible(true);
            test.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        }
    }
}
