package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import android.util.Log;

import info.elyasi.android.elyasilib.WebService.NameValue;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import info.elyasi.android.elyasilib.WebService.RESTDotNetWebService;
import info.elyasi.android.elyasilib.Security.Cryptography;
import info.elyasi.android.elyasilib.WebService.RequestType;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;

/**
 * Created by Canada on 2/11/2016.
 */
public class UserWebService extends RESTDotNetWebService {
    private static final String TAG = "UserWebService";


    public UserWebService()  {
        super(SettingWebService.getApiKey(), SettingWebService.getDeviceId(), SettingWebService.AUTHENTICATION_SCHEME, SettingWebService.TIME_OUT);
        //setResponseCallBack();
    }

    @Override
    protected String getControllerName() {
        return "user";
    }

    public ResponseWebService Login(String userName, String pass) throws Exception {
        Log.d(TAG, "login start");

        pass = Cryptography.md5(pass);

        NameValue[] parameter = {
                new NameValue<>("userName", userName),
                new NameValue<>("pass", pass)
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "Login", parameter, true,
                RequestType.create(RequestType.RType.POST));

        Log.d(TAG, "login finished");
        return responseWebService;
    }

    public ResponseWebService ShouldLogout(int userId) throws Exception {
        Log.d(TAG, "checkForLogout start");

        NameValue[] parameter = {
            new NameValue<>("userId", String.valueOf(userId))
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "ShouldLogout", parameter, false,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "checkForLogout finished");
        return responseWebService;
    }

    public ResponseWebService SetShouldLogout(int userId, boolean shouldLogout) throws Exception {
        Log.d(TAG, "SetShouldLogout start");

        NameValue[] parameter = {
            new NameValue<>("userId", String.valueOf(userId)),
            new NameValue<>("shouldLogout", String.valueOf(shouldLogout))
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "SetShouldLogout", parameter, false,
                RequestType.create(RequestType.RType.PUT_URL));

        Log.d(TAG, "SetShouldLogout finished");
        return responseWebService;
    }

    public ResponseWebService GetPermissions(int userId) throws Exception {
        Log.d(TAG, "GetPermissions start");

        NameValue[] parameter = {
            new NameValue<>("userId", String.valueOf(userId))
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetPermissions", parameter, false,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetPermissions finished");
        return responseWebService;
    }


}
