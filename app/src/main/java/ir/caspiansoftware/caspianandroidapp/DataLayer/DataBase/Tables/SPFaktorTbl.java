package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/**
 * Created by Canada on 7/22/2016.
 */
public class SPFaktorTbl extends ATableEntity {

    public static final String TABLE_NAME = "spfaktor";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MPFAKTOR_ID_FK = "mpfaktorId_FK";
    public static final String COLUMN_KALA_ID_FK = "kalaId_FK";
    public static final String COLUMN_SCOUNT = "scount";
    public static final String COLUMN_MCOUNT = "mcount";
    public static final String COLUMN_PRICE = "price";

    private static SPFaktorTbl sSPFaktorTbl = null;

    public static SPFaktorTbl get() {
        if (sSPFaktorTbl == null) {
            sSPFaktorTbl = new SPFaktorTbl();
        }
        return sSPFaktorTbl;
    }

    public SPFaktorTbl() {
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return new String[]{
                COLUMN_ID,
                COLUMN_MPFAKTOR_ID_FK,
                COLUMN_KALA_ID_FK,
                COLUMN_SCOUNT,
                COLUMN_MCOUNT,
                COLUMN_PRICE
        };
    }

    @Override
    public String[] getPKColumns() {
        return new String[]{
                COLUMN_ID
        };
    }

    @Override
    protected String[] getColumnsDefinition() {

        //addIndex("mpfaktor_unique_index", true, COLUMN_NUM);

        return new String[]{
                COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT",
                COLUMN_MPFAKTOR_ID_FK + " integer references " + MPFaktorTbl.TABLE_NAME + "(" + MPFaktorTbl.COLUMN_ID + ") ON DELETE NO ACTION ON UPDATE CASCADE MATCH SIMPLE",
                COLUMN_KALA_ID_FK + " integer references " + KalaTbl.TABLE_NAME + "(" + KalaTbl.COLUMN_ID + ") ON DELETE NO ACTION ON UPDATE CASCADE MATCH SIMPLE",
                COLUMN_SCOUNT + " double not null",
                COLUMN_MCOUNT + " double not null",
                COLUMN_PRICE + " long not null"
        };
    }
}