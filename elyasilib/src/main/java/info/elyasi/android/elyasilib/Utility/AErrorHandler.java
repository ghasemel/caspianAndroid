package info.elyasi.android.elyasilib.Utility;

import android.content.Context;

import info.elyasi.android.elyasilib.Models.ErrorModel;
import info.elyasi.android.elyasilib.R;


/**
 * Created by Canada on 2/8/2016.
 */
public abstract class AErrorHandler {
    protected abstract ErrorModel ConvertToErrorModel(String pSystemMessage);

    private ErrorModel mErrorModel;
    //protected int mErrorCode;
    //protected String mMessage;
    protected Context mContext;
    protected String mOccurredErrorMessage;
    protected Exception mException;


    protected AErrorHandler(Context pContext) {
        mContext = pContext;
    }

    public void setException(Exception ex) {
        mException = ex;
        mOccurredErrorMessage = extractExceptionDetail(ex);
        mErrorModel = ConvertToErrorModel(mOccurredErrorMessage);
    }

    public String getUserMessage() {
        return this.toString();
    }


    @Override
    public String toString() {
        if (mContext != null) {
            return mContext.getResources().getString(R.string.error_code) + " " + mErrorModel.getErrorNum()
                    + mContext.getResources().getString(R.string.comma) + " " + mErrorModel.getMessageString(mContext, mOccurredErrorMessage);
        }
        return "";
    }

    public String extractExceptionDetail(Exception ex) {
        return ExtractExceptionDetail(ex);
    }

    public static String ExtractExceptionDetail(Exception ex) {
        return (ex.getMessage() != null ? ex.getMessage() + " # " : "") + ex.getClass().getName();
    }
}
