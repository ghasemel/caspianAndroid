package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import android.util.Log;

import info.elyasi.android.elyasilib.WebService.NameValue;
import info.elyasi.android.elyasilib.WebService.RESTDotNetWebService;
import info.elyasi.android.elyasilib.WebService.RequestType;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaWebService extends RESTDotNetWebService {
    private static final String TAG = "KalaWebService";


    public KalaWebService() {
        super(SettingWebService.getApiKey(), SettingWebService.getDeviceId(), SettingWebService.AuthenticationScheme, SettingWebService.TIME_OUT);
    }

    @Override
    protected String getControllerName() {
        return "Kala";
    }

    public ResponseWebService GetKalaList(String dbName) throws Exception {
        Log.d(TAG, "GetKalaList start");

        NameValue[] parameter = {
            //new NameValue<>("userId", String.valueOf(userId)),
            new NameValue<>("dbName", dbName)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetKalaList", parameter, true,
                        RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetKalaList finished");
        return responseWebService;
    }

    public ResponseWebService GetKalaCount(String dbName) throws Exception {
        Log.d(TAG, "GetKalaCount start");

        NameValue[] parameter = {
                //new NameValue<>("userId", String.valueOf(userId)),
                new NameValue<>("dbName", dbName)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetKalaCount", parameter, false,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetKalaCount finished");
        return responseWebService;
    }

    public ResponseWebService GetKalaMojoodiByCode(String dbName, String code) throws Exception {
        Log.d(TAG, "GetKalaMojoodiByCode start");

        NameValue[] parameter = {
                //new NameValue<>("userId", String.valueOf(userId)),
                new NameValue<>("dbName", dbName),
                new NameValue<>("code", code)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetKalaMojoodiByCode", parameter, true,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetKalaMojoodiByCode finished");
        return responseWebService;
    }
}
