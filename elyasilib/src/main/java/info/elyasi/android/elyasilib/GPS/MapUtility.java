package info.elyasi.android.elyasilib.GPS;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Canada on 8/17/2016.
 */
public class MapUtility {

    public static void ShowLocationOnGoogleMap(double latitude, double longitude, Context context) {
        if (latitude > 0 && longitude > 0) {
            Intent browserIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                            // format1 :  // https://www.google.com/maps/@%s,%s,16.38z
                            // format2 : // http://maps.google.com/maps?q=24.197611,120.780512
                            String.format("http://maps.google.com/maps?q=%s,%s",
                                    latitude, longitude
                            )
                    )
            );
            context.startActivity(browserIntent);
        }
    }
}
