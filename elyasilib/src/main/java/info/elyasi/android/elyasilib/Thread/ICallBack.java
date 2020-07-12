package info.elyasi.android.elyasilib.Thread;

import org.json.JSONObject;

/**
 * Created by Canada on 1/5/2016.
 */
public interface ICallBack {
    void callback(String caller, int pResponseCode, String pResponseData);
    //void onMyFragmentCallBack(String caller, int pResponseCode, int pResponseData);
    //void onMyFragmentCallBack(String caller, int pResponseCode, JSONObject pResponseData);

    void onError(String caller, int pErrorCode, String pErrorMessage);
    void onSuccess(String caller, int pResponseCode, String pResponseData);
}
