package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import android.util.Log;

import info.elyasi.android.elyasilib.WebService.NameValue;
import info.elyasi.android.elyasilib.WebService.RESTDotNetWebService;
import info.elyasi.android.elyasilib.WebService.RequestType;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;

/**
 * Created by Canada on 6/21/2016.
 */
public class PersonWebService extends RESTDotNetWebService {
    private static final String TAG = "PersonWebService";


    public PersonWebService() {
        super(SettingWebService.getApiKey(), SettingWebService.getDeviceId(), SettingWebService.AuthenticationScheme, SettingWebService.TIME_OUT);
    }

    @Override
    protected String getControllerName() {
        return "Person";
    }

    public ResponseWebService GetPersonList(String dbName) throws Exception {
        Log.d(TAG, "GetPersonList start");

        NameValue[] parameter = {
               // new NameValue<>("userId", String.valueOf(userId)),
                new NameValue<>("dbName", dbName)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetPersonList", parameter, true,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetPersonList finished");
        return responseWebService;
    }

    public ResponseWebService GetPersonCount(String dbName) throws Exception {
        Log.d(TAG, "GetPersonCount start");

        NameValue[] parameter = {
                //new NameValue<>("userId", String.valueOf(userId)),
                new NameValue<>("dbName", dbName)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetPersonCount", parameter, true,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetPersonCount finished");
        return responseWebService;
    }

    public ResponseWebService GetPersonByCode(String dbName, String code) throws Exception {
        Log.d(TAG, "GetPersonByCode start");

        NameValue[] parameter = {
                //new NameValue<>("userId", String.valueOf(userId)),
                new NameValue<>("dbName", dbName),
                new NameValue<>("code", code)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetPersonByCode", parameter, true,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetPersonByCode finished");
        return responseWebService;
    }

    public ResponseWebService GetPersonMandeByCode(String dbName, String code) throws Exception {
        Log.d(TAG, "GetPersonMandeByCode start");

        NameValue[] parameter = {
                //new NameValue<>("userId", String.valueOf(userId)),
                new NameValue<>("dbName", dbName),
                new NameValue<>("code", code)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetPersonMandeByCode", parameter, true,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetPersonMandeByCode finished");
        return responseWebService;
    }

    public ResponseWebService GetPersonLastSellByCodeKala(String dbName, String personCode, String kalaCode) throws Exception {
        Log.d(TAG, "GetPersonLastSellByCodeKala start");

        NameValue[] parameter = {
                //new NameValue<>("userId", String.valueOf(userId)),
                new NameValue<>("dbName", dbName),
                new NameValue<>("personCode", personCode),
                new NameValue<>("kalaCode", kalaCode)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetPersonLastSellPriceByKalaCode", parameter, true,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetPersonLastSellByCodeKala finished");
        return responseWebService;
    }
}
