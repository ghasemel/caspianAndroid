package ir.caspiansoftware.caspianandroidapp;

import ir.caspiansoftware.caspianandroidapp.BusinessLayer.InitialSettingBLL;

/**
 * Created by Canada on 8/14/2016.
 */
public class SettingWebService {

    public static final int TIME_OUT = 5 * 60 * 1000; // in millisecond
    public static final String AUTHENTICATION_SCHEME = "CaspianAPI";
    private static final String IIS_APP_NAME = "CaspianService";
    private static final char DEVICE_USER_DELIMITER = '>';

    private SettingWebService() { }

    public static String getIP() {
        return InitialSettingBLL.getIP();
    }

    public static String getApiKey() {
        return InitialSettingBLL.getApiKey();
    }

    public static String getDeviceId() {
        String userId = "";
        if (Vars.USER != null)
            userId = "" + DEVICE_USER_DELIMITER + Vars.USER.getUserId();

        String deviceId = InitialSettingBLL.getDeviceId();
        return deviceId == null ? "" : deviceId + userId;
    }

    private static int getPort() {
        return InitialSettingBLL.getPort();
    }

    public static String getPortString() {
        if (getPort() == -1)
            return "";
        return String.valueOf(getPort());
    }

    public static String getAPI_URL() {
        return InitialSettingBLL.getAPI_URL(IIS_APP_NAME);
    }

    public static String getImageURL() {
        return InitialSettingBLL.getBaseURL(IIS_APP_NAME) + "images/";
    }
}
