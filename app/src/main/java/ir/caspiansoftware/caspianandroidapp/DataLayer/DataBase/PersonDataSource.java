package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.MPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.PersonTbl;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;

/**
 * Created by Canada on 6/21/2016.
 */
public class PersonDataSource extends ADataSource<PersonModel> {
    private static final String TAG = "PersonDataSource";


    public PersonDataSource(Context context) {
        super(context);
    }

    @Override
    public String[] getAllColumns() {
        return PersonTbl.get().getColumns();
    }

    @Override
    public int delete(PersonModel person) {
        return mDatabase.delete(PersonTbl.TABLE_NAME,
                PersonTbl.COLUMN_ID + "= ?",
                new String[] {
                        String.valueOf(person.getId())
                });
    }

    public int deleteOther(List<PersonModel> personList) {
        Log.d(TAG, "deleteOther()");

        StringBuilder codes = new StringBuilder();
        for (PersonModel kala : personList) {
            codes.append(kala.getCode());
            codes.append(",");
        }


        if (codes.length() > 0) {
            return mDatabase.delete(PersonTbl.TABLE_NAME,
                    PersonTbl.COLUMN_YEAR_ID_FK + " = " + personList.get(0).getYearId_FK() + " AND " +
                    PersonTbl.COLUMN_CODE + " NOT IN(?) AND " +
                    PersonTbl.COLUMN_ID + " NOT IN(SELECT " + MPFaktorTbl.COLUMN_PERSON_ID_FK + " FROM " + MPFaktorTbl.TABLE_NAME + ")",
                    new String[] { codes.substring(0, codes.length() - 1) });
        }
        return 0;
    }

    @Override
    public int insert(PersonModel person) {
        ContentValues cv = objectToContentValue(person);
        cv.remove(PersonTbl.COLUMN_ID);
        return (int) mDatabase.insert(PersonTbl.TABLE_NAME, null, cv);
    }

    @Override
    public int update(PersonModel person) {
        ContentValues cv = objectToContentValue(person);
        cv.remove(PersonTbl.COLUMN_ID);
        return mDatabase.update(PersonTbl.TABLE_NAME, cv, PersonTbl.COLUMN_CODE + "=" + person.getCode(), null);
    }

    @Override
    public List<PersonModel> getAll() {
        Cursor cursor = mDatabase.query(PersonTbl.TABLE_NAME, getAllColumns(), null,
                null, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            List<PersonModel> PersonModelList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                PersonModel person = cursorToObject(cursor);
                PersonModelList.add(person);
                cursor.moveToNext();
            }
            return PersonModelList;
        }
        return null;
    }

    public ArrayList<PersonModel> getPersonListByYearId(int yearId) {
        Log.d(TAG, "getPersonListByYearId(): function entered");

        Cursor cursor = mDatabase.query(PersonTbl.TABLE_NAME, getAllColumns(), PersonTbl.COLUMN_YEAR_ID_FK + " = " + yearId, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            ArrayList<PersonModel> personModelList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                personModelList.add(cursorToObject(cursor));
                cursor.moveToNext();
            }
            return personModelList;
        }
        return null;
    }


    @Override
    public PersonModel getById(int id) {
        Cursor cursor = mDatabase.query(PersonTbl.TABLE_NAME, getAllColumns(),
                PersonTbl.COLUMN_ID + " = " + id, null, null, null, null);

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

    public PersonModel getByCode(String code) {
        Cursor cursor = mDatabase.query(PersonTbl.TABLE_NAME, getAllColumns(),
                PersonTbl.COLUMN_CODE + " = '" + code + "'", null, null, null, null);

        PersonModel person = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            person = cursorToObject(cursor);
        }
        return person;
    }

    public boolean isExistByCode(String code) {
        Cursor cursor = null;
        try {
            cursor = mDatabase.query(PersonTbl.TABLE_NAME, getAllColumns(),
                    PersonTbl.COLUMN_CODE + " = '" + code + "'", null, null, null, null);

            cursor.moveToFirst();
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    @Override
    protected ContentValues objectToContentValue(PersonModel person) {
        ContentValues cv = new ContentValues();
        cv.put(PersonTbl.COLUMN_YEAR_ID_FK, person.getYearId_FK());
        cv.put(PersonTbl.COLUMN_ID, person.getId());
        cv.put(PersonTbl.COLUMN_CODE, person.getCode());
        cv.put(PersonTbl.COLUMN_NAME, person.getName());
        cv.put(PersonTbl.COLUMN_MANDE, person.getMande());
        cv.put(PersonTbl.COLUMN_TEL, person.getTel());
        cv.put(PersonTbl.COLUMN_MOBILE, person.getMobile());
        cv.put(PersonTbl.COLUMN_ADDRESS, person.getAddress());
        return cv;
    }

    @Override
    protected PersonModel cursorToObject(Cursor cursor) {
        PersonModel person = new PersonModel();
        person.setYearId_FK(cursor.getInt(0));
        person.setId(cursor.getInt(1));
        person.setCode(cursor.getString(2));
        person.setName(cursor.getString(3));
        person.setMande(cursor.getDouble(4));
        person.setTel(cursor.getString(5));
        person.setMobile(cursor.getString(6));
        person.setAddress(cursor.getString(7));
        return person;
    }
}

