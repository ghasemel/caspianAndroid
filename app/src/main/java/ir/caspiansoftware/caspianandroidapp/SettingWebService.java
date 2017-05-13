package ir.caspiansoftware.caspianandroidapp;

import ir.caspiansoftware.caspianandroidapp.BusinessLayer.InitialSettingBLL;

/**
 * Created by Canada on 8/14/2016.
 */
public class SettingWebService {

    public static final int TIME_OUT = 15000; // in millisecond
    public static final String AuthenticationScheme = "CaspianAPI";
    private static final String SUB_PATH = "/CaspianService";
    private static final char DEVICE_USER_DELIMITER = '>';
    //public static final String API_IP = "192.168.2.50"; //"192.168.2.50"; //"192.168.130.17";
    //public static final String API_KEY = "A93reRTUJHsCuQSHR+L3GxqOJyDmQpCgps102ciuabc=";
    //public static final String DEVICE_ID = "231asdasd23reda23rwe234";
    //public static final String API_PORT = "5412";

    //public static final String API_URL = "http://" + API_IP + ":" + API_PORT +"/api/";


    public static String getIP() {
        //return "192.168.130.17";
        //return "192.168.2.50";
        //return "84.241.28.43";
        return InitialSettingBLL.getIP();
    }

    public static String getApiKey() {
        //return "mN79bucN+x3qQWzs3zkfWA==";
        return InitialSettingBLL.getApiKey();
    }

    public static String getDeviceId() {
        String userId = "";
        if (Vars.USER != null)
            userId = "" + DEVICE_USER_DELIMITER + Vars.USER.getUserId();

        //return "vi38OpzQBuKPjuJJ8W+JSw==" + userId;
        String deviceId = InitialSettingBLL.getDeviceId();
        return deviceId == null ? "" : deviceId + userId;
    }

    public static int getPort() {
        //return 5412;
        return InitialSettingBLL.getPort();
    }

    public static String getPortString() {
        if (getPort() == -1)
            return "";
        return String.valueOf(getPort());
    }

    public static String getAPI_URL() {
        //return "http://" + getIP() + ":" + getPort() +"/api/";
        return InitialSettingBLL.getAPI_URL(SUB_PATH);
    }
}
