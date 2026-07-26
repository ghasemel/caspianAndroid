package ir.caspiansoftware.caspianandroidapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * One sellable dish on the menu.
 *
 * price is already resolved by the server from the desktop's configured touch
 * price level (tanzim.price_touch), so this app never sees the other price
 * columns -- in particular never price4, which is last purchase cost.
 */
public class MenuItemModel implements Serializable {
    private String mCode;
    private String mName;
    private int mCodeGrk;
    private double mPrice;
    private String mVahed;
    private boolean mKhadamat;

    public String getCode() { return mCode; }
    public void setCode(String code) { mCode = code; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public int getCodeGrk() { return mCodeGrk; }
    public void setCodeGrk(int codeGrk) { mCodeGrk = codeGrk; }

    public double getPrice() { return mPrice; }
    public void setPrice(double price) { mPrice = price; }

    public String getVahed() { return mVahed; }
    public void setVahed(String vahed) { mVahed = vahed; }

    public boolean isKhadamat() { return mKhadamat; }
    public void setKhadamat(boolean khadamat) { mKhadamat = khadamat; }

    public static MenuItemModel fromJSON(JSONObject json) throws JSONException {
        MenuItemModel m = new MenuItemModel();
        m.setCode(json.optString("code", ""));
        m.setName(json.optString("name", ""));
        m.setCodeGrk(json.optInt("code_grk"));
        m.setPrice(json.optDouble("price", 0));
        m.setVahed(json.optString("vahed_a", ""));
        m.setKhadamat(json.optBoolean("khadamat", false));
        return m;
    }

    @Override
    public String toString() { return mName; }
}
