package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.KalaTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.SPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaDataSource extends ADataSource<KalaModel> {
    private static final String TAG = "KalaDataSource";

    public KalaDataSource(Context context) {
        super(context);
    }

    @Override
    public String[] getAllColumns() {
        return KalaTbl.get().getColumns();
    }

    @Override
    public int delete(KalaModel kala) {
        return mDatabase.delete(KalaTbl.TABLE_NAME,
                KalaTbl.COLUMN_ID + "= ?",
                new String[] {
                        String.valueOf(kala.getID())
                });
    }

    public int deleteOther(List<KalaModel> kalaList) {
        StringBuilder codes = new StringBuilder();
        for (KalaModel kala : kalaList) {
            codes.append(kala.getCode());
            codes.append(",");
        }

        if (codes.length() > 0) {
            return mDatabase.delete(KalaTbl.TABLE_NAME,
                    KalaTbl.COLUMN_YEAR_ID_FK + " = " + kalaList.get(0).getYearId_FK() + " AND " +
                    KalaTbl.COLUMN_CODE + " NOT IN(?) AND " +
                    KalaTbl.COLUMN_ID + " NOT IN(SELECT " + SPFaktorTbl.COLUMN_KALA_ID_FK + " FROM " + SPFaktorTbl.TABLE_NAME + ")",
                    new String[] { codes.substring(0, codes.length() - 1) });
        }
        return 0;
    }

    @Override
    public int insert(KalaModel kala) {
        ContentValues cv = objectToContentValue(kala);
        cv.remove(KalaTbl.COLUMN_ID);
        return (int) mDatabase.insert(KalaTbl.TABLE_NAME, null, cv);
    }

    @Override
    public int update(KalaModel kala) {
        ContentValues cv = objectToContentValue(kala);
        cv.remove(KalaTbl.COLUMN_ID);
        return mDatabase.update(KalaTbl.TABLE_NAME,
                cv,
                KalaTbl.COLUMN_YEAR_ID_FK + " = " + kala.getYearId_FK() + " AND " +
                KalaTbl.COLUMN_CODE + "=" + kala.getCode(), null);
    }

    @Override
    public List<KalaModel> getAll() {
        Cursor cursor = mDatabase.query(KalaTbl.TABLE_NAME, getAllColumns(), null,
                null, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            List<KalaModel> kalaModelList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                KalaModel kala = cursorToObject(cursor);
                kalaModelList.add(kala);
                cursor.moveToNext();
            }
            return kalaModelList;
        }
        return null;
    }

    @Override
    public KalaModel getById(int id) {
        Cursor cursor = mDatabase.query(KalaTbl.TABLE_NAME, getAllColumns(),
                KalaTbl.COLUMN_ID + " = " + id, null, null, null, null);

        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursorToObject(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public KalaModel getByCode(String code, int yearId) {
        Cursor cursor = mDatabase.query(KalaTbl.TABLE_NAME, getAllColumns(),
                KalaTbl.COLUMN_YEAR_ID_FK + "=" + yearId + " AND " +
                KalaTbl.COLUMN_CODE + " = '" + code + "'", null, null, null, null);

        KalaModel kala = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            kala = cursorToObject(cursor);
        }
        return kala;
    }

    public boolean isExistByCode(String code, int yearId) {
        Log.d(TAG, "isExistByCode()");

        Cursor cursor = null;
        try {
            cursor = mDatabase.query(KalaTbl.TABLE_NAME, getAllColumns(),
                    KalaTbl.COLUMN_YEAR_ID_FK + " = " + yearId + " AND " +
                    KalaTbl.COLUMN_CODE + " = '" + code + "'", null, null, null, null);

            cursor.moveToFirst();
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public ArrayList<KalaModel> getKalaListByYearId(int yearId) {
        Log.d(TAG, "getKalaListByYearId(): function entered");

        Cursor cursor = mDatabase.query(KalaTbl.TABLE_NAME, getAllColumns(), KalaTbl.COLUMN_YEAR_ID_FK + " = " + yearId, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            ArrayList<KalaModel> kalaModelList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                kalaModelList.add(cursorToObject(cursor));
                cursor.moveToNext();
            }
            return kalaModelList;
        }
        return null;
    }

    @Override
    protected ContentValues objectToContentValue(KalaModel kala) {
        ContentValues cv = new ContentValues();
        cv.put(KalaTbl.COLUMN_YEAR_ID_FK, kala.getYearId_FK());
        cv.put(KalaTbl.COLUMN_ID, kala.getID());
        cv.put(KalaTbl.COLUMN_CODE, kala.getCode());
        cv.put(KalaTbl.COLUMN_NAME, kala.getName());
        cv.put(KalaTbl.COLUMN_VAHED_A, kala.getVahedA());
        cv.put(KalaTbl.COLUMN_VAHED_F, kala.getVahedF());
        cv.put(KalaTbl.COLUMN_MOHTAVI, kala.getMohtavi());
        cv.put(KalaTbl.COLUMN_PRICE1, kala.getPrice1());
        cv.put(KalaTbl.COLUMN_PRICE2, kala.getPrice2());
        cv.put(KalaTbl.COLUMN_PRICE3, kala.getPrice3());
        cv.put(KalaTbl.COLUMN_PRICE_OMDE, kala.getPriceOmde());
        cv.put(KalaTbl.COLUMN_MOJOODI, kala.getMojoodi());
        return cv;
    }

    @Override
    protected KalaModel cursorToObject(Cursor cursor) {
        KalaModel kala = new KalaModel();
        kala.setYearId_FK(cursor.getInt(0));
        kala.setID(cursor.getInt(1));
        kala.setCode(cursor.getString(2));
        kala.setName(cursor.getString(3));
        kala.setVahedA(cursor.getString(4));
        kala.setVahedF(cursor.getString(5));
        kala.setMohtavi(cursor.getDouble(6));
        kala.setPrice1(cursor.getDouble(7));
        kala.setPrice2(cursor.getDouble(8));
        kala.setPrice3(cursor.getDouble(9));
        kala.setPriceOmde(cursor.getDouble(10));
        kala.setMojoodi(cursor.getDouble(11));
        return kala;
    }
}
