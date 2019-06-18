package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import info.elyasi.android.elyasilib.UI.MoveDirection;
import info.elyasi.android.elyasilib.Utility.ConvertExt;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.MPFaktorDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.SPFaktorDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.PFaktorWebService;
import ir.caspiansoftware.caspianandroidapp.GPSTracker;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/22/2016.
 */
public class PFaktorBLL extends ABusinessLayer {
    private static final String TAG = "PFaktorBLL";

    private PFaktorWebService mPFaktorWebService;

    public PFaktorBLL(Context context) {
        super(context);

        mPFaktorWebService = new PFaktorWebService();
    }


    // region webservice
    public void sendMPFaktorToServer(List<MPFaktorModel> faktorList) throws Exception {
        Log.d(TAG, "syncMPFaktor()");

        try {
            if (faktorList != null) {
                ResponseWebService response = mPFaktorWebService.uploadPFaktorList(
                        faktorList,
                        Vars.YEAR.getDataBase());

                if (response == null)
                    throw new Exception("responseWebService is null");

                // extract faktor inserted number on server side from response
                List<Integer> listAtfNums = ConvertExt.toList(response.getData());
                if (listAtfNums != null) {

                    // create MPFaktorDataSource object
                    MPFaktorDataSource mpFaktorDataSource =
                            new MPFaktorDataSource(mContext);

                    try {
                        // open connection
                        mpFaktorDataSource.open();

                        // index for faktor number list items
                        int index = 0;

                        // for every faktor that we send to server
                        for (MPFaktorModel mp : faktorList) {
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
    public MPFaktorModel getByMove(MoveDirection moveDirection, int currentId) throws Exception {
        MPFaktorDataSource mpFaktorDataSource = new MPFaktorDataSource(mContext);
        try {
            mpFaktorDataSource.open();

            MPFaktorModel mpFaktorModel = null;
            switch (moveDirection) {
                case First:
                    mpFaktorModel = mpFaktorDataSource.getFirstOrLast(true, Vars.YEAR.getId());
                    break;

                case Previous:
                    mpFaktorModel = mpFaktorDataSource.getPrevOrNext(currentId, true, Vars.YEAR.getId());
                    break;

                case Next:
                    mpFaktorModel = mpFaktorDataSource.getPrevOrNext(currentId, false, Vars.YEAR.getId());
                    break;

                case Last:
                    mpFaktorModel = mpFaktorDataSource.getFirstOrLast(false, Vars.YEAR.getId());
                    break;
            }

            if (mpFaktorModel != null) {
                Log.d(TAG, "getByMove(): MPFaktorModel id = " + mpFaktorModel.getId());
                mpFaktorModel.setSPFaktorList(
                        getSPfaktorListByMPFaktorId(mpFaktorModel.getId())
                );

                PersonBLL personBLL = new PersonBLL(mContext);
                PersonModel personModel = personBLL.getById(mpFaktorModel.getPersonId_FK());
                if (personModel == null)
                    throw new Exception(CaspianErrors.CUSTOMER_INVALID);

                mpFaktorModel.setPersonModel(personModel);

                return mpFaktorModel;
            }
        } finally {
            mpFaktorDataSource.close();
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
        SPFaktorDataSource spFaktorDataSource = new SPFaktorDataSource(mContext);
        try {
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
        } finally {
            spFaktorDataSource.close();
        }
    }

    public SPFaktorModel getSPfaktorById(int id) throws Exception {
        SPFaktorDataSource spFaktorDataSource = new SPFaktorDataSource(mContext);
        try {
            spFaktorDataSource.open();
            SPFaktorModel spFaktorModel = spFaktorDataSource.getById(id);
            if (spFaktorModel != null) {
                KalaBLL kalaBLL = new KalaBLL(mContext);
                KalaModel kalaModel = kalaBLL.getKalaById(spFaktorModel.getKalaId_FK());
                spFaktorModel.setKalaModel(kalaModel);
            }
            return spFaktorModel;
        } finally {
            spFaktorDataSource.close();
        }
    }

    public void saveSPFaktorList(int mpFaktorId, List<SPFaktorModel> spFaktorModelList) throws Exception {
        if (spFaktorModelList != null) {
            SPFaktorDataSource spFaktorDataSource = new SPFaktorDataSource(mContext);
            try {
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
            } finally {
                spFaktorDataSource.close();
            }
        }
    }

    public MPFaktorModel Save(int id, int num, String date, String customer_code, String description, List<SPFaktorModel> spFaktorModelList) throws Exception {
        MPFaktorDataSource mpFaktorDataSource = new MPFaktorDataSource(mContext);

        try {
            if (num <= 0)
                throw new Exception(CaspianErrors.INVOICE_NUM_INVALID);

            if (date.trim().equals(""))
                throw new Exception(CaspianErrors.DATE_INVALID);

            PersonBLL personBLL = new PersonBLL(mContext);
            PersonModel personModel = personBLL.getByCode(customer_code, Vars.YEAR.getId());
            if (personModel == null)
                throw new Exception(CaspianErrors.CUSTOMER_INVALID);

            MPFaktorModel mpFaktorModel = new MPFaktorModel();
            mpFaktorModel.setYearId_FK(Vars.YEAR.getId());
            mpFaktorModel.setNum(num);
            mpFaktorModel.setDate(date);
            mpFaktorModel.setPersonModel(personModel);
            mpFaktorModel.setDescription(description);

            mpFaktorDataSource.open();

            // insert
            if (id <= 0) {
                // get location
                GPSTracker gps = new GPSTracker(mContext);

                // gps is ON
                if (gps.canGetLocation()) {
                    Log.d(TAG, "lat: " + gps.getLatitude() + ", lon: " + gps.getLongitude());
                    if ((gps.getLongitude() == 0 || gps.getLatitude() == 0) && PermissionBLL.forceLocationSaving())
                        throw new Exception(CaspianErrors.location_not_available);

                    mpFaktorModel.setLat(gps.getLatitude());
                    mpFaktorModel.setLon(gps.getLongitude());
                } else {
                    if (PermissionBLL.forceLocationSaving())
                        throw new RuntimeException(CaspianErrors.gps_is_off);
                }

                id = mpFaktorDataSource.insert(mpFaktorModel);
                if (id <= 0)
                    throw new Exception(CaspianErrors.SAVING_ERROR);
                mpFaktorModel.setId(id);

            } else { // update
                if (mpFaktorModel.getLon() == 0 || mpFaktorModel.getLat() == 0) {
                    // get location
                    GPSTracker gps = new GPSTracker(mContext);

                    // gps is ON
                    if (gps.canGetLocation()) {
                        Log.d(TAG, "lat: " + gps.getLatitude() + ", lon: " + gps.getLongitude());
                        if ((gps.getLongitude() == 0 || gps.getLatitude() == 0) && PermissionBLL.forceLocationSaving())
                            throw new Exception(CaspianErrors.location_not_available);

                        mpFaktorModel.setLat(gps.getLatitude());
                        mpFaktorModel.setLon(gps.getLongitude());
                    } else {
                        if (PermissionBLL.forceLocationSaving())
                            throw new RuntimeException(CaspianErrors.gps_is_off);
                    }
                }

                mpFaktorModel.setId(id);
                mpFaktorDataSource.update(mpFaktorModel);
            }

            saveSPFaktorList(id, spFaktorModelList);

            return mpFaktorModel;
        } finally {
            mpFaktorDataSource.close();
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

    public int delete(MPFaktorModel mpFaktorModel) throws Exception {
        MPFaktorDataSource mpFaktorDataSource = new MPFaktorDataSource(mContext);

        try {
            mpFaktorDataSource.open();
            if (mpFaktorModel != null && mpFaktorModel.getId() > 0) {
                deleteSPFaktorByMPFaktorId(mpFaktorModel.getId());
                return mpFaktorDataSource.delete(mpFaktorModel);
            }
            return -1;
        } finally {
            mpFaktorDataSource.close();
        }
    }

    private ArrayList<MPFaktorModel> fillMPFaktorList(ArrayList<MPFaktorModel> list) {
        if (list != null) {
            PersonBLL personBLL = new PersonBLL(mContext);

            for (MPFaktorModel mpFaktor : list) {
                PersonModel person = personBLL.getById(mpFaktor.getPersonId_FK());

                if (person == null)
                    throw new RuntimeException(CaspianErrors.person_not_exist + "#" + mpFaktor.getPersonId_FK());

                mpFaktor.setPersonModel(person);
            }
            return list;
        }
        return null;
    }

    public ArrayList<MPFaktorModel> getMPFaktors() {
        MPFaktorDataSource mpFaktorDataSource = new MPFaktorDataSource(mContext);

        try {
            mpFaktorDataSource.open();
            if (Vars.YEAR.getId() > 0) {
                ArrayList<MPFaktorModel> list = mpFaktorDataSource.getAllByYearId(Vars.YEAR.getId(), false);
                list = fillMPFaktorList(list);
                return list;
            }
            return null;
        } finally {
            mpFaktorDataSource.close();
        }
    }

    public ArrayList<MPFaktorModel> getMPFaktorsByLast() throws Exception {
        MPFaktorDataSource mpFaktorDataSource = new MPFaktorDataSource(mContext);

        try {
            mpFaktorDataSource.open();
            if (Vars.YEAR.getId() > 0) {
                ArrayList<MPFaktorModel> list = mpFaktorDataSource.getAllByYearId(Vars.YEAR.getId(), true);
                list = fillMPFaktorList(list);
                return list;
            }
            return null;
        } finally {
            mpFaktorDataSource.close();
        }
    }

    public void updateSyncInfo(int mpFaktorId, int atfNum) {
        MPFaktorDataSource mpFaktorDataSource = new MPFaktorDataSource(mContext);

        try {
            mpFaktorDataSource.open();
            mpFaktorDataSource.updateSync(mpFaktorId, atfNum);

        } finally {
            mpFaktorDataSource.close();
        }
    }
    // endregion database
}
