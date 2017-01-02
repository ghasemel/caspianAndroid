package info.elyasi.android.elyasilib.WebService;

import android.content.Context;

import info.elyasi.android.elyasilib.Constant;
import info.elyasi.android.elyasilib.Thread.ICallBack;

/**
 * Created by Canada on 1/6/2016.
 */
public abstract class WebServiceCallBack implements ICallBack {

    protected Context mContext;

    protected WebServiceCallBack(Context pContext) {
        mContext = pContext;
    }

    public final void callback(String caller, int pResponseCode, String pResponseData) {
        if (pResponseCode == Constant.ERROR) {
            onError(caller, Constant.ERROR, pResponseData);
        } else {
            onSuccess(caller, pResponseCode, pResponseData);
        }
    }

    public abstract void onError(String caller, int errorCode, String errorMessage);
    public abstract void onSuccess(String caller, int responseCode, String responseData);

}
