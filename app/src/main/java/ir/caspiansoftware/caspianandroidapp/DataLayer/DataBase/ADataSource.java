package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import info.elyasi.android.elyasilib.DataBase.IDataSource;

/**
 * Created by Canada on 1/18/2016.
 */
public abstract class ADataSource<T> implements IDataSource<T>, AutoCloseable {

    protected CaspianDataBaseHelper mDataBaseHelper;
    protected SQLiteDatabase mDatabase;

    protected ADataSource(Context context) {
        mDataBaseHelper = new CaspianDataBaseHelper(context);
    }

    protected void open() {
        mDatabase = mDataBaseHelper.getWritableDatabase();

        if (!mDatabase.isReadOnly()) {
            // Enable foreign key constraints
            mDatabase.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void close() {
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDataBaseHelper.close();
        }
    }

    protected abstract ContentValues objectToContentValue(T TObject);
    protected abstract T cursorToObject(Cursor cursor);
}
