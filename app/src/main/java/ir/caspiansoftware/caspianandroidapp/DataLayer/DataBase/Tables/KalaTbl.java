package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaTbl extends ATableEntity {

    public static final String TABLE_NAME = "kala";
    public static final String COLUMN_YEAR_ID_FK = "yearId_FK";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VAHED_A = "vahed_a";
    public static final String COLUMN_VAHED_F = "vahed_f";
    public static final String COLUMN_MOHTAVI = "mohtavi";
    public static final String COLUMN_PRICE1 = "price1";
    public static final String COLUMN_PRICE2 = "price2";
    public static final String COLUMN_PRICE3 = "price3";
    public static final String COLUMN_PRICE_OMDE = "price_omde";
    public static final String COLUMN_MOJOODI = "mojoodi";

    private static KalaTbl sKalaTbl = null;
    public static KalaTbl get() {
        if (sKalaTbl == null) {
            sKalaTbl = new KalaTbl();
        }
        return sKalaTbl;
    }

    public KalaTbl() {}

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getColumns() {
        return new String[] {
                COLUMN_YEAR_ID_FK,
                COLUMN_ID,
                COLUMN_CODE,
                COLUMN_NAME,
                COLUMN_VAHED_A,
                COLUMN_VAHED_F,
                COLUMN_MOHTAVI,
                COLUMN_PRICE1,
                COLUMN_PRICE2,
                COLUMN_PRICE3,
                COLUMN_PRICE_OMDE,
                COLUMN_MOJOODI
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

        addIndex("kala_unique_index", true, COLUMN_YEAR_ID_FK, COLUMN_CODE);

        return new String[] {
                COLUMN_YEAR_ID_FK + " integer references " + YearMaliTbl.TABLE_NAME + "(" + YearMaliTbl.COLUMN_YEAR_ID + ") ON DELETE NO ACTION ON UPDATE CASCADE MATCH SIMPLE",
                COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT",
                COLUMN_CODE + " varchar(30) not null", // unique",
                COLUMN_NAME + " nvarchar(300) not null",
                COLUMN_VAHED_A + " nvarchar(18)",
                COLUMN_VAHED_F + " nvarchar(18)",
                COLUMN_MOHTAVI + " double",
                COLUMN_PRICE1 + " double",
                COLUMN_PRICE2 + " double",
                COLUMN_PRICE3 + " double",
                COLUMN_PRICE_OMDE + " double",
                COLUMN_MOJOODI + " double"
        };
    }



    //@Override
    //public String createTableSql() {
    //    return super.createTableSql();
    //}
}
