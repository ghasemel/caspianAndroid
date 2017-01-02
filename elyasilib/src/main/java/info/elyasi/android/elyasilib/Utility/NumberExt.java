package info.elyasi.android.elyasilib.Utility;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Canada on 12/18/2015.
 */
public class NumberExt {

    public static byte[] ConvertToUnsignedByte(byte[] byteArray) {
        for (int i = 0; i < byteArray.length; i++) {
            int n = byteArray[i] & 0xFF;
            byteArray[i] = (byte)(n);
        }
        return byteArray;
    }

    public static String DigitSeparator(BigDecimal decimal) {
        return String.format("%,d", decimal.longValue());
    }

    public static String DigitSeparator(long value) {
        return String.format("%,d", value);
    }

    public static String DigitSeparator(double value) {
        long v = (long) Math.floor(value);
        return DigitSeparator(v);
    }

    public static long getRandomNumber(int length) {
        Random random = new Random();
        double min = Math.pow(10, length - 1);
        return (long) Math.floor(min + random.nextFloat() * (9 * min));
    }
}
