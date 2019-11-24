package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import info.elyasi.android.elyasilib.DownloadData;
import info.elyasi.android.elyasilib.Utility.DirectoryUtil;
import info.elyasi.android.elyasilib.Utility.ImageUtil;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.KalaPhotoDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.KalaWebService;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.ResponseToModel;
import ir.caspiansoftware.caspianandroidapp.Models.KalaPhotoModel;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaPhotoBLL extends ABusinessLayer {
    private static final String TAG = "KalaPhotoBLL";

    private KalaWebService mKalaWebService;

    public KalaPhotoBLL(Context context) {
        super(context);
        mKalaWebService = new KalaWebService();
    }

    // region webService ***************************************************************************
    public List<KalaPhotoModel> FetchKalaPhotosList(String dbName) throws Exception {
        Log.d(TAG, "FetchKalaPhotosList start");
        try {
            ResponseWebService responseWebService =
                    mKalaWebService.GetKalaPhotosList(dbName);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            return ResponseToModel.getKalaPhotosList(responseWebService.getData(), Vars.YEAR.getId());
        } finally {
            Log.d(TAG, "FetchKalaPhotosList finished");
        }
    }

    public Integer FetchKalaPhotosCount(String dbName) throws Exception {
        Log.d(TAG, "FetchKalaPhotosCount start");
        try {
            ResponseWebService responseWebService = mKalaWebService.GetKalaPhotosCount(dbName);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            return Integer.parseInt(responseWebService.getData());
        } finally {
            Log.d(TAG, "FetchKalaPhotosCount finished");
        }
    }

    public Bitmap downloadImage(KalaPhotoModel photo, String imageUrl) {
        if (photo == null)
            return null;

        return DownloadData.DownloadImageFromPath(imageUrl + Vars.YEAR.getDaftar() + "/kala/" + photo.getCode() + "/" + photo.getFileName());
    }

    public boolean saveImage(KalaPhotoModel photo, Bitmap bmp, Context context) {
        if (!DirectoryUtil.makeDirInAppFolder(context, photo.getImageDirPath()))
            return false;

        return ImageUtil.saveImage(bmp, photo.getImageFullPath());
    }
    // endregion webService ************************************************************************


    // region database *****************************************************************************
    public void SyncWithDatabase(KalaPhotoModel photo) {
        Log.d(TAG, "SyncWithDatabase start");

        if (photo == null)
            return;

        try (KalaPhotoDataSource dataSource = new KalaPhotoDataSource(mContext)) {

            Log.d(TAG, "check for insert or update");
            if (dataSource.isExistByCode(photo.getCode(), photo.getFileName(), photo.getYearIdFK())) {
                // update
                Log.d(TAG, "update " + photo.getCode());
                dataSource.update(photo);
            } else {
                // insert
                Log.d(TAG, "insert " + photo.getCode());
                int id = dataSource.insert(photo);
                photo.setId(id);
            }
        } finally {
            Log.d(TAG, "SyncWithDatabase finished");
        }
    }

    public void DeleteNotExistInList(List<KalaPhotoModel> photoList) {
        Log.d(TAG, "SyncWithDatabase start");

        if (photoList == null)
            return;

        try (KalaPhotoDataSource dataSource = new KalaPhotoDataSource(mContext)) {
            dataSource.deleteOther(photoList);
        } finally {
            Log.d(TAG, "SyncWithDatabase finished");
        }
    }

    public ArrayList<KalaPhotoModel> getKalaPhotoListByYearId(int yearId) {
        Log.d(TAG, "getKalaListByYearId(): function entered");


        try (KalaPhotoDataSource dataSource = new KalaPhotoDataSource(mContext)) {
            return dataSource.getKalaPhotosListByYearId(yearId);
        } finally {
            Log.d(TAG, "getKalaListByYearId() finished");
        }
    }

    public KalaPhotoModel getKalaById(int id) {
        Log.d(TAG, "getKalaById(): function entered");

        try (KalaPhotoDataSource dataSource = new KalaPhotoDataSource(mContext)) {
            return dataSource.getById(id);
        } finally {
            Log.d(TAG, "getKalaById() finished");
        }
    }
    // endregion database **************************************************************************
}