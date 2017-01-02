package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.InitialSettingDataSource;
import ir.caspiansoftware.caspianandroidapp.Models.InitialSettingModel;


/**
 * Created by Canada on 6/17/2016.
 */
public class InitialSettingBLL extends ABusinessLayer {
    private static final String TAG = "InitialSettingBLL";


    private static List<InitialSettingModel> sInitialSettingList = null;
    //private Context mContext;


    private static InitialSettingBLL sSettingBLL = null;
    public static InitialSettingBLL create(Context context) {
        if (sSettingBLL == null) {
            sSettingBLL = new InitialSettingBLL(context);
        }
        return sSettingBLL;
    }


    private InitialSettingBLL(Context context) {
        super(context);
        //mContext = context;
        //sInitialSettingList = loadAllSetting();
    }

    public static List<InitialSettingModel> getSetting() {
        return sInitialSettingList;
    }

    private List<InitialSettingModel> loadAllSetting() {
        InitialSettingDataSource initialSettingDataSource = new InitialSettingDataSource(mContext);
        try {
            initialSettingDataSource.open();
            return initialSettingDataSource.getAll();
        } finally {
            initialSettingDataSource.close();
        }
    }

    private void saveAllSetting(List<InitialSettingModel> settingList) {
        InitialSettingDataSource initialSettingDataSource = new InitialSettingDataSource(mContext);
        try {
            initialSettingDataSource.open();
            for (InitialSettingModel setting: settingList) {
                //setting.setValue(PersianConvert.ConvertDigitsToLatin(setting.getValue()));
                Log.d(TAG, "setting '" + setting.getName() + "' : " + setting.getValue());

                if (initialSettingDataSource.getByName(setting.getName()) == null)
                    initialSettingDataSource.insert(setting);
                else
                    initialSettingDataSource.update(setting);
            }
            loadAllSetting();
        } finally {
            initialSettingDataSource.close();
        }
    }

    // region static
    public static void save() {
        if (sInitialSettingList != null) {
            sSettingBLL.saveAllSetting(sInitialSettingList);
        }
    }

    public static List<InitialSettingModel> load() {
        sInitialSettingList = sSettingBLL.loadAllSetting();
        return sInitialSettingList;
    }

    private static void setSetting(final String name, final String value) {
        if (sInitialSettingList == null) {
            sInitialSettingList = new ArrayList<>();
        }


        InitialSettingModel settingModel = null;
        for (InitialSettingModel setting : sInitialSettingList) {
            if (setting.getName().equals(name)) {
                settingModel = setting;
            }
        }

        // for new setting
        if (settingModel == null) {
            sInitialSettingList.add(new InitialSettingModel(name, value));
        } else {
            // for old setting
            for (InitialSettingModel setting : sInitialSettingList) {
                if (setting.getName().equals(name)) {
                    setting.setValue(value);
                    break;
                }
            }
        }
    }

    public static void setIP(String ip) {
        setSetting("ip", ip);
    }

    public static String getIP() {
        return getValue("ip");
    }


    public static void setApiKey(String apiKey) {
        setSetting("apiKey", apiKey);
    }

    public static String getApiKey() {
        return getValue("apiKey");
    }

    public static void setDeviceId(String appId) {
        setSetting("deviceId", appId);
    }

    public static String getDeviceId() {
        return getValue("deviceId");
    }

    public static void setPort(int port) {
        setSetting("port", String.valueOf(port));
    }


    public static int getPort() {
        if (getValue("port") != null)
            return Integer.parseInt(getValue("port"));
        return -1;
    }

    public static String getAPI_URL(String subPath) {
        return "http://" + getIP() + ":" + getPort() + subPath +"/api/";
    }

    public static int getDBVersion() {
        InitialSettingDataSource initialSettingDataSource =
                new InitialSettingDataSource(sSettingBLL.mContext);

        try {
            initialSettingDataSource.open();
            return initialSettingDataSource.getDbVersion();
        } finally {
            initialSettingDataSource.close();
        }
    }

    public static void setDBVersion(int version) {
        setSetting("dbVersion", String.valueOf(version));
    }

    private static String getValue(String settingName) {
        if (sInitialSettingList != null) {
            for (InitialSettingModel setting : sInitialSettingList) {
                if (setting.getName().equals(settingName)) {
                    return setting.getValue();
                }
            }
        }
        return null;
    }

    public static boolean isDBVersionCorrect(int appDbVersion) {
        Log.d(TAG, "isDBVersionCorrect(): appDbVersion = " + appDbVersion);
        Log.d(TAG, "isDBVersionCorrect(): DbVersion = " + InitialSettingBLL.getDBVersion());

//        if (InitialSettingBLL.getDBVersion() == -1) {
//            setDBVersion(appDbVersion);
//            save();
//        }
        //else if (InitialSettingBLL.getDBVersion() != appDbVersion) {
        if (InitialSettingBLL.getDBVersion() != appDbVersion) {
            return false;
        }
        return true;
    }
    // endregion static
}
