package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.KalaPhotoTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.KalaPhotoTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.SPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.Models.KalaPhotoModel;
import ir.caspiansoftware.caspianandroidapp.Models.KalaPhotoModel;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaPhotoDataSource extends ADataSource<KalaPhotoModel> implements AutoCloseable {
    private static final String TAG = "KalaDataSource";

    public KalaPhotoDataSource(Context context) {
        super(context);

        open();
    }



    @Override
    public String[] getAllColumns() {
        return KalaPhotoTbl.get().getColumns();
    }

    @Override
    public int delete(KalaPhotoModel kala) {
        return mDatabase.delete(KalaPhotoTbl.TABLE_NAME,
                KalaPhotoTbl.COLUMN_ID + "= ?",
                new String[] {
                        String.valueOf(kala.getId())
                });
    }

    public int deleteOther(List<KalaPhotoModel> kalaList) {
        StringBuilder codes = new StringBuilder();
        for (KalaPhotoModel kala : kalaList) {
            codes.append(kala.getCode());
            codes.append(",");
        }

        if (codes.length() > 0) {
            return mDatabase.delete(KalaPhotoTbl.TABLE_NAME,
                    KalaPhotoTbl.COLUMN_YEAR_ID_FK + " = " + kalaList.get(0).getYearIdFK() + " AND " +
                    KalaPhotoTbl.COLUMN_CODE + " NOT IN(?) ",
                    new String[] { codes.substring(0, codes.length() - 1) });
        }
        return 0;
    }

    @Override
    public int insert(KalaPhotoModel kala) {
        ContentValues cv = objectToContentValue(kala);
        cv.remove(KalaPhotoTbl.COLUMN_ID);
        return (int) mDatabase.insert(KalaPhotoTbl.TABLE_NAME, null, cv);
    }

    @Override
    public int update(KalaPhotoModel kala) {
        ContentValues cv = objectToContentValue(kala);
        cv.remove(KalaPhotoTbl.COLUMN_ID);
        return mDatabase.update(KalaPhotoTbl.TABLE_NAME,
                cv,
                KalaPhotoTbl.COLUMN_YEAR_ID_FK + " = " + kala.getYearIdFK() + " AND " +
                KalaPhotoTbl.COLUMN_CODE + "=" + kala.getCode(), null);
    }

    @Override
    public List<KalaPhotoModel> getAll() {
        Cursor cursor = mDatabase.query(KalaPhotoTbl.TABLE_NAME, getAllColumns(), null,
                null, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            List<KalaPhotoModel> KalaPhotoModelList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                KalaPhotoModel kala = cursorToObject(cursor);
                KalaPhotoModelList.add(kala);
                cursor.moveToNext();
            }
            return KalaPhotoModelList;
        }
        return null;
    }

    @Override
    public KalaPhotoModel getById(int id) {
        Cursor cursor = mDatabase.query(KalaPhotoTbl.TABLE_NAME, getAllColumns(),
                KalaPhotoTbl.COLUMN_ID + " = " + id, null, null, null, null);

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

    public List<KalaPhotoModel> getByCode(String code, int yearId) {
        Cursor cursor = mDatabase.query(KalaPhotoTbl.TABLE_NAME, getAllColumns(),
                KalaPhotoTbl.COLUMN_YEAR_ID_FK + "=" + yearId + " AND " +
                KalaPhotoTbl.COLUMN_CODE + " = '" + code + "'", null, null, null, null);

        List<KalaPhotoModel> kala = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            kala.add(cursorToObject(cursor));
            cursor.moveToNext();
        }
        return kala;
    }

    public boolean isExistByCode(String code, String fileName, int yearId) {
        Log.d(TAG, "isExistByCode()");

        Cursor cursor = null;
        try {
            cursor = mDatabase.query(KalaPhotoTbl.TABLE_NAME, getAllColumns(),
                    KalaPhotoTbl.COLUMN_YEAR_ID_FK + " = " + yearId + " AND " +
                    KalaPhotoTbl.COLUMN_CODE + " = '" + code + "' AND " +
                    KalaPhotoTbl.COLUMN_FILE_NAME + " = '" + fileName + "'", null, null, null, null);

            cursor.moveToFirst();
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public ArrayList<KalaPhotoModel> getKalaPhotosListByYearId(int yearId) {
        Log.d(TAG, "getKalaListByYearId(): function entered");

        Cursor cursor = mDatabase.query(KalaPhotoTbl.TABLE_NAME, getAllColumns(), KalaPhotoTbl.COLUMN_YEAR_ID_FK + " = " + yearId, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            ArrayList<KalaPhotoModel> KalaPhotoModelList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                KalaPhotoModelList.add(cursorToObject(cursor));
                cursor.moveToNext();
            }
            return KalaPhotoModelList;
        }
        return null;
    }

    @Override
    protected ContentValues objectToContentValue(KalaPhotoModel kala) {
        ContentValues cv = new ContentValues();
        cv.put(KalaPhotoTbl.COLUMN_YEAR_ID_FK, kala.getYearIdFK());
        cv.put(KalaPhotoTbl.COLUMN_ID, kala.getId());
        cv.put(KalaPhotoTbl.COLUMN_SOURCE_ID, kala.getServerPhotoId());
        cv.put(KalaPhotoTbl.COLUMN_CODE, kala.getCode());
        cv.put(KalaPhotoTbl.COLUMN_FILE_NAME, kala.getFileName());
        cv.put(KalaPhotoTbl.COLUMN_TITLE, kala.getTitle());
        return cv;
    }

    @Override
    protected KalaPhotoModel cursorToObject(Cursor cursor) {
        KalaPhotoModel kala = new KalaPhotoModel();
        kala.setYearIdFK(cursor.getInt(0));
        kala.setId(cursor.getInt(1));
        kala.setServerPhotoId(cursor.getInt(2));
        kala.setCode(cursor.getString(3));
        kala.setFileName(cursor.getString(4));
        kala.setTitle(cursor.getString(5));
        return kala;
    }
}
