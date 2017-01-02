package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.SPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;

/**
 * Created by Canada on 7/22/2016.
 */
public class SPFaktorDataSource extends ADataSource<SPFaktorModel> {

    public SPFaktorDataSource(Context context) {
        super(context);
    }

    @Override
    public String[] getAllColumns() {
        return SPFaktorTbl.get().getColumns();
    }

    @Override
    public int insert(SPFaktorModel spFaktorModel) {
        ContentValues cv = objectToContentValue(spFaktorModel);
        cv.remove(SPFaktorTbl.COLUMN_ID);
        return (int) mDatabase.insertOrThrow(SPFaktorTbl.TABLE_NAME, null, cv);
    }

    @Override
    public int delete(SPFaktorModel spFaktorModel) {
        return 0;
    }

    public int deleteByMPFaktorId(int mpfaktorId) {
        return mDatabase.delete(
                SPFaktorTbl.TABLE_NAME,
                SPFaktorTbl.COLUMN_MPFAKTOR_ID_FK + "=" + mpfaktorId,
                null);
    }

    public int deleteOther(List<SPFaktorModel> spFaktorlList) {
        if (spFaktorlList != null) {
            StringBuilder ids = new StringBuilder();

            for (SPFaktorModel sp : spFaktorlList) {
                ids.append(String.valueOf(sp.getId()));
                ids.append(",");
            }


            if (ids.length() > 0) {
                String in = ids.substring(0, ids.length() - 1);
                return mDatabase.delete(
                        SPFaktorTbl.TABLE_NAME,
                        SPFaktorTbl.COLUMN_MPFAKTOR_ID_FK + "=" + spFaktorlList.get(0).getMPFaktorId_FK() + " AND " +
                                SPFaktorTbl.COLUMN_ID + " NOT IN(" + in + ")", null);
            }
        }
        return -1;
    }

    @Override
    public int update(SPFaktorModel spFaktorModel) throws Exception {
        ContentValues cv = objectToContentValue(spFaktorModel);
        return mDatabase.update(SPFaktorTbl.TABLE_NAME, cv,
                SPFaktorTbl.COLUMN_ID + "=" + spFaktorModel.getId(), null);
    }

    @Override
    public SPFaktorModel getById(int id) {
        Cursor cursor = mDatabase.query(SPFaktorTbl.TABLE_NAME, getAllColumns(),
                SPFaktorTbl.COLUMN_ID + "=" + id, null, null, null, null);

        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursorToObject(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public boolean isExistById(int id) {
        Cursor cursor = mDatabase.query(SPFaktorTbl.TABLE_NAME, getAllColumns(),
                SPFaktorTbl.COLUMN_ID + "=" + id, null, null, null, null);

        try {
            cursor.moveToFirst();
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ArrayList<SPFaktorModel> getByMPFaktorId(long mpfaktorId) {
        Cursor cursor = mDatabase.query(SPFaktorTbl.TABLE_NAME, getAllColumns(),
                SPFaktorTbl.COLUMN_MPFAKTOR_ID_FK + "=" + mpfaktorId, null, null, null, null);

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ArrayList<SPFaktorModel> spFaktorModelList = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    spFaktorModelList.add(cursorToObject(cursor));
                    cursor.moveToNext();
                }
                return spFaktorModelList;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public List<SPFaktorModel> getAll() {
        return null;
    }

    @Override
    protected SPFaktorModel cursorToObject(Cursor cursor) {
        SPFaktorModel spFaktorModel = new SPFaktorModel();
        spFaktorModel.setId(cursor.getInt(0));
        spFaktorModel.setMPFaktorId_FK(cursor.getInt(1));
        spFaktorModel.setKalaId_FK(cursor.getInt(2));
        spFaktorModel.setSCount(cursor.getDouble(3));
        spFaktorModel.setMCount(cursor.getDouble(4));
        spFaktorModel.setPrice(cursor.getLong(5));
        return spFaktorModel;
    }

    @Override
    protected ContentValues objectToContentValue(SPFaktorModel spFaktorModel) {
        ContentValues cv = new ContentValues();
        cv.put(SPFaktorTbl.COLUMN_ID, spFaktorModel.getId());
        cv.put(SPFaktorTbl.COLUMN_MPFAKTOR_ID_FK, spFaktorModel.getMPFaktorId_FK());
        cv.put(SPFaktorTbl.COLUMN_KALA_ID_FK, spFaktorModel.getKalaId_FK());
        cv.put(SPFaktorTbl.COLUMN_SCOUNT, spFaktorModel.getSCount());
        cv.put(SPFaktorTbl.COLUMN_MCOUNT, spFaktorModel.getMCount());
        cv.put(SPFaktorTbl.COLUMN_PRICE, spFaktorModel.getPrice());
        return cv;
    }
}