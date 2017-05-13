package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import android.util.Log;

import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import info.elyasi.android.elyasilib.WebService.NameValue;
import info.elyasi.android.elyasilib.WebService.RESTDotNetWebService;
import info.elyasi.android.elyasilib.WebService.RequestType;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;

/**
 * Created by Canada on 3/6/2016.
 */
public class YearMaliWebService extends RESTDotNetWebService {
    private static final String TAG = "YearMaliWebService";

    public YearMaliWebService() {
        super(SettingWebService.getApiKey(), SettingWebService.getDeviceId(), SettingWebService.AuthenticationScheme, SettingWebService.TIME_OUT);
    }

    protected String getControllerName() {
        return "YearMali";
    }

    public ResponseWebService GetYearMali(int userId) throws Exception {
        Log.d(TAG, "GetYearMali start");

        NameValue[] parameter = {
                new NameValue<>("userId", String.valueOf(userId))
        };

        ResponseWebService responseWebService = sendRequest(SettingWebService.getAPI_URL(), "GetYearMali", parameter, false,
                RequestType.create(RequestType.RType.GET));

        Log.d(TAG, "GetYearMali finished");
        return responseWebService;
    }

    public ResponseWebService GetDore(String dbName) throws Exception {
        Log.d(TAG, "GetDore(): start");

        NameValue[] parameter = {
            new NameValue<>("dbName", dbName)
        };

        ResponseWebService responseWebService =
                sendRequest(
                        SettingWebService.getAPI_URL(),
                        "GetDore",
                        parameter,
                        true,
                        RequestType.create(RequestType.RType.GET)
                );

        Log.d(TAG, "GetDore(): end");
        return responseWebService;
    }
}
