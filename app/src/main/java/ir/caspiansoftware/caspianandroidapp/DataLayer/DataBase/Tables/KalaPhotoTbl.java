package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaPhotoTbl extends ATableEntity {

    public static final String TABLE_NAME = "kalaPhoto";
    public static final String COLUMN_YEAR_ID_FK = "yearId_FK";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_SOURCE_ID = "sourceId";
    public static final String COLUMN_FILE_NAME = "fileName";
    public static final String COLUMN_TITLE = "title";

    private static KalaPhotoTbl sKalaTbl = null;
    public static KalaPhotoTbl get() {
        if (sKalaTbl == null) {
            sKalaTbl = new KalaPhotoTbl();
        }
        return sKalaTbl;
    }

    public KalaPhotoTbl() {}

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return new String[] {
                COLUMN_YEAR_ID_FK,
                COLUMN_ID,
                COLUMN_SOURCE_ID,
                COLUMN_CODE,
                COLUMN_FILE_NAME,
                COLUMN_TITLE
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

        addIndex("kala_photo_unique_index", true, COLUMN_YEAR_ID_FK, COLUMN_CODE);

        return new String[] {
                COLUMN_YEAR_ID_FK + " integer references " + YearMaliTbl.TABLE_NAME + "(" + YearMaliTbl.COLUMN_YEAR_ID + ") ON DELETE NO ACTION ON UPDATE CASCADE MATCH SIMPLE",
                COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT",
                COLUMN_SOURCE_ID + " integer not null",
                COLUMN_CODE + " varchar(30) not null", // unique",
                COLUMN_FILE_NAME + " nvarchar(500) not null",
                COLUMN_TITLE + " nvarchar(500)"
        };
    }

}
