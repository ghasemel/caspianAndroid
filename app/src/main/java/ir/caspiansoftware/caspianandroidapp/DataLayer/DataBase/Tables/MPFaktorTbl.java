package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/**
 * Created by Canada on 7/22/2016.
 */
public class MPFaktorTbl extends ATableEntity {

    public static final String TABLE_NAME = "mpfaktor";
    public static final String COLUMN_YEAR_ID_FK = "yearId_FK";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NUM = "num";
    public static final String COLUMN_PERSON_ID_FK = "personId_FK";
    public static final String COLUMN_DATE = "faktor_date";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_AVANCE = "avance";
    public static final String COLUMN_ARZESH_AFZOODE = "afzoode";
    public static final String COLUMN_SYNCED = "is_synced";
    public static final String COLUMN_SYNC_DATE = "sync_date";
    public static final String COLUMN_ATF_NUM = "atf_num";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_CREATE_TIMESTAMP = "create_date";


    private static MPFaktorTbl sMPFaktorTbl = null;

    public static MPFaktorTbl get() {
        if (sMPFaktorTbl == null) {
            sMPFaktorTbl = new MPFaktorTbl();
        }
        return sMPFaktorTbl;
    }

    public MPFaktorTbl() {}

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return new String[] {
                COLUMN_YEAR_ID_FK,
                COLUMN_ID,
                COLUMN_NUM,
                COLUMN_PERSON_ID_FK,
                COLUMN_DATE,
                COLUMN_DESCRIPTION,
                COLUMN_AVANCE,
                COLUMN_ARZESH_AFZOODE,
                COLUMN_SYNCED,
                COLUMN_SYNC_DATE,
                COLUMN_ATF_NUM,
                COLUMN_LAT,
                COLUMN_LON,
                COLUMN_CREATE_TIMESTAMP
        };
    }

    @Override
    public String[] getPKColumns() {
        return new String[] {
                COLUMN_ID
        };
    }

    @Override
    protected String[] getColumnsDefinition() {

        addIndex("mpfaktor_unique_num_index", true, COLUMN_NUM, COLUMN_YEAR_ID_FK);

        return new String[] {
                COLUMN_YEAR_ID_FK + " integer references " + YearMaliTbl.TABLE_NAME + "(" + YearMaliTbl.COLUMN_YEAR_ID + ") ON DELETE NO ACTION ON UPDATE CASCADE MATCH SIMPLE NOT NULL",
                COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT",
                COLUMN_NUM + " integer not null", // unique",
                COLUMN_PERSON_ID_FK + " integer references " + PersonTbl.TABLE_NAME + "(" + PersonTbl.COLUMN_ID + ") ON DELETE NO ACTION ON UPDATE CASCADE MATCH SIMPLE",
                COLUMN_DATE + " varchar(10) not null",
                COLUMN_DESCRIPTION + " nvarchar(200)",
                COLUMN_AVANCE + " double",
                COLUMN_ARZESH_AFZOODE + " double",
                COLUMN_SYNCED + " integer",
                COLUMN_SYNC_DATE + " varchar(10)",
                COLUMN_ATF_NUM + " integer",
                COLUMN_LAT + " double",
                COLUMN_LON + " double",
                COLUMN_CREATE_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
        };
    }
}