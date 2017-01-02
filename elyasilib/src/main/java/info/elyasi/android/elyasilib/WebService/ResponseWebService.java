package info.elyasi.android.elyasilib.WebService;

import java.util.Objects;

/**
 * Created by Canada on 3/3/2016.
 */
public class ResponseWebService {
    private int mCode;
    private String mData;
    private String mCaller;

    public ResponseWebService(int code, String data, String caller) {
        mCode = code;
        mData = data;
        mCaller = caller;
    }

    public int getCode() {
        return mCode;
    }

    public String getData() {
        return mData;
    }

    public String getCaller() {
        return mCaller;
    }

    @Override
    public String toString() {
        return mCode + " - " + mCaller + " - " + mData;
    }
}
