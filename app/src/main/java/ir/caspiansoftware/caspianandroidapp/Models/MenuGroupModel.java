package ir.caspiansoftware.caspianandroidapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * A goods group (GRK) used as a menu category. Groups that contain no sellable
 * item are filtered out server-side, so every group here opens onto a non-empty
 * grid.
 */
public class MenuGroupModel implements Serializable {
    private int mCode;
    private String mName;
    private int mKalaCount;

    public int getCode() { return mCode; }
    public void setCode(int code) { mCode = code; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public int getKalaCount() { return mKalaCount; }
    public void setKalaCount(int kalaCount) { mKalaCount = kalaCount; }

    public static MenuGroupModel fromJSON(JSONObject json) throws JSONException {
        MenuGroupModel m = new MenuGroupModel();
        m.setCode(json.optInt("code"));
        m.setName(json.optString("name", ""));
        m.setKalaCount(json.optInt("kalaCount"));
        return m;
    }

    @Override
    public String toString() { return mName; }
}
