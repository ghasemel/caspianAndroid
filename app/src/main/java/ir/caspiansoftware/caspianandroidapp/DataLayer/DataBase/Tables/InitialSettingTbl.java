package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/**
 * Created by Canada on 6/4/2016.
 */
public class InitialSettingTbl extends ATableEntity {

    public static final String TABLE_NAME = "initial_setting";
    public static final String COLUMN_SETTING_NAME = "setting_name";
    public static final String COLUMN_SETTING_VALUE = "setting_value";
    //public static final String COLUMN_IP = "ip";
    //public static final String COLUMN_API_KEY = "apiKey";
    //public static final String COLUMN_APP_ID = "appId";
    //public static final String COLUMN_PORT = "port";

    private static InitialSettingTbl sInitialSettingTbl = null;
    public static InitialSettingTbl get() {
        if (sInitialSettingTbl == null) {
            sInitialSettingTbl = new InitialSettingTbl();
        }
        return sInitialSettingTbl;
    }

    private InitialSettingTbl() {}

    public String getTableName() {
        return TABLE_NAME;
    }

    protected String[] getColumnsDefinition() {
        return new String[] {
                COLUMN_SETTING_NAME + " varchar(50) PRIMARY KEY",
                COLUMN_SETTING_VALUE + " varchar(50)"
        };
    }

    public String[] getColumns() {
        return new String[] {
                COLUMN_SETTING_NAME,
                COLUMN_SETTING_VALUE
        };
    }

    public String[] getPKColumns() {
        return new String[] {
                COLUMN_SETTING_NAME
        };
    }
}
