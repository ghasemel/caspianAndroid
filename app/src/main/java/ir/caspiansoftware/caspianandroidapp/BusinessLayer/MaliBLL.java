package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import info.elyasi.android.elyasilib.UI.MoveDirection;
import info.elyasi.android.elyasilib.Utility.ConvertExt;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.MPFaktorDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.MaliDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.SPFaktorDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.MaliWebService;
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


    public MaliBLL(Context context) {
        super(context);

        maliWebService = new MaliWebService();
    }


    // region webservice
    public void sendMPFaktorToServer(List<MaliModel> faktorList) throws Exception {
        Log.d(TAG, "syncMPFaktor()");

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

                    // create MPFaktorDataSource object
                    MPFaktorDataSource mpFaktorDataSource = new MPFaktorDataSource(mContext);

                    try {
                        // open connection
                        mpFaktorDataSource.open();

                        // index for faktor number list items
                        int index = 0;

                        // for every faktor that we send to server
                        for (MaliModel mp : faktorList) {
                            // update sync info
                            mpFaktorDataSource.updateSync(mp.getId(), listAtfNums.get(index++));
                        }

                    } finally {
                        // close connection
                        mpFaktorDataSource.close();
                    }
                }
            }

        } finally {
            Log.d(TAG, "syncMPFaktor(): end");
        }
    }
    // endregion webservice

    // region database
    public MaliModel getByMove(MoveDirection moveDirection, int currentId) throws Exception {
        MaliDataSource maliDataSource = new MaliDataSource(mContext);
        try {
            maliDataSource.open();

            MaliModel maliModel = null;
            switch (moveDirection) {
                case First:
                    maliModel = maliDataSource.getFirstOrLast(true, Vars.YEAR.getId());
                    break;

                case Previous:
                    maliModel = maliDataSource.getPrevOrNext(currentId, true, Vars.YEAR.getId());
                    break;

                case Next:
                    maliModel = maliDataSource.getPrevOrNext(currentId, false, Vars.YEAR.getId());
                    break;

                case Last:
                    maliModel = maliDataSource.getFirstOrLast(false, Vars.YEAR.getId());
                    break;
            }

            if (maliModel != null) {
                Log.d(TAG, "getByMove(): MaliModel id = " + maliModel.getId());

                PersonBLL personBLL = new PersonBLL(mContext);
                PersonModel personModel = personBLL.getById(maliModel.getPersonId_FK());
                if (personModel == null)
                    throw new Exception(CaspianErrors.CUSTOMER_INVALID);

                maliModel.setPersonModel(personModel);

                return maliModel;
            }
        } finally {
            maliDataSource.close();
        }
        return null;
    }

    public int getNewNum() {
        MPFaktorDataSource mpFaktorDataSource = new MPFaktorDataSource(mContext);
        try {
            mpFaktorDataSource.open();
            return mpFaktorDataSource.getMaxNum(Vars.YEAR.getId()) + 1;
        } finally {
            mpFaktorDataSource.close();
        }
    }

    public ArrayList<SPFaktorModel> getSPfaktorListByMPFaktorId(int mpFaktorId) {
        try (SPFaktorDataSource spFaktorDataSource = new SPFaktorDataSource(mContext)) {
            spFaktorDataSource.open();
            ArrayList<SPFaktorModel> spFaktorList = spFaktorDataSource.getByMPFaktorId(mpFaktorId);
            if (spFaktorList != null) {
                KalaBLL kalaBLL = new KalaBLL(mContext);

                for (SPFaktorModel sp : spFaktorList) {
                    KalaModel kalaModel = kalaBLL.getKalaById(sp.getKalaId_FK());
                    sp.setKalaModel(kalaModel);
                }
            }
            return spFaktorList;
        }
    }

    public MaliModel getMPfaktorById(int id) {
        try (MaliDataSource maliDataSource = new MaliDataSource(mContext)) {
            maliDataSource.open();
            MaliModel maliModel = maliDataSource.getById(id);
            if (maliModel != null) {
                PersonBLL personBLL = new PersonBLL(mContext);
                PersonModel personModel = personBLL.getById(maliModel.getPersonId_FK());
                if (personModel == null)
                    throw new RuntimeException(CaspianErrors.CUSTOMER_INVALID);
                maliModel.setPersonModel(personModel);
                return maliModel;
            }
        }

        return null;
    }

    public SPFaktorModel getSPfaktorById(int id) throws Exception {
        try (SPFaktorDataSource spFaktorDataSource = new SPFaktorDataSource(mContext)) {
            spFaktorDataSource.open();
            SPFaktorModel spFaktorModel = spFaktorDataSource.getById(id);
            if (spFaktorModel != null) {
                KalaBLL kalaBLL = new KalaBLL(mContext);
                KalaModel kalaModel = kalaBLL.getKalaById(spFaktorModel.getKalaId_FK());
                spFaktorModel.setKalaModel(kalaModel);
            }
            return spFaktorModel;
        }
    }

    public void saveSPFaktorList(int mpFaktorId, List<SPFaktorModel> spFaktorModelList) throws Exception {
        if (spFaktorModelList != null) {
            try (SPFaktorDataSource spFaktorDataSource = new SPFaktorDataSource(mContext)) {
                if (mpFaktorId <= 0)
                    throw new Exception(CaspianErrors.invoice_id_invalid);

                spFaktorDataSource.open();
                for (SPFaktorModel spFaktor : spFaktorModelList) {
                    if (spFaktorDataSource.isExistById(spFaktor.getId())) {
                        // update
                        spFaktorDataSource.update(spFaktor);

                    } else {
                        // insert
                        spFaktor.setMPFaktorId_FK(mpFaktorId);

                        int id = spFaktorDataSource.insert(spFaktor);
                        if (id > 0) {
                            spFaktor.setId(id);
                        } else {
                            throw new Exception(CaspianErrors.SAVING_ERROR);
                        }
                    }
                }

                // delete removed rows from database
                int del = spFaktorDataSource.deleteOther(spFaktorModelList);
                Log.d(TAG, "removed count: " + del);
            }
        }
    }

    public MaliModel Save(int id, int num, String date, String customer_code, String description, Activity activity, Date insertDate) throws Exception {
        MaliDataSource maliDataSource = new MaliDataSource(mContext);

        try {
            if (num <= 0)
                throw new Exception(CaspianErrors.INVOICE_NUM_INVALID);

            if (date.trim().equals(""))
                throw new Exception(CaspianErrors.DATE_INVALID);

            PersonBLL personBLL = new PersonBLL(mContext);
            PersonModel personModel = personBLL.getByCode(customer_code, Vars.YEAR.getId());
            if (personModel == null)
                throw new Exception(CaspianErrors.CUSTOMER_INVALID);

            MaliModel maliModel = new MaliModel();
            maliModel.setYearId_FK(Vars.YEAR.getId());
            maliModel.setNum(num);
            maliModel.setDate(date);
            maliModel.setPersonModel(personModel);
            maliModel.setDescription(description);
            maliDataSource.open();



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
        } finally {
            maliDataSource.close();
        }
    }

    public int deleteSPFaktorByMPFaktorId(int mpFaktorId) {
        SPFaktorDataSource spFaktorDataSource = new SPFaktorDataSource(mContext);

        try {
            spFaktorDataSource.open();
            if (mpFaktorId > 0) {
                return spFaktorDataSource.deleteByMPFaktorId(mpFaktorId);
            }
            return -1;
        } finally {
            spFaktorDataSource.close();
        }
    }

    public int delete(MaliModel mpFaktorModel) throws Exception {
        MaliDataSource maliDataSource = new MaliDataSource(mContext);

        try {
            maliDataSource.open();
            if (mpFaktorModel != null && mpFaktorModel.getId() > 0) {
                deleteSPFaktorByMPFaktorId(mpFaktorModel.getId());
                return maliDataSource.delete(mpFaktorModel);
            }
            return -1;
        } finally {
            maliDataSource.close();
        }
    }

    private ArrayList<MaliModel> fillMPFaktorList(ArrayList<MaliModel> list) {
        if (list != null) {
            PersonBLL personBLL = new PersonBLL(mContext);

            for (MaliModel mpFaktor : list) {
                PersonModel person = personBLL.getById(mpFaktor.getPersonId_FK());

                if (person == null)
                    throw new RuntimeException(CaspianErrors.person_not_exist + "#" + mpFaktor.getPersonId_FK());

                mpFaktor.setPersonModel(person);
            }
            return list;
        }
        return null;
    }

    public ArrayList<MaliModel> getMPFaktors() {
        MaliDataSource maliDataSource = new MaliDataSource(mContext);

        try {
            maliDataSource.open();
            if (Vars.YEAR.getId() > 0) {
                ArrayList<MaliModel> list = maliDataSource.getAllByYearId(Vars.YEAR.getId(), false);
                list = fillMPFaktorList(list);
                return list;
            }
            return null;
        } finally {
            maliDataSource.close();
        }
    }

    public ArrayList<MaliModel> getMPFaktorsByLast() throws Exception {
        MaliDataSource maliDataSource = new MaliDataSource(mContext);

        try {
            maliDataSource.open();
            if (Vars.YEAR.getId() > 0) {
                ArrayList<MaliModel> list = maliDataSource.getAllByYearId(Vars.YEAR.getId(), true);
                list = fillMPFaktorList(list);
                return list;
            }
            return null;
        } finally {
            maliDataSource.close();
        }
    }

    public void updateSyncInfo(int mpFaktorId, int atfNum) {
        MaliDataSource maliDataSource = new MaliDataSource(mContext);

        try {
            maliDataSource.open();
            maliDataSource.updateSync(mpFaktorId, atfNum);

        } finally {
            maliDataSource.close();
        }
    }
    // endregion database
}
