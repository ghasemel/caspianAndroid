package info.elyasi.android.elyasilib.Utility;

import android.util.Log;

import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Canada on 12/18/2015.
 */
public class StringExt {

    public static String newGUID()
    {
        return UUID.randomUUID().toString();
    }

    public static String padRight(String str, int length, char ch) {
        length = length - str.length();
        for (int i = 0; i < length; i++) {
            str = str + ch;
        }
        return str;
    }

    public static String padLeft(String str, int length, char ch) {
        length = length - str.length();
        for (int i = 0; i < length; i++) {
            str = ch + str;
        }
        return str;
    }


    public static boolean IsValidUserName(String value) {

        if (value.contains(" ")) {
            return false;
        }


        if (value.startsWith("0") || value.startsWith("1") || value.startsWith("2") ||
                value.startsWith("3") || value.startsWith("4") || value.startsWith("5") ||
                value.startsWith("6") || value.startsWith("7") || value.startsWith("8") ||
                value.startsWith("9")) {

            return false;
        }

        if (value.length() < 5) {
            return false;
        }

        return true;
    }
}
