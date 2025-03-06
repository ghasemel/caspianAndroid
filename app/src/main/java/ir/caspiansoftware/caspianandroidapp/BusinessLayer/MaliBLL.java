package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import info.elyasi.android.elyasilib.UI.MoveDirection;
import info.elyasi.android.elyasilib.Utility.ConvertExt;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.MaliDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.MaliDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.SPFaktorDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.MaliWebService;
import ir.caspiansoftware.caspianandroidapp.Enum.MaliType;
import ir.caspiansoftware.caspianandroidapp.GPSTracker;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/22/2016.
 */
public class MaliBLL extends ABusinessLayer {
    private static final String TAG = "MaliBLL";

    private MaliWebService maliWebService;

    private PersonBLL personBLL;

    public MaliBLL(Context context) {
        super(context);

        maliWebService = new MaliWebService();
        personBLL = new PersonBLL(mContext);
    }


    // region webservice
    public void sendmaliModelToServer(List<MaliModel> faktorList) throws Exception {
        Log.d(TAG, "syncmaliModel()");

        try {
            if (faktorList != null) {
                ResponseWebService response = maliWebService.uploadPFaktorList(
                        faktorList,
                        Vars.YEAR.getDataBase());

                if (response == null)
                    throw new Exception("responseWebService is null");

                // extract faktor inserted number on server side from response
                List<Integer> listAtfNums = ConvertExt.toList(response.getData());
                if (listAtfNums != null) {

                    // create MaliDataSource object

                    try (MaliDataSource maliModelDataSource = new MaliDataSource(mContext)) {
                        // index for faktor number list items
                        int index = 0;

                        // for every faktor that we send to server
                        for (MaliModel mp : faktorList) {
                            // update sync info
                            maliModelDataSource.updateSync(mp.getId(), listAtfNums.get(index++));
                        }

                    }
                    // close connection
                }
            }

        } finally {
            Log.d(TAG, "syncmaliModel(): end");
        }
    }
    // endregion webservice

    // region database
    public MaliModel getByMove(MoveDirection moveDirection, int currentId) throws Exception {
        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {

            MaliModel maliModel = switch (moveDirection) {
                case First -> maliDataSource.getFirstOrLast(true, Vars.YEAR.getId());
                case Previous -> maliDataSource.getPrevOrNext(currentId, true, Vars.YEAR.getId());
                case Next -> maliDataSource.getPrevOrNext(currentId, false, Vars.YEAR.getId());
                case Last -> maliDataSource.getFirstOrLast(false, Vars.YEAR.getId());
            };

            if (maliModel != null) {
                Log.d(TAG, "getByMove(): MaliModel id = " + maliModel.getId());

                setBedAndBesById(maliModel);

                return maliModel;
            }
        }
        return null;
    }

    private void setBedAndBesById(MaliModel maliModel) {
        PersonModel bed = personBLL.getById(maliModel.getPersonBedId_FK());
        PersonModel bes = personBLL.getById(maliModel.getPersonBedId_FK());
        setBedAndBes(maliModel, bed, bes);
    }

    private void setBedAndBesByCode(MaliModel maliModel, String bedCode, String besCode) {
        PersonModel bed = personBLL.getByCode(bedCode, Vars.YEAR.getId());
        PersonModel bes = personBLL.getByCode(besCode, Vars.YEAR.getId());
        setBedAndBes(maliModel, bed, bes);
    }

    private void setBedAndBes(MaliModel maliModel, PersonModel bed, PersonModel bes) {
        // bed
        if (bed == null)
            throw new RuntimeException(CaspianErrors.mali_bed_null);
        maliModel.setPersonBedModel(bed);
        maliModel.setPersonBedId_FK(bed.getId());

        // bes
        if (bes != null) {
            maliModel.setPersonBesModel(bes);
            maliModel.setPersonBesId_FK(bes.getId());
        }
    }

    public int getNewNum() {
        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {
            return maliDataSource.getMaxNum(Vars.YEAR.getId()) + 1;
        }
    }

    public MaliModel getMaliById(int id) {
        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {
            MaliModel maliModel = maliDataSource.getById(id);
            if (maliModel != null) {
                setBedAndBesById(maliModel);
                return maliModel;
            }
        }

        return null;
    }

    public MaliModel Save(int id, int num, MaliType maliType, String bedCode, String besCode, String maliDate, String description, String vcheckBank, String vcheckSarresidDate, String vcheckSerial, long amount, Activity activity, Date insertDate) throws Exception {

        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {
            if (num <= 0)
                throw new Exception(CaspianErrors.INVOICE_NUM_INVALID);

            if (maliDate.trim().equals(""))
                throw new Exception(CaspianErrors.DATE_INVALID);

            MaliModel maliModel = new MaliModel();
            maliModel.setYearId_FK(Vars.YEAR.getId());
            maliModel.setNum(num);
            maliModel.setMaliType(maliType);
            maliModel.setMaliDate(maliDate);
            maliModel.setDescription(description);
            maliModel.setVcheckSarresidDate(vcheckSarresidDate);
            maliModel.setVcheckBank(vcheckBank);
            maliModel.setVcheckSerial(vcheckSerial);
            maliModel.setAmount(amount);
            setBedAndBesByCode(maliModel, bedCode, besCode);

            // insert
            if (id <= 0) {
                // get location
                GPSTracker gps = new GPSTracker(activity);

                // gps is ON
                if (gps.canGetLocation()) {
                    Log.d(TAG, "lat: " + gps.getLatitude() + ", lon: " + gps.getLongitude());
                    if ((gps.getLongitude() == 0 || gps.getLatitude() == 0) && PermissionBLL.forceLocationSaving())
                        throw new Exception(CaspianErrors.location_not_available);

                    maliModel.setLat(gps.getLatitude());
                    maliModel.setLon(gps.getLongitude());
                } else {
                    if (PermissionBLL.forceLocationSaving())
                        throw new RuntimeException(CaspianErrors.gps_is_off);
                }

                maliModel.setCreateDate(insertDate);
                id = maliDataSource.insert(maliModel);
                if (id <= 0)
                    throw new Exception(CaspianErrors.SAVING_ERROR);
                maliModel.setId(id);

            } else { // update
                if (maliModel.getLon() == 0 || maliModel.getLat() == 0) {
                    // get location
                    GPSTracker gps = new GPSTracker(activity);

                    // gps is ON
                    if (gps.canGetLocation()) {
                        Log.d(TAG, "lat: " + gps.getLatitude() + ", lon: " + gps.getLongitude());
                        if ((gps.getLongitude() == 0 || gps.getLatitude() == 0) && PermissionBLL.forceLocationSaving())
                            throw new Exception(CaspianErrors.location_not_available);

                        maliModel.setLat(gps.getLatitude());
                        maliModel.setLon(gps.getLongitude());
                    } else {
                        if (PermissionBLL.forceLocationSaving())
                            throw new RuntimeException(CaspianErrors.gps_is_off);
                    }
                }

                maliModel.setId(id);
                maliDataSource.update(maliModel);
            }
            return maliModel;
        }
    }

    public int delete(MaliModel maliModelModel) throws Exception {

        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {
            if (maliModelModel != null && maliModelModel.getId() > 0) {
                return maliDataSource.delete(maliModelModel);
            }
            return -1;
        }
    }

    private ArrayList<MaliModel> fillMaliModelList(ArrayList<MaliModel> list) {
        if (list != null) {

            for (MaliModel maliModel : list) {
                // set bed
                PersonModel bed = personBLL.getById(maliModel.getPersonBesId_FK());
                if (bed == null)
                    throw new RuntimeException(CaspianErrors.person_not_exist + "#" + maliModel.getPersonBesId_FK());
                maliModel.setPersonBedModel(bed);

                // set bes
                if (maliModel.getPersonBesId_FK() != null) {
                    PersonModel bes = personBLL.getById(maliModel.getPersonBesId_FK());
                    maliModel.setPersonBesModel(bes);
                }
            }
            return list;
        }
        return null;
    }

    public ArrayList<MaliModel> getmaliModels() {

        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {
            if (Vars.YEAR.getId() > 0) {
                ArrayList<MaliModel> list = maliDataSource.getAllByYearId(Vars.YEAR.getId(), false);
                list = fillMaliModelList(list);
                return list;
            }
            return null;
        }
    }

    public ArrayList<MaliModel> getmaliModelsByLast() throws Exception {

        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {
            if (Vars.YEAR.getId() > 0) {
                ArrayList<MaliModel> list = maliDataSource.getAllByYearId(Vars.YEAR.getId(), true);
                list = fillMaliModelList(list);
                return list;
            }
            return null;
        }
    }

    public void updateSyncInfo(int maliModelId, int atfNum) {

        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {
            maliDataSource.updateSync(maliModelId, atfNum);

        }
    }
    // endregion database
}
