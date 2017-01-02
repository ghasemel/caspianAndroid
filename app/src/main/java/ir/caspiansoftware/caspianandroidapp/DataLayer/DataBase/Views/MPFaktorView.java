package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Views;

import info.elyasi.android.elyasilib.DataBase.AViewEntity;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.MPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.SPFaktorTbl;

/**
 * Created by Canada on 7/29/2016.
 */
public class MPFaktorView extends AViewEntity {
    public static final String VIEW_NAME = "mpfaktor_view";
    public static final String COLUMN_TOTAL_PRICE = "total_price";

    private static MPFaktorView sMPFaktorView;
    public static MPFaktorView get() {
        if (sMPFaktorView == null)
            sMPFaktorView = new MPFaktorView();
        return sMPFaktorView;
    }

    private MPFaktorView() {}

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }


    @Override
    public String[] getColumns() {
        String[] cols = new String[MPFaktorTbl.get().getColumns().length + 1];
        for (int c = 0; c < MPFaktorTbl.get().getColumns().length; c++) {
            cols[c] = MPFaktorTbl.get().getColumns()[c];
        }
        cols[cols.length - 1] = COLUMN_TOTAL_PRICE;
        return cols;
    }

    @Override
    public String[] getColumnsDefinition() {
        return new String[] {
                MPFaktorTbl.TABLE_NAME + ".*",
                "SUM(" +
                        SPFaktorTbl.TABLE_NAME + "." + SPFaktorTbl.COLUMN_PRICE + " * " +
                        SPFaktorTbl.TABLE_NAME + "." + SPFaktorTbl.COLUMN_SCOUNT +
                        ") as " + COLUMN_TOTAL_PRICE
        };
    }

    @Override
    public String[] getJoins() {
        return new String[] {
                MPFaktorTbl.TABLE_NAME + LEFT_JOIN + SPFaktorTbl.TABLE_NAME + JOIN_ON +
                        MPFaktorTbl.TABLE_NAME + "." + MPFaktorTbl.COLUMN_ID + JOIN_EQUAL +
                        SPFaktorTbl.TABLE_NAME + "." + SPFaktorTbl.COLUMN_MPFAKTOR_ID_FK +
                        GROUP_BY +  MPFaktorTbl.TABLE_NAME + "." + MPFaktorTbl.COLUMN_ID

        };
    }
}
