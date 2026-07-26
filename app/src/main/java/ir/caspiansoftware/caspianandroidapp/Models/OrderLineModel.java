package ir.caspiansoftware.caspianandroidapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import info.elyasi.android.elyasilib.Utility.IJson;

/**
 * A single line on an order.
 *
 * The same class covers both directions, distinguished by mSent:
 *
 *  - mSent = true  : came back from the server, so the kitchen already has it.
 *                    Must render read-only; re-sending it would have the dish
 *                    cooked twice.
 *  - mSent = false : still in the waiter's draft, editable and submittable.
 */
public class OrderLineModel implements Serializable, IJson {
    private int mRadif;
    private String mCode;
    private String mName;
    private double mCount;
    private double mPrice;
    private String mSpec;
    private boolean mSent;
    private int mAndroidLineId;

    public OrderLineModel() { }

    public OrderLineModel(MenuItemModel item, double count) {
        mCode = item.getCode();
        mName = item.getName();
        mPrice = item.getPrice();
        mCount = count;
        mSpec = "";
        mSent = false;
    }

    public int getRadif() { return mRadif; }
    public void setRadif(int radif) { mRadif = radif; }

    public String getCode() { return mCode; }
    public void setCode(String code) { mCode = code; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public double getCount() { return mCount; }
    public void setCount(double count) { mCount = count; }

    public double getPrice() { return mPrice; }
    public void setPrice(double price) { mPrice = price; }

    public String getSpec() { return mSpec == null ? "" : mSpec; }
    public void setSpec(String spec) { mSpec = spec; }

    public boolean isSent() { return mSent; }
    public void setSent(boolean sent) { mSent = sent; }

    public int getAndroidLineId() { return mAndroidLineId; }
    public void setAndroidLineId(int androidLineId) { mAndroidLineId = androidLineId; }

    public double getTotal() { return mCount * mPrice; }

    public static OrderLineModel fromJSON(JSONObject json) throws JSONException {
        OrderLineModel m = new OrderLineModel();
        m.setRadif(json.optInt("radif"));
        m.setCode(json.optString("code", ""));
        m.setName(json.optString("name", ""));
        m.setCount(json.optDouble("mcount", 0));
        m.setPrice(json.optDouble("price", 0));
        m.setSpec(json.optString("spec", ""));
        m.setSent(true);
        return m;
    }

    /**
     * Submit shape. price is deliberately not sent: the server re-resolves it
     * from the catalogue so a tampered client cannot choose its own prices.
     */
    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("androidLineId", mAndroidLineId);
        json.put("code", mCode);
        json.put("mcount", mCount);
        json.put("spec", getSpec());
        return json;
    }
}
