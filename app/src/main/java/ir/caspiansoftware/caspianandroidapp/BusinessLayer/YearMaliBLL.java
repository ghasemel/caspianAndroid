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
import ir.caspiansoftware.caspianandroidapp.Vars;

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


    public ArrayList<YearMaliModel> FetchYearMali(int userId) throws Exception {
        Log.d(TAG, "FetchYearMali start");

        try {
            ResponseWebService responseWebService = mYearMaliWebService.GetYearMali(userId);
            if (responseWebService == null)
                throw new Exception("empty response");

            //return doOnSuccess(new ResponseWebService(Constant.SUCCESS, responseWebService.getData(), "FetchYearMali"));
            return ResponseToModel.getYearMaliList(responseWebService.getData());
        //} catch (Exception ex) {
        //    return doOnError(ex.getMessage(), "FetchYearMali");
        } finally {
            Log.d(TAG, "FetchYearMali finished");
        }
    }


    public long saveYearMali(YearMaliModel yearMali) {
        Log.d(TAG, "saveYearMali start");

        try (YearMaliDataSource yearMaliDataSource = new YearMaliDataSource(mContext)) {
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
            Log.d(TAG, "saveYearMali end");
        }
    }

    public YearMaliModel getCurrentYearMali(int userId) {
        Log.d(TAG, "getCurrentYearMali start");

        try (YearMaliDataSource yearMaliDataSource = new YearMaliDataSource(mContext)) {
            YearMaliModel year = yearMaliDataSource.getCurrentYearByUserId(userId);

            if (year != null &&
                    (
                            year.getDoreModel().getStartDore() == null || year.getDoreModel().getStartDore().isEmpty() ||
                                    year.getDoreModel().getEndDore() == null || year.getDoreModel().getEndDore().isEmpty()
                    )
            )
                year = null;

            return year;
        } finally {
            Log.d(TAG, "getCurrentYearMali end");
        }
    }


    public void FetchDore() throws Exception {
        Log.d(TAG, "FetchDore(): start");

        try {
            ResponseWebService responseWebService = mYearMaliWebService.GetDore(Vars.YEAR.getDataBase());
            if (responseWebService == null)
                throw new Exception("empty response");

            //Vars.YEAR.s

            //return doOnSuccess(new ResponseWebService(Constant.SUCCESS, responseWebService.getData(), "FetchYearMali"));
            //return ResponseToModel.getYearMaliList(responseWebService.getData());
            //} catch (Exception ex) {
            //    return doOnError(ex.getMessage(), "FetchYearMali");
        } finally {
            Log.d(TAG, "FetchYearMali finished");
        }
    }
}
