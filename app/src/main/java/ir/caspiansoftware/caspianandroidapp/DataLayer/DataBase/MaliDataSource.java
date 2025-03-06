package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Persian.PersianDate;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.MaliTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Views.MPFaktorView;
import ir.caspiansoftware.caspianandroidapp.Enum.MaliType;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/22/2016.
 */
public class MaliDataSource extends ADataSource<MaliModel> {
    private static final String TAG = "MaliDataSource";


    public MaliDataSource(Context context) {
        super(context);
        open();
    }

    @Override
    public String[] getAllColumns() {
        return MaliTbl.getInstance().getColumns();
    }

    @Override
    public int insert(MaliModel maliModel) {
        ContentValues cv = objectToContentValue(maliModel);
        cv.remove(MaliTbl.COLUMN_ID);
        return (int) mDatabase.insertOrThrow(MaliTbl.TABLE_NAME, null, cv);
    }

    @Override
    public int delete(MaliModel maliModel) {
        return mDatabase.delete(
                MaliTbl.TABLE_NAME,
                MaliTbl.COLUMN_ID + "=" + maliModel.getId(),
                null);
    }

    public int deleteById(int id) {
        return mDatabase.delete(MaliTbl.TABLE_NAME, MaliTbl.COLUMN_ID + "=" + id, null);
    }

    @Override
    public int update(MaliModel maliModel) throws Exception {
        ContentValues cv = objectToContentValue(maliModel);

        // to not modify lat/lang
        cv.remove(MaliTbl.COLUMN_LAT);
        cv.remove(MaliTbl.COLUMN_LON);

        return mDatabase.update(MaliTbl.TABLE_NAME, cv, MaliTbl.COLUMN_ID + "=" + maliModel.getId(), null);
    }

    public void updateSync(int mpFaktorId, int atfNum) {
        mDatabase.execSQL(
                "UPDATE " + MaliTbl.TABLE_NAME +
                " SET " + MaliTbl.COLUMN_SYNC_DATE + "=?," +
                MaliTbl.COLUMN_SYNCED + "= 1," +
                MaliTbl.COLUMN_ATF_NUM + "= ?" +
                " WHERE " + MaliTbl.COLUMN_ID + " = ?",
                new Object[] {
                        PersianDate.getToday(),
                        atfNum,
                        mpFaktorId
                });
    }

    @Override
    public MaliModel getById(int id) {

        try (Cursor cursor = mDatabase.query(MaliTbl.TABLE_NAME, getAllColumns(),
                MaliTbl.COLUMN_ID + " = " + id, null, null, null, null)) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursorToObject(cursor);
            }
        }
        return null;
    }

    @Override
    public List<MaliModel> getAll() {
        return null;
    }


    public ArrayList<MaliModel> getAllByYearId(int yearId, boolean DescOrder) {

        try (Cursor cursor = mDatabase.query(
                MPFaktorView.VIEW_NAME,
                MPFaktorView.get().getColumns(),
                MaliTbl.COLUMN_YEAR_ID_FK + "=" + yearId, null, null, null,
                MaliTbl.COLUMN_ID + (DescOrder ? " desc" : ""))) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ArrayList<MaliModel> list = new ArrayList<>();

                while (!cursor.isAfterLast()) {
                    list.add(cursorToObject(cursor));
                    cursor.moveToNext();
                }
                return list;
            }
        }

        return null;
    }



    public MaliModel getByNum(int num, int yearId) {

        try (Cursor cursor = mDatabase.query(MaliTbl.TABLE_NAME, getAllColumns(),
                MaliTbl.COLUMN_YEAR_ID_FK + "=" + yearId + " AND " +
                        MaliTbl.COLUMN_NUM + " = " + num, null, null, null, null)) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursorToObject(cursor);
            }
        }
        return null;
    }

    public MaliModel getPrevOrNext(int currentId, boolean prev, int yearId) {

        String function = prev ? "MAX" : "MIN";
        String operator = prev ? "<" : ">";
        String where = MaliTbl.COLUMN_ID + operator + currentId;

        Cursor cursor = mDatabase.query(MaliTbl.TABLE_NAME,
                new String[] { function + "(" + MaliTbl.COLUMN_ID + ")"},
                MaliTbl.COLUMN_YEAR_ID_FK + "="+ yearId + " AND " + where, null, null, null, null);

        int id = -1;
        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                id = cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }

        if (id > -1) {
            return getById(id);
        }

        return null;
    }

    public MaliModel getFirstOrLast(boolean first, int yearId) {

        String function = first ? "MIN" : "MAX";

        Cursor cursor = mDatabase.query(MaliTbl.TABLE_NAME,
                new String[] { function + "(" + MaliTbl.COLUMN_ID + ")"},
                MaliTbl.COLUMN_YEAR_ID_FK + "="+ yearId, null, null, null, null);

        int id = -1;
        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                id = cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }

        if (id > -1) {
            return getById(id);
        }

        return null;
    }


    public int getMaxNum(int yearId) {


        try (Cursor cursor = mDatabase.query(MaliTbl.TABLE_NAME,
                new String[]{"MAX(" + MaliTbl.COLUMN_NUM + ")"},
                MaliTbl.COLUMN_YEAR_ID_FK + "=" + yearId, null, null, null, null)) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        }
        return 0;
    }

    @Override
    protected MaliModel cursorToObject(Cursor cursor) {
        MaliModel model = new MaliModel();
        model.setYearId_FK(cursor.getInt(0));
        model.setId(cursor.getInt(1));
        model.setNum(cursor.getInt(2));
        model.setMaliType(MaliType.valueOf(cursor.getString(3)));
        model.setPersonBedId_FK(cursor.getInt(4));
        model.setPersonBesId_FK(cursor.getInt(5));
        model.setMaliDate(cursor.getString(6));
        model.setDescription(cursor.getString(7));
        model.setVcheckSarresidDate(cursor.getString(8));
        model.setVcheckBank(cursor.getString(9));
        model.setVcheckSerial(cursor.getString(10));
        model.setAmount(cursor.getLong(11));
        model.setSynced(cursor.getInt(12) == 1);
        model.setSyncDate(cursor.getString(13));
        model.setAtfNum(cursor.getInt(14));
        model.setLat(cursor.getDouble(15));
        model.setLon(cursor.getDouble(16));

        try {
            if (cursor.getString(17) != null)
                model.setCreateDate(Vars.iso8601Format.parse(cursor.getString(17)));
        } catch (ParseException e) {
            Log.e(TAG, "error on converting create_date column", e);
            throw new RuntimeException("error on converting create_date column");
        }

        return model;
    }

    @Override
    protected ContentValues objectToContentValue(MaliModel maliModel) {
        ContentValues cv = new ContentValues();
        cv.put(MaliTbl.COLUMN_YEAR_ID_FK, maliModel.getYearId_FK());
        cv.put(MaliTbl.COLUMN_ID, maliModel.getId());
        cv.put(MaliTbl.COLUMN_NUM, maliModel.getNum());
        cv.put(MaliTbl.COLUMN_TYPE, maliModel.getMaliType().getValue());
        cv.put(MaliTbl.COLUMN_PERSON_BED_ID_FK, maliModel.getPersonBedId_FK());
        cv.put(MaliTbl.COLUMN_PERSON_BES_ID_FK, maliModel.getPersonBesId_FK());
        cv.put(MaliTbl.COLUMN_DATE, maliModel.getMaliDate());
        cv.put(MaliTbl.COLUMN_DESCRIPTION, maliModel.getDescription());
        cv.put(MaliTbl.COLUMN_VCHECK_SARRESID_DATE, maliModel.getVcheckSarresidDate());
        cv.put(MaliTbl.COLUMN_VCHECK_BANK, maliModel.getVcheckBank());
        cv.put(MaliTbl.COLUMN_VCHECK_SERIAL, maliModel.getVcheckSerial());
        cv.put(MaliTbl.COLUMN_AMOUNT, maliModel.getAmount());
        cv.put(MaliTbl.COLUMN_SYNCED, maliModel.isSynced() ? 1 : 0);
        cv.put(MaliTbl.COLUMN_SYNC_DATE, maliModel.getSyncDate());
        cv.put(MaliTbl.COLUMN_ATF_NUM, maliModel.getAtfNum());
        cv.put(MaliTbl.COLUMN_LAT, maliModel.getLat());
        cv.put(MaliTbl.COLUMN_LON, maliModel.getLon());

        if (maliModel.getCreateDate() != null)
            cv.put(MaliTbl.COLUMN_CREATE_TIMESTAMP, maliModel.getCreateDateInIsoFormat());
        return cv;
    }
}
