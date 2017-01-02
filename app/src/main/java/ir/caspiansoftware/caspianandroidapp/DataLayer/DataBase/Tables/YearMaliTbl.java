package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/**
 * Created by Canada on 3/5/2016.
 */
public class YearMaliTbl extends ATableEntity {

    public static final String TABLE_NAME = "yearMali";
    public static final String COLUMN_YEAR_ID = "id";
    public static final String COLUMN_USER_ID_FK = "userId_FK";
    public static final String COLUMN_DAFTAR = "daftar";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_COMPANY = "company";
    public static final String COLUMN_DATABASE = "database_name";
    public static final String COLUMN_IS_CURRENT = "is_current";


    private static YearMaliTbl sYearMaliTbl = null;
    public static YearMaliTbl get() {
        if (sYearMaliTbl == null) {
            sYearMaliTbl = new YearMaliTbl();
        }
        return sYearMaliTbl;
    }


    private YearMaliTbl() {}

    public String getTableName() {
        return TABLE_NAME;
    }

    protected String[] getColumnsDefinition() {
        return new String[] {
                COLUMN_YEAR_ID + " integer PRIMARY KEY AUTOINCREMENT",
                COLUMN_USER_ID_FK + " integer references " + UserTbl.TABLE_NAME + "(" + UserTbl.COLUMN_USER_ID + ") MATCH SIMPLE",
                COLUMN_DAFTAR + " integer",
                COLUMN_YEAR + " integer",
                COLUMN_COMPANY + " varchar(70)",
                COLUMN_DATABASE + " varchar(30)",
                COLUMN_IS_CURRENT + " boolean"
        };
    }

    public String[] getColumns() {
        return new String[] {
                COLUMN_YEAR_ID,
                COLUMN_USER_ID_FK,
                COLUMN_DAFTAR,
                COLUMN_YEAR,
                COLUMN_COMPANY,
                COLUMN_DATABASE,
                COLUMN_IS_CURRENT
        };
    }

    public String[] getPKColumns() {
        return new String[] {
                COLUMN_YEAR_ID
        };
    }
}
