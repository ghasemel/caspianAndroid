package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.KalaDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.KalaWebService;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.ResponseToModel;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Setting;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaBLL extends ABusinessLayer {
    private static final String TAG = "KalaBLL";

    private KalaWebService mKalaWebService;

    public KalaBLL(Context context) {
        super(context);
        mKalaWebService = new KalaWebService();
    }

    // region webService ***************************************************************************
    public List<KalaModel> FetchKalaList(String dbName) throws Exception
    {
        Log.d(TAG, "FetchKalaList start");
        try {
            ResponseWebService responseWebService =
                    mKalaWebService.GetKalaList(dbName);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            //List<KalaModel> kalaList = ResponseToModel.getKalaList(responseWebService.getData());

           // SyncWithDatabase(kalaList);

            //return new ResponseBLL<>(Constant.SUCCESS, kalaList, "FetchKalaList"); //doOnSuccess(new ResponseWebService(Constant.SUCCESS, Constant.OK, "FetchKalaList"));
            return ResponseToModel.getKalaList(responseWebService.getData(), Vars.YEAR.getId());
        }
        //catch (Exception ex) {
        //    return doOnError(ex.getMessage(), "FetchKalaList");
       // }
        finally {
            Log.d(TAG, "FetchKalaList finished");
        }
    }

    public Integer FetchKalaListCount(String dbName) throws Exception
    {
        Log.d(TAG, "FetchKalaListCount start");
        try {
            ResponseWebService responseWebService =
                    mKalaWebService.GetKalaCount(dbName);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            //return new ResponseBLL<>(Constant.SUCCESS, Integer.parseInt(responseWebService.getData()), "FetchKalaListCount");
            return Integer.parseInt(responseWebService.getData());
        }
        //catch (Exception ex) {
        //    return doOnError(ex.getMessage(), "FetchKalaListCount");
        //}
        finally {
            Log.d(TAG, "FetchKalaListCount finished");
        }
    }

    public double FetchKalaMojoodi(String dbName, String code) throws Exception
    {
        Log.d(TAG, "FetchPersonMande start");
        try {
            ResponseWebService responseWebService =
                    mKalaWebService.GetKalaMojoodiByCode(dbName, code);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            return Double.parseDouble(responseWebService.getData());

        } finally {
            Log.d(TAG, "FetchPersonMande finished");
        }
    }
    // endregion webService ************************************************************************


    // region database *****************************************************************************
    public void SyncWithDatabase(KalaModel kala) {
        Log.d(TAG, "SyncWithDatabase start");

        KalaDataSource dataSource = new KalaDataSource(mContext);

        try {
            if (kala == null)
                return;

            //kala.setCode(PersianConvert.ConvertDigitsToPersian(kala.getCode()));
            //kala.setName(PersianConvert.ConvertDigitsToPersian(kala.getName()));
            kala.setName(kala.getName().replace("ي", "ی"));

            dataSource.open();
            Log.d(TAG, "check for insert or update");
            if (dataSource.isExistByCode(kala.getCode())) {
                // update
                Log.d(TAG, "update " + kala.getCode());
                dataSource.update(kala);
            } else {
                // insert
                Log.d(TAG, "insert " + kala.getCode());
                int id = dataSource.insert(kala);
                kala.setID(id);
            }
            //}
        } finally {
            dataSource.close();
            Log.d(TAG, "SyncWithDatabase finished");
        }
    }

    public void DeleteNotExistInList(List<KalaModel> kalaList) {
        Log.d(TAG, "SyncWithDatabase start");

        KalaDataSource dataSource = new KalaDataSource(mContext);

        try {
            if (kalaList == null)
                return;

            dataSource.open();
            dataSource.deleteOther(kalaList);
        } finally {
            dataSource.close();
            Log.d(TAG, "SyncWithDatabase finished");
        }
    }

    public ArrayList<KalaModel> getKalaListByYearId(int yearId) {
        Log.d(TAG, "getKalaListByYearId(): function entered");

        KalaDataSource dataSource = new KalaDataSource(mContext);
        try {
            dataSource.open();
            return dataSource.getKalaListByYearId(yearId);
        } finally {
            dataSource.close();
            Log.d(TAG, "getKalaListByYearId() finished");
        }
    }

    public KalaModel getKalaById(int id) {
        Log.d(TAG, "getKalaById(): function entered");

        KalaDataSource dataSource = new KalaDataSource(mContext);
        try {
            dataSource.open();
            return dataSource.getById(id);
        } finally {
            dataSource.close();
            Log.d(TAG, "getKalaById() finished");
        }
    }
    // endregion database **************************************************************************
}