package info.elyasi.android.elyasilib.UI;

/**
 * Created by Canada on 3/8/2016.
 */
public interface IActivityCallback {
    void fragment_callback(String actionName, FormActionTypes actionTypes, Object... parameter);
}
