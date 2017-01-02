package info.elyasi.android.elyasilib.BLL;

import android.content.Context;

/**
 * Created by Canada on 2/11/2016.
 */
public abstract class ABusinessLayer {

    private static final String TAG = "ABusinessLayer";

    //protected IUICallBack mUICallBack;
    protected Context mContext;

    protected ABusinessLayer(Context pContext) { //Context pContext
        mContext = pContext;
        //mUICallBack = null;
    }


   /* protected ResponseBLL doOnSuccess(ResponseBLL responseBLL) {
        Log.d(TAG, "doOnSuccess - " + responseBLL.toString());

        return responseBLL;
    }

    protected ResponseBLL doOnError(String errorMessage, String caller) {
        Log.d(TAG, "doOnSuccess - caller: " + caller + ", errorMessage: " + errorMessage);

        //AErrorHandler errorExt = new AErrorHandler(mContext,  errorMessage);
        return new ResponseBLL<String>(Constant.ERROR, errorMessage, caller);
    }*/
}
