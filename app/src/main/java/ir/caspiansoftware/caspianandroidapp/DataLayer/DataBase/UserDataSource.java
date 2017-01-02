package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.UserTbl;
import ir.caspiansoftware.caspianandroidapp.Models.UserModel;
import info.elyasi.android.elyasilib.Utility.DateExt;

/**
 * Created by Canada on 1/18/2016.
 */
public class UserDataSource extends ADataSource<UserModel> {

    public UserDataSource(Context context) {
        super(context);
    }

    public String[] getAllColumns() {
        return UserTbl.get().getColumns();
    }

    public int delete(UserModel pUserModel) {
        return 0;
    }

    public int insert(UserModel pUserModel) {
        ContentValues cv = objectToContentValue(pUserModel);
        return (int) mDatabase.insert(UserTbl.TABLE_NAME, null, cv);
    }

    public int update(UserModel pUserModel) {
        ContentValues cv = objectToContentValue(pUserModel);
        return mDatabase.update(UserTbl.TABLE_NAME, cv, UserTbl.COLUMN_USER_ID + "=" + pUserModel.getUserId(), null);
    }

    public void setOtherUsersLogout(int pUserId) {
        mDatabase.execSQL("UPDATE " + UserTbl.TABLE_NAME +
                        " SET " + UserTbl.COLUMN_IS_LOGON + "=0 " +
                        " WHERE " + UserTbl.COLUMN_USER_ID + " <> " + pUserId);

    }

    public List<UserModel> getAll() {
        return null;
    }

    public UserModel getById(int userId) {
        Cursor cursor = mDatabase.query(UserTbl.TABLE_NAME, getAllColumns(),
                UserTbl.COLUMN_USER_ID + " = " + userId, null, null, null, null);

        UserModel userModel = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            userModel = cursorToObject(cursor);
        }
        return userModel;
    }

    public UserModel getLogonUser() {
        Cursor cursor = mDatabase.query(UserTbl.TABLE_NAME, getAllColumns(),
                UserTbl.COLUMN_IS_LOGON + "=1", null, null, null, null);
        UserModel userModel = null;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            if (cursor.getCount() == 1) {
                userModel = cursorToObject(cursor);
            } else if (cursor.getCount() > 1) {
                // logout all other users
                userModel = cursorToObject(cursor);
                setOtherUsersLogout(userModel.getUserId());
                userModel = null;
            }
        }
        return userModel;
    }

    public void logout(UserModel pUserModel) {
        pUserModel.setLogon(false);
        update(pUserModel);
    }

    protected UserModel cursorToObject(Cursor cursor) {
        UserModel userModel = new UserModel();
        userModel.setUserId(cursor.getInt(0));
        userModel.setUserName(cursor.getString(1));
        userModel.setLastLoginDate(DateExt.getDate(cursor.getLong(2)));
        userModel.setLogon(cursor.getInt(3) == 1);
        userModel.setName(cursor.getString(4));
        userModel.setActive(cursor.getInt(5) == 1);
        userModel.setVisitorCode(cursor.getInt(6));
        userModel.setKalaPriceType(cursor.getString(7).charAt(0));
        return userModel;
    }

    protected ContentValues objectToContentValue(UserModel pUserModel) {
        ContentValues cv = new ContentValues();
        cv.put(UserTbl.COLUMN_USER_ID, pUserModel.getUserId());
        cv.put(UserTbl.COLUMN_USERNAME, pUserModel.getUserName());
        cv.put(UserTbl.COLUMN_LAST_LOGIN, pUserModel.getLastLoginDate().getTime());
        cv.put(UserTbl.COLUMN_IS_LOGON, pUserModel.isLogon() ? 1 : 0);
        cv.put(UserTbl.COLUMN_NAME, pUserModel.getName());
        cv.put(UserTbl.COLUMN_IS_ACTIVE, pUserModel.isActive() ? 1 : 0);
        cv.put(UserTbl.COLUMN_VISITOR_CODE, pUserModel.getVisitorCode());
        cv.put(UserTbl.COLUMN_KALA_PRICE_TYPE, String.valueOf(pUserModel.getKalaPriceType()));
        return cv;
    }
}
