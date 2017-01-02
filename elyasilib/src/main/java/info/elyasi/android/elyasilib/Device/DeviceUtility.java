package info.elyasi.android.elyasilib.Device;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Canada on 8/18/2016.
 */
public class DeviceUtility {

    public static String getDeviceUniqueId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
