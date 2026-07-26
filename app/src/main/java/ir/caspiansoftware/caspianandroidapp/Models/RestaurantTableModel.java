package ir.caspiansoftware.caspianandroidapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * A restaurant table (miz) and its live occupancy.
 *
 * Tables are defined in the FROOSH desktop application only -- this app never
 * creates, edits or reorders them, so there is no write path for this model.
 *
 * Occupancy is derived server-side from whether the table has an unsettled
 * pre-invoice, so a table frees itself the moment the cashier settles it.
 */
public class RestaurantTableModel implements Serializable {
    private int mId;
    private String mTitle;
    private int mOpenOrderCount;
    private int mOpenNum;
    private double mOpenTotal;
    private int mItemCount;

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getTitle() { return mTitle; }
    public void setTitle(String title) { mTitle = title; }

    public int getOpenOrderCount() { return mOpenOrderCount; }
    public void setOpenOrderCount(int openOrderCount) { mOpenOrderCount = openOrderCount; }

    public int getOpenNum() { return mOpenNum; }
    public void setOpenNum(int openNum) { mOpenNum = openNum; }

    public double getOpenTotal() { return mOpenTotal; }
    public void setOpenTotal(double openTotal) { mOpenTotal = openTotal; }

    public int getItemCount() { return mItemCount; }
    public void setItemCount(int itemCount) { mItemCount = itemCount; }

    public boolean isOccupied() { return mOpenOrderCount > 0; }

    public static RestaurantTableModel fromJSON(JSONObject json) throws JSONException {
        RestaurantTableModel m = new RestaurantTableModel();
        m.setId(json.optInt("id"));
        m.setTitle(json.optString("title", ""));
        m.setOpenOrderCount(json.optInt("openOrderCount"));
        m.setOpenNum(json.optInt("openNum"));
        m.setOpenTotal(json.optDouble("openTotal", 0));
        m.setItemCount(json.optInt("itemCount"));
        return m;
    }

    @Override
    public String toString() { return mTitle; }
}
