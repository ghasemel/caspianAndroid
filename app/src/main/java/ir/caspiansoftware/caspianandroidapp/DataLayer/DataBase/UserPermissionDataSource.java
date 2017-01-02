package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import java.util.List;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.UserPermissionTbl;
import ir.caspiansoftware.caspianandroidapp.Models.UserPermissionModel;

/**
 * Created by Canada on 2/9/2016.
 */
public class UserPermissionDataSource extends ADataSource<UserPermissionModel> {

    public UserPermissionDataSource(Context pContext) {
        super(pContext);
    }

    public String[] getAllColumns() {
        return UserPermissionTbl.get().getColumns();
    }

    public int delete(UserPermissionModel pPermission) {
        return 0;
    }

    public int deleteAll(int userId) {
        return mDatabase.delete(UserPermissionTbl.TABLE_NAME,
                UserPermissionTbl.COLUMN_USER_ID_FK + "= ?",
                new String[] {
                        String.valueOf(userId)
                });
    }

    public int insert(UserPermissionModel userPermissionModel) {
        ContentValues cv = objectToContentValue(userPermissionModel);
        return (int) mDatabase.insert(UserPermissionTbl.TABLE_NAME, null, cv);
    }

    public int update(UserPermissionModel pPermission) {
        ContentValues cv = objectToContentValue(pPermission);
        return mDatabase.update(UserPermissionTbl.TABLE_NAME, cv,
                UserPermissionTbl.COLUMN_USER_ID_FK + "= ? AND " +
                        UserPermissionTbl.COLUMN_PART + "='?'",
                new String[]{
                        String.valueOf(pPermission.getUserId()),
                        pPermission.getPart()
                });
    }

    public List<UserPermissionModel> getAll() {
        return null;
    }

    public UserPermissionModel getById(int userId) {
        return null;
    }

    public List<UserPermissionModel> getAll(int userId) {
        Cursor cursor = mDatabase.query(UserPermissionTbl.TABLE_NAME, getAllColumns(),
                UserPermissionTbl.COLUMN_USER_ID_FK + "=" + userId,
                null, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            List<UserPermissionModel> userPermissionModelList = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                UserPermissionModel permission = cursorToObject(cursor);
                userPermissionModelList.add(permission);
                cursor.moveToNext();
            }
            return userPermissionModelList;
        }
        return null;
    }


    public UserPermissionModel getPermission(int pUserId, UserPermissionTbl.Parts pPart) {
        String partName = UserPermissionTbl.getPartName(pPart);
        if (partName != null) {
            Cursor cursor = mDatabase.query(UserPermissionTbl.TABLE_NAME, getAllColumns(),
                    UserPermissionTbl.COLUMN_USER_ID_FK + "=? AND " +
                            UserPermissionTbl.COLUMN_PART + "='?'",
                    new String[]{String.valueOf(pUserId), partName},
                    null, null, null, null);

            cursor.moveToFirst();
            if (!cursor.isAfterLast() && cursor.getCount() == 1) {
                return cursorToObject(cursor);
            }
        }
        return null;
    }


    protected UserPermissionModel cursorToObject(Cursor pCursor) {
        UserPermissionModel permission = new UserPermissionModel();
        permission.setUserId(pCursor.getInt(0));
        permission.setPart(pCursor.getString(1));
        permission.setWrite(pCursor.getInt(2) == 1);
        permission.setAccess(pCursor.getInt(3) == 1);
        return permission;
    }

    protected ContentValues objectToContentValue(UserPermissionModel pPermission) {
        ContentValues cv = new ContentValues();
        cv.put(UserPermissionTbl.COLUMN_USER_ID_FK, pPermission.getUserId());
        cv.put(UserPermissionTbl.COLUMN_PART, pPermission.getPart());
        cv.put(UserPermissionTbl.COLUMN_WRITE, pPermission.isWrite() ? 1 : 0);
        cv.put(UserPermissionTbl.COLUMN_ACCESS, pPermission.isAccess() ? 1 : 0);
        return cv;
    }
}
