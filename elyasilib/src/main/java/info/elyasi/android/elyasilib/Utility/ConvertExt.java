package info.elyasi.android.elyasilib.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canada on 8/16/2016.
 */
public class ConvertExt {

    public static List<Integer> toList(String listValue) {
        if (listValue != null && listValue.length() > 0) {
            listValue = listValue.replace("[", "").replace("]", "");

            String[] listValueArray = listValue.split(",");
            List<Integer> list = new ArrayList<>();
            for (String s: listValueArray) {
                list.add(Integer.parseInt(s));
            }
            return list;
        }
        return null;
    }
}
