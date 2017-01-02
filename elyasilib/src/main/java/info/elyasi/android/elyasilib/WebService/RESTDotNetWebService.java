package info.elyasi.android.elyasilib.WebService;

import android.util.Log;

/**
 * Created by Canada on 12/18/2015.
 */
public abstract class RESTDotNetWebService extends REST_HMAC {
    private static final String TAG = "RESTDotNetWebService";

    protected abstract String getControllerName();

    protected RESTDotNetWebService(String ApiKEY, String AppID, String AuthenticationScheme, int TimeOut) {
        super(ApiKEY, AppID, AuthenticationScheme, TimeOut);
    }

    @Override
    protected ResponseWebService sendRequest(String ApiUrl, String method, final NameValue[] pParameters,
                                             boolean pParameterIsJSON, final RequestType requestType) throws Exception {
        Log.d(TAG, "sendRequest starting");

        ResponseWebService responseWebService = super.sendRequest(ApiUrl + getControllerName(), method, pParameters, pParameterIsJSON, requestType);

        if (responseWebService != null) {
            Log.d(TAG, responseWebService.toString());
        }

        Log.d(TAG, "sendRequest finished");

        return responseWebService;
    }
}