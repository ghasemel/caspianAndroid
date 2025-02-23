package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Persian.PersianDate;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.MPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Views.MPFaktorView;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/22/2016.
 */
public class MaliDataSource extends ADataSource<MaliModel> {
    private static final String TAG = "MPFaktorDataSource";


    public MaliDataSource(Context context) {
        super(context);
    }

    @Override
    public String[] getAllColumns() {
        return MPFaktorTbl.get().getColumns();
    }

    @Override
    public int insert(MaliModel mpFaktorModel) {
        ContentValues cv = objectToContentValue(mpFaktorModel);
        cv.remove(MPFaktorTbl.COLUMN_ID);
        return (int) mDatabase.insertOrThrow(MPFaktorTbl.TABLE_NAME, null, cv);
    }

    @Override
    public int delete(MaliModel mpFaktorModel) {
        return mDatabase.delete(
                MPFaktorTbl.TABLE_NAME,
                MPFaktorTbl.COLUMN_ID + "=" + mpFaktorModel.getId(),
                null);
    }

    public int deleteById(int id) {
        return mDatabase.delete(MPFaktorTbl.TABLE_NAME, MPFaktorTbl.COLUMN_ID + "=" + id, null);
    }

    @Override
    public int update(MaliModel mpFaktorModel) throws Exception {
        ContentValues cv = objectToContentValue(mpFaktorModel);

        // to not modify lat/lang
        cv.remove(MPFaktorTbl.COLUMN_LAT);
        cv.remove(MPFaktorTbl.COLUMN_LON);

        return mDatabase.update(MPFaktorTbl.TABLE_NAME, cv, MPFaktorTbl.COLUMN_ID + "=" + mpFaktorModel.getId(), null);
    }

    public void updateSync(int mpFaktorId, int atfNum) {
        mDatabase.execSQL(
                "UPDATE " + MPFaktorTbl.TABLE_NAME +
                " SET " + MPFaktorTbl.COLUMN_SYNC_DATE + "=?," +
                MPFaktorTbl.COLUMN_SYNCED + "= 1," +
                MPFaktorTbl.COLUMN_ATF_NUM + "= ?" +
                " WHERE " + MPFaktorTbl.COLUMN_ID + " = ?",
                new Object[] {
                        PersianDate.getToday(),
                        atfNum,
                        mpFaktorId
                });
    }

    @Override
    public MaliModel getById(int id) {
        Cursor cursor = mDatabase.query(MPFaktorTbl.TABLE_NAME, getAllColumns(),
                MPFaktorTbl.COLUMN_ID + " = " + id, null, null, null, null);

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

    @Override
    public List<MaliModel> getAll() {
        return null;
    }


    public ArrayList<MaliModel> getAllByYearId(int yearId, boolean DescOrder) {
        Cursor cursor  = mDatabase.query(
                MPFaktorView.VIEW_NAME,
                MPFaktorView.get().getColumns(),
                MPFaktorTbl.COLUMN_YEAR_ID_FK + "=" + yearId, null, null, null,
                MPFaktorTbl.COLUMN_ID + (DescOrder ? " desc" : ""));

        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ArrayList<MaliModel> list = new ArrayList<>();

                while (!cursor.isAfterLast()) {
                    list.add(cursorToObject(cursor));
                    cursor.moveToNext();
                }
                return list;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }



    public MaliModel getByNum(int num, int yearId) {
        Cursor cursor = mDatabase.query(MPFaktorTbl.TABLE_NAME, getAllColumns(),
                MPFaktorTbl.COLUMN_YEAR_ID_FK + "="+ yearId + " AND " +
                MPFaktorTbl.COLUMN_NUM + " = " + num, null, null, null, null);

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

    public MaliModel getPrevOrNext(int currentId, boolean prev, int yearId) {

        String function = prev ? "MAX" : "MIN";
        String operator = prev ? "<" : ">";
        String where = MPFaktorTbl.COLUMN_ID + operator + currentId;

        Cursor cursor = mDatabase.query(MPFaktorTbl.TABLE_NAME,
                new String[] { function + "(" + MPFaktorTbl.COLUMN_ID + ")"},
                MPFaktorTbl.COLUMN_YEAR_ID_FK + "="+ yearId + " AND " + where, null, null, null, null);

        int id = -1;
        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                id = cursor.getInt(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        if (id > -1) {
            return getById(id);
        }

        return null;
    }

    public MaliModel getFirstOrLast(boolean first, int yearId) {

        String function = first ? "MIN" : "MAX";

        Cursor cursor = mDatabase.query(MPFaktorTbl.TABLE_NAME,
                new String[] { function + "(" + MPFaktorTbl.COLUMN_ID + ")"},
                MPFaktorTbl.COLUMN_YEAR_ID_FK + "="+ yearId, null, null, null, null);

        int id = -1;
        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                id = cursor.getInt(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        if (id > -1) {
            return getById(id);
        }

        return null;
    }


    public int getMaxNum(int yearId) {
        Cursor cursor = mDatabase.query(MPFaktorTbl.TABLE_NAME,
                new String[] { "MAX(" + MPFaktorTbl.COLUMN_NUM + ")"},
                MPFaktorTbl.COLUMN_YEAR_ID_FK + "="+ yearId, null, null, null, null);


        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    @Override
    protected MaliModel cursorToObject(Cursor cursor) {
        MaliModel model = new MaliModel();
        model.setYearId_FK(cursor.getInt(0));
        model.setId(cursor.getInt(1));
        model.setNum(cursor.getInt(2));
        model.setPersonId_FK(cursor.getInt(3));
        model.setDate(cursor.getString(4));
        model.setDescription(cursor.getString(5));
        model.setSynced(cursor.getInt(8) == 1);
        model.setSyncDate(cursor.getString(9));
        model.setAtfNum(cursor.getInt(10));
        model.setLat(cursor.getDouble(11));
        model.setLon(cursor.getDouble(12));

        try {
            if (cursor.getString(13) != null)
                model.setCreateDate(Vars.iso8601Format.parse(cursor.getString(13)));
        } catch (ParseException e) {
            Log.e(TAG, "error on converting create_date column", e);
            throw new RuntimeException("error on converting create_date column");
        }

        if (cursor.getColumnIndex(MPFaktorView.COLUMN_TOTAL_PRICE) != -1)
            model.setPriceTotal(cursor.getLong(14));

        return model;
    }

    @Override
    protected ContentValues objectToContentValue(MaliModel mpFaktorModel) {
        ContentValues cv = new ContentValues();
        cv.put(MPFaktorTbl.COLUMN_YEAR_ID_FK, mpFaktorModel.getYearId_FK());
        cv.put(MPFaktorTbl.COLUMN_ID, mpFaktorModel.getId());
        cv.put(MPFaktorTbl.COLUMN_NUM, mpFaktorModel.getNum());
        cv.put(MPFaktorTbl.COLUMN_PERSON_ID_FK, mpFaktorModel.getPersonId_FK());
        cv.put(MPFaktorTbl.COLUMN_DATE, mpFaktorModel.getDate());
        cv.put(MPFaktorTbl.COLUMN_DESCRIPTION, mpFaktorModel.getDescription());
        cv.put(MPFaktorTbl.COLUMN_SYNCED, mpFaktorModel.isSynced() ? 1 : 0);
        cv.put(MPFaktorTbl.COLUMN_SYNC_DATE, mpFaktorModel.getSyncDate());
        cv.put(MPFaktorTbl.COLUMN_ATF_NUM, mpFaktorModel.getAtfNum());
        cv.put(MPFaktorTbl.COLUMN_LAT, mpFaktorModel.getLat());
        cv.put(MPFaktorTbl.COLUMN_LON, mpFaktorModel.getLon());

        if (mpFaktorModel.getCreateDate() != null)
            cv.put(MPFaktorTbl.COLUMN_CREATE_TIMESTAMP, mpFaktorModel.getCreateDateInIsoFormat());
        return cv;
    }
}
