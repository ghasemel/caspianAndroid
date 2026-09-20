package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.MPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.MaliTbl;
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

        if (personList == null || personList.isEmpty())
            return 0;

        return mDatabase.delete(PersonTbl.TABLE_NAME,
                buildDeleteOtherWhere(personList.size()),
                buildDeleteOtherArgs(personList));
    }

    /**
     * WHERE clause for deleteOther: keep every person the server still sends,
     * and every person the phone's own rows still point at.
     *
     * A person is referenced by three foreign keys, all ON DELETE NO ACTION,
     * so deleting a referenced row raises SQLITE_CONSTRAINT_FOREIGNKEY (787):
     * mpfaktor.personId_FK, mali.personBedId_FK and mali.personBesdId_FK.
     * The mali pair was missed when that form was added, which is what threw
     * 787 on customers who use it.
     *
     * NOT EXISTS rather than NOT IN because NOT IN yields no rows at all when
     * the subquery contains a NULL, and mali rows routinely leave one of the
     * two person columns empty - which would silently stop the delete working.
     *
     * Split out from deleteOther, and package-visible, so it can be unit
     * tested: the module has no Robolectric, so SQLiteDatabase itself cannot
     * be exercised in a JVM test.
     */
    static String buildDeleteOtherWhere(int codeCount) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < codeCount; i++) {
            if (i > 0)
                placeholders.append(",");
            placeholders.append("?");
        }

        return PersonTbl.COLUMN_YEAR_ID_FK + " = ? AND " +
                // one placeholder per code: binding the whole comma-joined list
                // as a single argument compares against the literal "a,b,c" and
                // therefore never matches, marking every person for deletion.
                PersonTbl.COLUMN_CODE + " NOT IN(" + placeholders + ") AND " +
                "NOT EXISTS(SELECT 1 FROM " + MPFaktorTbl.TABLE_NAME +
                " WHERE " + MPFaktorTbl.TABLE_NAME + "." + MPFaktorTbl.COLUMN_PERSON_ID_FK +
                " = " + PersonTbl.TABLE_NAME + "." + PersonTbl.COLUMN_ID + ") AND " +
                "NOT EXISTS(SELECT 1 FROM " + MaliTbl.TABLE_NAME +
                " WHERE " + MaliTbl.TABLE_NAME + "." + MaliTbl.COLUMN_PERSON_BED_ID_FK +
                " = " + PersonTbl.TABLE_NAME + "." + PersonTbl.COLUMN_ID +
                " OR " + MaliTbl.TABLE_NAME + "." + MaliTbl.COLUMN_PERSON_BES_ID_FK +
                " = " + PersonTbl.TABLE_NAME + "." + PersonTbl.COLUMN_ID + ")";
    }

    /** Args for {@link #buildDeleteOtherWhere}: the year id, then one code per person. */
    static String[] buildDeleteOtherArgs(List<PersonModel> personList) {
        String[] args = new String[personList.size() + 1];
        args[0] = String.valueOf(personList.get(0).getYearId_FK());
        for (int i = 0; i < personList.size(); i++)
            args[i + 1] = personList.get(i).getCode();
        return args;
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
        return mDatabase.update(PersonTbl.TABLE_NAME, cv,
                PersonTbl.COLUMN_YEAR_ID_FK + "=" + person.getYearId_FK() + " AND " +
                PersonTbl.COLUMN_CODE + "='" + person.getCode() + "'",
                null);
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

    public PersonModel getByCode(String code, int yearId) {
        Cursor cursor = mDatabase.query(PersonTbl.TABLE_NAME, getAllColumns(),
                PersonTbl.COLUMN_YEAR_ID_FK + "=" + yearId + " AND " +
                PersonTbl.COLUMN_CODE + " = '" + code + "'", null, null, null, null);

        PersonModel person = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            person = cursorToObject(cursor);
        }
        return person;
    }

    public boolean isExistByCode(String code, int yearId) {
        Cursor cursor = null;
        try {
            cursor = mDatabase.query(PersonTbl.TABLE_NAME, getAllColumns(),
                    PersonTbl.COLUMN_YEAR_ID_FK + "=" + yearId + " AND " +
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

