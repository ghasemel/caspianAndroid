package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.InitialSettingTbl;
import ir.caspiansoftware.caspianandroidapp.Models.InitialSettingModel;

/**
 * Created by Canada on 6/4/2016.
 */
public class InitialSettingDataSource extends ADataSource<InitialSettingModel> {

    public InitialSettingDataSource(Context context) {
        super(context);
    }

    public String[] getAllColumns() {
        return InitialSettingTbl.get().getColumns();
    }

    public int delete(InitialSettingModel initialSettingModel) {
        return mDatabase.delete(InitialSettingTbl.TABLE_NAME,
                InitialSettingTbl.COLUMN_SETTING_NAME + "=" + initialSettingModel.getName(), null);
    }

    public int insert(InitialSettingModel initialSettingModel) {
        ContentValues cv = objectToContentValue(initialSettingModel);
        return (int) mDatabase.insert(InitialSettingTbl.TABLE_NAME, null, cv);
    }

    public int update(InitialSettingModel initialSettingModel) {
        ContentValues cv = objectToContentValue(initialSettingModel);
        return mDatabase.update(InitialSettingTbl.TABLE_NAME, cv,
                InitialSettingTbl.COLUMN_SETTING_NAME + "='" + initialSettingModel.getName() + "'", null);
    }

    public List<InitialSettingModel> getAll() {
        Cursor cursor = mDatabase.query(InitialSettingTbl.TABLE_NAME, getAllColumns(), null, null, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            List<InitialSettingModel> settingList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                InitialSettingModel setting = cursorToObject(cursor);
                settingList.add(setting);
                cursor.moveToNext();
            }
            return settingList;
        }
        return null;
    }

    public int getDbVersion() {
        return mDatabase.getVersion();
    }

    public int getCount() {
        Cursor cursor = mDatabase.query(InitialSettingTbl.TABLE_NAME, getAllColumns(), null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getCount();
    }

    public InitialSettingModel getById(int settingName) {
        throw new UnsupportedOperationException();
    }

    public InitialSettingModel getByName(String settingName) {
        Cursor cursor = mDatabase.query(InitialSettingTbl.TABLE_NAME, getAllColumns(),
                InitialSettingTbl.COLUMN_SETTING_NAME + " ='" + settingName + "'", null, null, null, null);

        InitialSettingModel settingModel = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            settingModel = cursorToObject(cursor);
        }
        return settingModel;
    }

    protected ContentValues objectToContentValue(InitialSettingModel initialSettingModel) {
        ContentValues cv = new ContentValues();
        cv.put(InitialSettingTbl.COLUMN_SETTING_NAME, initialSettingModel.getName());
        cv.put(InitialSettingTbl.COLUMN_SETTING_VALUE, initialSettingModel.getValue());
        return cv;
    }
    protected InitialSettingModel cursorToObject(Cursor cursor) {
        InitialSettingModel setting = new InitialSettingModel();
        setting.setName(cursor.getString(0));
        setting.setValue(cursor.getString(1));
        return setting;
    }
}
