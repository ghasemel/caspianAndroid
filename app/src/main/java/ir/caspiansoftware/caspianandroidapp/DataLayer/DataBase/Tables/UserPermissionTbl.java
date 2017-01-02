package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/*
 * Created by Canada on 1/18/2016.
 */
public class UserPermissionTbl extends ATableEntity {
    public static final String TABLE_NAME = "userPermission";
    public static final String COLUMN_USER_ID_FK = "userId_FK";
    public static final String COLUMN_PART = "part";
    public static final String COLUMN_WRITE = "write";
    public static final String COLUMN_ACCESS = "access";

    public enum Parts {
        Faktor
    };



    private static UserPermissionTbl sPermissionTbl = null;
    public static UserPermissionTbl get() {
        if (sPermissionTbl == null) {
            sPermissionTbl = new UserPermissionTbl();
        }
        return sPermissionTbl;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String[] getColumns() {
        return new String[] {
                COLUMN_USER_ID_FK,
                COLUMN_PART,
                COLUMN_WRITE,
                COLUMN_ACCESS
        };
    }

    public String[] getPKColumns() {
        return new String[] {
                COLUMN_USER_ID_FK,
                COLUMN_PART
        };
    }

    protected String[] getColumnsDefinition() {
        return new String[] {
                COLUMN_USER_ID_FK + " integer references " + UserTbl.TABLE_NAME + "(" + UserTbl.COLUMN_USER_ID + ") MATCH SIMPLE",
                COLUMN_PART + " varchar(20)",
                COLUMN_WRITE + " integer",
                COLUMN_ACCESS + " integer",
                "PRIMARY KEY (" + COLUMN_USER_ID_FK + ", " + COLUMN_PART + ")"
        };
    }

    public static String getPartName(Parts pPart) {
        switch (pPart) {
            case Faktor:
                return "faktor";
            default:
                return null;
        }
    }
}
