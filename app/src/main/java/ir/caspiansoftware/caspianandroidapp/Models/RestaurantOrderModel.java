package ir.caspiansoftware.caspianandroidapp.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The running bill for one table: lines already sent to the kitchen plus the
 * waiter's current draft.
 *
 * Only unsent lines are submitted. The server decides whether the submit opens
 * a new order or appends to the one already at the table, so this class never
 * has to guess which case it is in.
 */
public class RestaurantOrderModel implements Serializable {
    private int mNum;
    private int mTableId;
    private String mTableTitle;
    private String mDate;
    private final List<OrderLineModel> mLines = new ArrayList<>();

    public int getNum() { return mNum; }
    public void setNum(int num) { mNum = num; }

    public int getTableId() { return mTableId; }
    public void setTableId(int tableId) { mTableId = tableId; }

    public String getTableTitle() { return mTableTitle; }
    public void setTableTitle(String tableTitle) { mTableTitle = tableTitle; }

    public String getDate() { return mDate; }
    public void setDate(String date) { mDate = date; }

    public List<OrderLineModel> getLines() { return mLines; }

    /**
     * Adds one of a dish, merging into an existing unsent line for the same
     * dish and note. Lines already sent are never merged into -- the kitchen
     * has them, so a second round has to be its own line.
     */
    public void addItem(MenuItemModel item, double count) {
        for (OrderLineModel line : mLines) {
            if (!line.isSent()
                    && line.getCode().equals(item.getCode())
                    && line.getSpec().isEmpty()) {
                line.setCount(line.getCount() + count);
                return;
            }
        }
        mLines.add(new OrderLineModel(item, count));
    }

    public void removeLine(OrderLineModel line) {
        if (!line.isSent())
            mLines.remove(line);
    }

    public List<OrderLineModel> getDraftLines() {
        List<OrderLineModel> draft = new ArrayList<>();
        for (OrderLineModel line : mLines)
            if (!line.isSent())
                draft.add(line);
        return draft;
    }

    /** How many of a dish are in the draft -- drives the tile quantity badge. */
    public double getDraftCount(String kalaCode) {
        double n = 0;
        for (OrderLineModel line : mLines)
            if (!line.isSent() && line.getCode().equals(kalaCode))
                n += line.getCount();
        return n;
    }

    public double getDraftTotal() {
        double t = 0;
        for (OrderLineModel line : mLines)
            if (!line.isSent())
                t += line.getTotal();
        return t;
    }

    public double getSentTotal() {
        double t = 0;
        for (OrderLineModel line : mLines)
            if (line.isSent())
                t += line.getTotal();
        return t;
    }

    public double getGrandTotal() { return getDraftTotal() + getSentTotal(); }

    public int getDraftItemCount() { return getDraftLines().size(); }

    public boolean hasDraft() { return getDraftItemCount() > 0; }

    public void markDraftAsSent() {
        for (OrderLineModel line : mLines)
            line.setSent(true);
    }

    public static RestaurantOrderModel fromJSON(JSONObject json) throws JSONException {
        RestaurantOrderModel m = new RestaurantOrderModel();
        m.setNum(json.optInt("num"));
        m.setTableId(json.optInt("tableId"));
        m.setTableTitle(json.optString("tableTitle", ""));
        m.setDate(json.optString("date", ""));

        JSONArray arr = json.optJSONArray("lines");
        if (arr != null)
            for (int i = 0; i < arr.length(); i++)
                m.getLines().add(OrderLineModel.fromJSON(arr.getJSONObject(i)));

        return m;
    }
}
