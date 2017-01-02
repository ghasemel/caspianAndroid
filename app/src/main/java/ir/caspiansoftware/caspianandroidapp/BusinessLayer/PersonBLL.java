package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.PersonDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.PersonWebService;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.ResponseToModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonLastSellInfoModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 6/21/2016.
 */
public class PersonBLL extends ABusinessLayer {
    private static final String TAG = "PersonBLL";

    private PersonWebService mPersonWebService;

    public PersonBLL(Context context) {
        super(context);
        mPersonWebService = new PersonWebService();
    }

    // region webService ***************************************************************************
    public List<PersonModel> FetchPersonList(String dbName) throws Exception
    {
        Log.d(TAG, "FetchPersonList start");
        try {
            ResponseWebService responseWebService =
                    mPersonWebService.GetPersonList(dbName);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            return ResponseToModel.getPersonList(responseWebService.getData(), Vars.YEAR.getId());

        } finally {
            Log.d(TAG, "FetchPersonList finished");
        }
    }

    public Integer FetchPersonListCount(String dbName) throws Exception
    {
        Log.d(TAG, "FetchPersonListCount start");
        try {
            ResponseWebService responseWebService =
                    mPersonWebService.GetPersonCount(dbName);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            return Integer.parseInt(responseWebService.getData());
        } finally {
            Log.d(TAG, "FetchPersonListCount finished");
        }
    }

    public double FetchPersonMande(String dbName, String code) throws Exception
    {
        Log.d(TAG, "FetchPersonMande start");
        try {
            ResponseWebService responseWebService =
                    mPersonWebService.GetPersonMandeByCode(dbName, code);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            return Double.parseDouble(responseWebService.getData());

        } finally {
            Log.d(TAG, "FetchPersonMande finished");
        }
    }

    public PersonModel FetchPersonByCode(String dbName, String code) throws Exception
    {
        Log.d(TAG, "FetchPersonByCode start");
        try {
            ResponseWebService responseWebService =
                    mPersonWebService.GetPersonByCode(dbName, code);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            //return new ResponseBLL<>(Constant.SUCCESS, Integer.parseInt(responseWebService.getData()), "FetchKalaListCount");
            return ResponseToModel.getPerson(responseWebService.getData());
        } finally {
            Log.d(TAG, "FetchPersonByCode finished");
        }
    }

    public PersonLastSellInfoModel FetchPersonLastSellInfo(String personCode, String kalaCode) throws Exception
    {
        Log.d(TAG, "FetchPersonLastSellInfo start");
        try {
            ResponseWebService responseWebService =
                    mPersonWebService.GetPersonLastSellByCodeKala(Vars.YEAR.getDataBase(), personCode, kalaCode);

            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            return ResponseToModel.getPersonLastSellInfo(responseWebService.getData());

        } finally {
            Log.d(TAG, "FetchPersonLastSellInfo finished");
        }
    }
    // endregion webService ************************************************************************


    // region database *****************************************************************************
    public void SyncWithDatabase(PersonModel person) {
        Log.d(TAG, "SyncWithDatabase start");

        PersonDataSource dataSource = new PersonDataSource(mContext);

        try {
            if (person == null)
                return;


         //ي
            //person.setCode(PersianConvert.ConvertDigitsToPersian(person.getCode()));
            //person.setName(PersianConvert.ConvertDigitsToPersian(person.getName()));
            //person.setAddress(PersianConvert.ConvertDigitsToPersian(person.getAddress()));
            person.setName(person.getName().replace("ي", "ی"));

            dataSource.open();
            if (dataSource.isExistByCode(person.getCode())) {
                // update
                Log.d(TAG, "update " + person.getCode());
                dataSource.update(person);
            } else {
                // insert
                int id = dataSource.insert(person);
                person.setId(id);
                Log.d(TAG, "insert " + person.getCode());
            }
        } finally {
            dataSource.close();
            Log.d(TAG, "SyncWithDatabase finished");
        }
    }


    public void DeleteNotExistInList(List<PersonModel> personList) {
        Log.d(TAG, "SyncWithDatabase start");

        PersonDataSource dataSource = new PersonDataSource(mContext);

        try {
            if (personList == null)
                return;

            dataSource.open();
            dataSource.deleteOther(personList);
        } finally {
            dataSource.close();
            Log.d(TAG, "SyncWithDatabase finished");
        }
    }


    public ArrayList<PersonModel> getPersonListByYearId(int yearId) {
        Log.d(TAG, "getPersonListByYearId(): function entered");

        PersonDataSource dataSource = new PersonDataSource(mContext);
        try {
            dataSource.open();
            return dataSource.getPersonListByYearId(yearId);
        } finally {
            dataSource.close();
            Log.d(TAG, "getPersonListByYearId() finished");
        }
    }

    public PersonModel getByCode(String code) throws Exception {
        PersonDataSource personDataSource = new PersonDataSource(mContext);
        try {
            if (code.trim().equals(""))
                throw new Exception(CaspianErrors.CUSTOMER_INVALID);

            personDataSource.open();
            return personDataSource.getByCode(code);
        } finally {
            personDataSource.close();
        }
    }


    public PersonModel getById(int id) {
        PersonDataSource personDataSource = new PersonDataSource(mContext);
        try {

            personDataSource.open();
            return personDataSource.getById(id);
        } finally {
            personDataSource.close();
        }
    }
    // endregion database **************************************************************************
}
