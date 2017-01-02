package info.elyasi.android.elyasilib.Thread;

/**
 * Created by Canada on 2/8/2016.
 */
public interface IUICallBack {
    void onError(String caller, String errorMessage);
    void onSuccess(String caller, int responseCode, String responseData);
    void stopProgressBar(String caller, int value);
}
