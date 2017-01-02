package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.YearMaliDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.ResponseToModel;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.YearMaliWebService;
import ir.caspiansoftware.caspianandroidapp.Models.YearMaliModel;
import info.elyasi.android.elyasilib.Constant;

/**
 * Created by Canada on 3/6/2016.
 */
public class YearMaliBLL extends ABusinessLayer {
    private static final String TAG = "YearMaliModel";
    private YearMaliWebService mYearMaliWebService;

    public YearMaliBLL(Context context) {
        super(context);
        mYearMaliWebService = new YearMaliWebService();
    }


    public ArrayList<YearMaliModel> getYearMali(int userId) throws Exception {
        Log.d(TAG, "getYearMali start");

        try {
            ResponseWebService responseWebService = mYearMaliWebService.GetYearMali(userId);
            if (responseWebService == null)
                throw new Exception("empty response");

            //return doOnSuccess(new ResponseWebService(Constant.SUCCESS, responseWebService.getData(), "getYearMali"));
            return ResponseToModel.getYearMaliList(responseWebService.getData());
        //} catch (Exception ex) {
        //    return doOnError(ex.getMessage(), "getYearMali");
        } finally {
            Log.d(TAG, "getYearMali finished");
        }
    }


    public long saveYearMali(YearMaliModel yearMali) throws Exception {
        Log.d(TAG, "saveYearMali start");

        YearMaliDataSource yearMaliDataSource = new YearMaliDataSource(mContext);
        try {
            yearMaliDataSource.open();
            if (yearMali.isCurrent()) {
                yearMaliDataSource.updateAllUnCurrent();
            }


            YearMaliModel oldYear = yearMaliDataSource.getByUserIdDatabase(yearMali.getUserId(), yearMali.getDataBase());
            if (oldYear != null) {
                yearMali.setId(oldYear.getId());
                return yearMaliDataSource.update(yearMali);
            } else {
                int insertId = (int) yearMaliDataSource.insert(yearMali);
                yearMali.setId(insertId);
                return insertId;
            }
            //yearMaliDataSource.delete(yearMali);
        } catch (SQLiteException ex) {
            Log.d(TAG, ex.getMessage());
            throw ex;
        } finally {
            yearMaliDataSource.close();
            Log.d(TAG, "saveYearMali end");
        }
    }

    public YearMaliModel getCurrentYearMali(int userId) {
        Log.d(TAG, "getCurrentYearMali start");

        YearMaliDataSource yearMaliDataSource = new YearMaliDataSource(mContext);
        try {
            yearMaliDataSource.open();
            return yearMaliDataSource.getCurrentYearByUserId(userId);
        } finally {
            yearMaliDataSource.close();
            Log.d(TAG, "getCurrentYearMali end");
        }
    }
}
