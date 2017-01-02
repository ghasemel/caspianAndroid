package info.elyasi.android.elyasilib.Persian;

import android.text.Editable;

/**
 * Created by Canada on 2/9/2016.
 */
public class PersianConvert {
    public static final char N0 = '\u06f0';
    public static final char N1 = '\u06f1';
    public static final char N2 = '\u06f2';
    public static final char N3 = '\u06f3';
    public static final char N4 = '\u06f4';
    public static final char N5 = '\u06f5';
    public static final char N6 = '\u06f6';
    public static final char N7 = '\u06f7';
    public static final char N8 = '\u06f8';
    public static final char N9 = '\u06f9';
    public static final char N_DECIMAL = '/';


    //public static String ConvertNumbersToPersian(String pValue) {
        //String languageToLoad  = "fa"; // your language
        //Locale locale = new Locale(languageToLoad);
        //return String.format(locale, "%f", pValue);

    public static String ConvertDigitsToPersian(String pValue) {
        if (pValue == null) return null;

        return pValue
                .replace('0', N0)
                .replace('1', N1)
                .replace('2', N2)
                .replace('3', N3)
                .replace('4', N4)
                .replace('5', N5)
                .replace('6', N6)
                .replace('7', N7)
                .replace('8', N8)
                .replace('9', N9);
    }

    public static String ConvertDigitsToPersian(Editable pValue) {
        return ConvertDigitsToPersian(pValue.toString());
    }

    public static String ConvertNumbersToPersian(String pValue) {
        if (pValue == null) return null;

        return pValue
                .replace('0', N0)
                .replace('1', N1)
                .replace('2', N2)
                .replace('3', N3)
                .replace('4', N4)
                .replace('5', N5)
                .replace('6', N6)
                .replace('7', N7)
                .replace('8', N8)
                .replace('9', N9)
                .replace('.', N_DECIMAL);
    }

    public static String ConvertNumbersToPersian(Editable pValue) {
        return ConvertNumbersToPersian(pValue.toString());
    }




    public static String ConvertDigitsToLatin(String pValue) {
        return pValue
                .replace(N0, '0')
                .replace(N1, '1')
                .replace(N2, '2')
                .replace(N3, '3')
                .replace(N4, '4')
                .replace(N5, '5')
                .replace(N6, '6')
                .replace(N7, '7')
                .replace(N8, '8')
                .replace(N9, '9');
    }

    public static String ConvertDigitsToLatin(Editable pValue) {
        return ConvertDigitsToLatin(pValue.toString());
    }

    public static String ConvertNumbersToLatin(String pValue) {
        return pValue
                .replace(N0, '0')
                .replace(N1, '1')
                .replace(N2, '2')
                .replace(N3, '3')
                .replace(N4, '4')
                .replace(N5, '5')
                .replace(N6, '6')
                .replace(N7, '7')
                .replace(N8, '8')
                .replace(N9, '9')
                .replace(N_DECIMAL, '.');
    }

    public static String ConvertNumbersToLatin(Editable pValue) {
        return ConvertNumbersToLatin(pValue.toString());
    }
}
