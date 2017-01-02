package info.elyasi.android.elyasilib.Utility;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;



/**
 * Created by Canada on 7/20/2016.
 */
public class JsonExt {
    private static final String TAG = "JsonExt";
    //private static final String sample_name = "sample_name";


    public static JSONObject toJSON(String value) {
        try {
            //JSONObject json = new JSONObject(value);
            return new JSONObject(value);
        } catch (Throwable t) {
            Log.e("string", "Could not parse this value as JSON: \"" + value + "\"");
        }
        return null;
    }

    public static boolean isJSONArray(String values) {
        try {
            Object obj = new JSONTokener(values).nextValue();
            return obj instanceof JSONArray;
        } catch (JSONException jex) {
            Log.d(TAG, jex.getMessage());
        }
        return false;
    }

    public static JSONArray toJsonArray(String values) {
        try {
            Object obj = new JSONTokener(values).nextValue();
            if (obj instanceof JSONArray)
                return (JSONArray) obj;
        } catch (JSONException jex) {
            Log.d(TAG, jex.getMessage());
        }
        return null;
    }

    public static String toJsonArrayString(String[] value) throws JSONException {
        return toJsonArray(value).toString();
    }


    public static JSONArray toJsonArray(String[] value) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < value.length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(String.valueOf(i), value[i]);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }


    public static String[] toArray(String jsonArrayString) throws JSONException {
        Object obj = new JSONTokener(jsonArrayString).nextValue();
        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            if (array.length() == 0)
                return null;

            String[] result = new String[array.length()];
            for (int j = 0; j < array.length(); j++) {
                result[j] = array.getJSONObject(j).getString(String.valueOf(j));
            }
            return result;
        }
        throw new JSONException("'" + jsonArrayString + "' is not a json array string value");
    }
}
