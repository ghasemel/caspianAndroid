package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/**
 * Created by Canada on 1/18/2016.
 */
public class UserTbl extends ATableEntity {

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_USERNAME = "userName";
    public static final String COLUMN_LAST_LOGIN = "lastLogin";
    public static final String COLUMN_IS_LOGON = "isLogon";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IS_ACTIVE = "isActive";
    public static final String COLUMN_VISITOR_CODE = "visitorCode";
    public static final String COLUMN_KALA_PRICE_TYPE = "kalaPriceType";


    private static UserTbl sUserTbl = null;
    public static UserTbl get() {
        if (sUserTbl == null) {
            sUserTbl = new UserTbl();
        }
        return sUserTbl;
    }

    private UserTbl() {}

    public String getTableName() {
        return TABLE_NAME;
    }

    protected String[] getColumnsDefinition() {
        return new String[] {
                COLUMN_USER_ID + " integer primary key",
                COLUMN_USERNAME + " varchar(30) not null unique",
                COLUMN_LAST_LOGIN + " integer",
                COLUMN_IS_LOGON + " integer",
                COLUMN_NAME + " nvarchar(70)",
                COLUMN_IS_ACTIVE + " integer",
                COLUMN_VISITOR_CODE + " integer",
                COLUMN_KALA_PRICE_TYPE + " char"
        };

        /*
        references " + InitialSettingTbl.TABLE_NAME + "(" + InitialSettingTbl.COLUMN_USER_NAME_PK + ") " +
                        " ON DELETE CASCADE ON UPDATE CASCADE MATCH SIMPLE
         */
    }

    public String[] getColumns() {
        return new String[] {
                COLUMN_USER_ID,
                COLUMN_USERNAME,
                COLUMN_LAST_LOGIN,
                COLUMN_IS_LOGON,
                COLUMN_NAME,
                COLUMN_IS_ACTIVE,
                COLUMN_VISITOR_CODE,
                COLUMN_KALA_PRICE_TYPE
        };
    }

    public String[] getPKColumns() {
        return new String[] {
                COLUMN_USER_ID
        };
    }
}
