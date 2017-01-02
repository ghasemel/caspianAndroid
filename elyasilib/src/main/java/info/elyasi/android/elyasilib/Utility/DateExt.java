package info.elyasi.android.elyasilib.Utility;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Canada on 1/19/2016.
 */
public class DateExt {

    public static Date getDate(long timeStamp) {

        Date netDate = new Date(timeStamp);
        return netDate;

    }

    public static String getDateTimeAsFileName() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public static String getDateAsFileName() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public static String Now() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        return df.format(c.getTime());
    }
}
