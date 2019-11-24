package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.InitialSettingTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.KalaPhotoTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.KalaTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.MPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.PersonTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.SPFaktorTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.UserTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.UserPermissionTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Tables.YearMaliTbl;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.Views.MPFaktorView;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Setting;

/**
 * Created by Canada on 1/18/2016.
 */
public class CaspianDataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "CaspianDataBaseHelper";

    private Context mContext;

    public CaspianDataBaseHelper(Context context) {
        super(context, Setting.CASPIAN_DB, null, Setting.APP_DB_VERSION);

        mContext = context;
    }

//    @Override
//    public void onOpen(SQLiteDatabase db) {
//        Log.d(TAG, "onOpen(): dbVersion = " + db.getVersion());
//        if (db.getVersion() != Setting.APP_DB_VERSION)
//            db.needUpgrade(Setting.APP_DB_VERSION);
//
//        super.onOpen(db);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            // create tables here
            String sql = UserTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = UserPermissionTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = YearMaliTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = InitialSettingTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = KalaTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = KalaTbl.get().createIndexesSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = PersonTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = PersonTbl.get().createIndexesSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = MPFaktorTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = MPFaktorTbl.get().createIndexesSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = SPFaktorTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = MPFaktorView.get().createViewSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

            sql = KalaPhotoTbl.get().createTableSql();
            Log.d(TAG, sql);
            db.execSQL(sql);

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement scheme changes and data message here when upgrading
        Log.d(TAG, "Database onUpgrade(): oldVersion = " + oldVersion + ", newVersion=" + newVersion);


        try {
            db.beginTransaction();

            // select right upgrade way
            switch (oldVersion) {
                case 1:
                    execSQLFile(mContext, db, R.raw.dore_mali_sql);

                case 2:
                    execSQLFile(mContext, db, R.raw.kala_photo_sql);
            }

            // commit
            db.setTransactionSuccessful();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());

        } finally {
            // commit or rollback
            db.endTransaction();
        }



        //DropDataBase(mContext);
        //throw new RuntimeException(CaspianErrors.database_need_drop);
    }

    public static void DropDataBase(Context pContext) {
        pContext.deleteDatabase(Setting.CASPIAN_DB);
    }


    private static int execSQLFile(Context context, SQLiteDatabase db, int resourceId) throws IOException {
        // Reseting Counter
        int result = 0;

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        String sql = "";
        while (insertReader.ready()) {
            sql += insertReader.readLine();
            if (!sql.trim().isEmpty() && !sql.startsWith("--") && sql.contains(";")) {
                db.execSQL(sql);
                result++;
                sql = "";
            }
        }

        insertReader.close();

        // returning number of inserted rows
        return result;
    }
}
