package info.elyasi.android.elyasilib.BLL;

/**
 * Created by Canada on 6/20/2016.
 */
public class ResponseBLL<T> {
    private int mCode;
    private T mData;
    private String mCaller;

    public ResponseBLL(int code, T data, String caller) {
        mCode = code;
        mData = data;
        mCaller = caller;
    }

    public int getCode() {
        return mCode;
    }

    public T getData() {
        return mData;
    }

    public String getCaller() {
        return mCaller;
    }

    @Override
    public String toString() {
        return "caller: " + getCaller()+ ", responseCode: " +
                String.valueOf(getCode()) + ", data: " + getData();
    }
}
