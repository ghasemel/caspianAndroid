package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.YearMaliTbl;
import ir.caspiansoftware.caspianandroidapp.Models.DoreModel;
import ir.caspiansoftware.caspianandroidapp.Models.YearMaliModel;

/**
 * Created by Canada on 3/5/2016.
 */
public class YearMaliDataSource extends ADataSource<YearMaliModel> {

    public YearMaliDataSource(Context context) {
        super(context);
    }

    public String[] getAllColumns() {
        return YearMaliTbl.get().getColumns();
    }

    public int delete(YearMaliModel yearMali) {
        return mDatabase.delete(
                YearMaliTbl.TABLE_NAME,
                YearMaliTbl.COLUMN_YEAR_ID + "= ?",
                new String[] { String.valueOf(yearMali.getId()) });
    }

    public int insert(YearMaliModel yearMali) {
        ContentValues cv = objectToContentValue(yearMali);
        cv.remove(YearMaliTbl.COLUMN_YEAR_ID);

        return (int) mDatabase.insertOrThrow(YearMaliTbl.TABLE_NAME, null, cv);
    }

    public int update(YearMaliModel yearMali) {
        //throw new Exception("Not implemented");
        ContentValues cv = objectToContentValue(yearMali);
        cv.remove(YearMaliTbl.COLUMN_YEAR_ID);

        return mDatabase.update(YearMaliTbl.TABLE_NAME, cv,
                YearMaliTbl.COLUMN_YEAR_ID  + " = " + yearMali.getId(), null);
    }

    public void updateAllUnCurrent() {
        mDatabase.execSQL("UPDATE " + YearMaliTbl.TABLE_NAME +
                " SET " + YearMaliTbl.COLUMN_IS_CURRENT + "= 0");
    }

    public List<YearMaliModel> getAll() {
        return null;
    }

    public YearMaliModel getById(int yearId) {
        Cursor cursor = mDatabase.query(YearMaliTbl.TABLE_NAME, getAllColumns(),
                YearMaliTbl.COLUMN_YEAR_ID + "=" + yearId, null, null, null, null);

        YearMaliModel yearMali = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            yearMali = cursorToObject(cursor);
        }
        return yearMali;
    }

    public YearMaliModel getByUserIdDatabase(int userId, String database) {
        Cursor cursor = mDatabase.query(YearMaliTbl.TABLE_NAME, getAllColumns(),
                YearMaliTbl.COLUMN_USER_ID_FK + "=" + userId + " AND " +
                YearMaliTbl.COLUMN_DATABASE + "='" + database + "'", null, null, null, null);

        YearMaliModel yearMali = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            yearMali = cursorToObject(cursor);
        }
        return yearMali;
    }

    public YearMaliModel getCurrentYearByUserId(int userId) {
        Cursor cursor = mDatabase.query(YearMaliTbl.TABLE_NAME, getAllColumns(),
                YearMaliTbl.COLUMN_USER_ID_FK + "=" + userId + " AND " +
                       YearMaliTbl.COLUMN_IS_CURRENT + " = 1" , null, null, null, null);

        YearMaliModel yearMali = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            yearMali = cursorToObject(cursor);
        }
        return yearMali;
    }

    protected ContentValues objectToContentValue(YearMaliModel yearMali) {
        ContentValues cv = new ContentValues();
        cv.put(YearMaliTbl.COLUMN_YEAR_ID, yearMali.getId());
        cv.put(YearMaliTbl.COLUMN_USER_ID_FK, yearMali.getUserId());
        cv.put(YearMaliTbl.COLUMN_DAFTAR, yearMali.getDaftar());
        cv.put(YearMaliTbl.COLUMN_YEAR, yearMali.getYear());
        cv.put(YearMaliTbl.COLUMN_COMPANY, yearMali.getCompany());
        cv.put(YearMaliTbl.COLUMN_DATABASE, yearMali.getDataBase());
        cv.put(YearMaliTbl.COLUMN_IS_CURRENT, yearMali.isCurrent());
        cv.put(YearMaliTbl.COLUMN_DORE_START, yearMali.getDoreModel().getStartDore());
        cv.put(YearMaliTbl.COLUMN_DORE_END, yearMali.getDoreModel().getEndDore());
        return cv;
    }

    protected YearMaliModel cursorToObject(Cursor cursor) {
        YearMaliModel yearMali = new YearMaliModel();
        yearMali.setId(cursor.getInt(0));
        yearMali.setUserId(cursor.getInt(1));
        yearMali.setDaftar(cursor.getInt(2));
        yearMali.setYear(cursor.getInt(3));
        yearMali.setCompany(cursor.getString(4));
        yearMali.setDataBase(cursor.getString(5));
        yearMali.setCurrent(cursor.getInt(6) == 1);

        DoreModel dore = new DoreModel();
        dore.setYear(yearMali.getYear());
        dore.setDaftar(yearMali.getDaftar());
        dore.setStartDore(cursor.getString(7));
        dore.setEndDore(cursor.getString(8));

        yearMali.setDoreModel(dore);
        return yearMali;
    }
}
