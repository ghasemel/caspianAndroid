package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables;

import info.elyasi.android.elyasilib.DataBase.ATableEntity;

/**
 * Created by Canada on 6/21/2016.
 */
public class PersonTbl extends ATableEntity {

    public static final String TABLE_NAME = "person";
    public static final String COLUMN_YEAR_ID_FK = "yearId_FK";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name_hesab";
    public static final String COLUMN_MANDE = "mande";
    public static final String COLUMN_TEL = "tel";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_ADDRESS = "address";


    private static PersonTbl sPersonTbl = null;
    public static PersonTbl get() {
        if (sPersonTbl == null) {
            sPersonTbl = new PersonTbl();
        }
        return sPersonTbl;
    }

    public PersonTbl() {}

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
                COLUMN_MANDE,
                COLUMN_TEL,
                COLUMN_MOBILE,
                COLUMN_ADDRESS
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
        addIndex("person_unique_index", true, COLUMN_YEAR_ID_FK, COLUMN_CODE);

        return new String[] {
                COLUMN_YEAR_ID_FK + " integer references " + YearMaliTbl.TABLE_NAME + "(" + YearMaliTbl.COLUMN_YEAR_ID + ") ON DELETE NO ACTION ON UPDATE CASCADE MATCH SIMPLE",
                COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT",
                COLUMN_CODE + " varchar(25)",
                COLUMN_NAME + " nvarchar(100)",
                COLUMN_MANDE + " double",
                COLUMN_TEL + " varchar(50)",
                COLUMN_MOBILE + " varchar(50)",
                COLUMN_ADDRESS + " nvarchar(100)"
        };
    }
}
