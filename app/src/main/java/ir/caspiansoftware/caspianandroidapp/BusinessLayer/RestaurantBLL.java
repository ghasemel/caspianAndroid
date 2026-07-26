package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.RestaurantWebService;
import ir.caspiansoftware.caspianandroidapp.Models.MenuGroupModel;
import ir.caspiansoftware.caspianandroidapp.Models.MenuItemModel;
import ir.caspiansoftware.caspianandroidapp.Models.OrderLineModel;
import ir.caspiansoftware.caspianandroidapp.Models.RestaurantOrderModel;
import ir.caspiansoftware.caspianandroidapp.Models.RestaurantTableModel;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Restaurant ordering business logic.
 */
public class RestaurantBLL extends ABusinessLayer {
    private static final String TAG = "RestaurantBLL";

    /** HTTP 404 from GetOpenOrder simply means the table is free. */
    private static final int HTTP_NOT_FOUND = 404;

    private final RestaurantWebService mWebService;

    public RestaurantBLL(Context context) {
        super(context);
        mWebService = new RestaurantWebService();
    }

    private static String currentDbName() {
        if (Vars.YEAR == null)
            throw new IllegalStateException("no fiscal year selected");
        return Vars.YEAR.getDataBase();
    }

    public List<RestaurantTableModel> fetchTables() throws Exception {
        Log.d(TAG, "fetchTables start");
        try {
            ResponseWebService response = mWebService.getTables(currentDbName());
            if (response == null)
                throw new Exception("responseWebService is null");

            List<RestaurantTableModel> list = new ArrayList<>();
            JSONArray arr = new JSONArray(response.getData());
            for (int i = 0; i < arr.length(); i++)
                list.add(RestaurantTableModel.fromJSON(arr.getJSONObject(i)));
            return list;
        } finally {
            Log.d(TAG, "fetchTables finished");
        }
    }

    public List<MenuGroupModel> fetchGroups() throws Exception {
        Log.d(TAG, "fetchGroups start");
        try {
            ResponseWebService response = mWebService.getGroups(currentDbName());
            if (response == null)
                throw new Exception("responseWebService is null");

            List<MenuGroupModel> list = new ArrayList<>();
            JSONArray arr = new JSONArray(response.getData());
            for (int i = 0; i < arr.length(); i++)
                list.add(MenuGroupModel.fromJSON(arr.getJSONObject(i)));
            return list;
        } finally {
            Log.d(TAG, "fetchGroups finished");
        }
    }

    public List<MenuItemModel> fetchGroupItems(int grkCode) throws Exception {
        Log.d(TAG, "fetchGroupItems start, grk=" + grkCode);
        try {
            ResponseWebService response = mWebService.getGroupItems(currentDbName(), grkCode);
            if (response == null)
                throw new Exception("responseWebService is null");

            List<MenuItemModel> list = new ArrayList<>();
            JSONArray arr = new JSONArray(response.getData());
            for (int i = 0; i < arr.length(); i++)
                list.add(MenuItemModel.fromJSON(arr.getJSONObject(i)));
            return list;
        } finally {
            Log.d(TAG, "fetchGroupItems finished");
        }
    }

    /**
     * Loads the bill already running at a table, or an empty order if the table
     * is free. A free table is a normal outcome, not an error.
     */
    public RestaurantOrderModel fetchOrderForTable(RestaurantTableModel table) throws Exception {
        Log.d(TAG, "fetchOrderForTable start, table=" + table.getId());
        try {
            if (!table.isOccupied())
                return emptyOrderFor(table);

            ResponseWebService response = mWebService.getOpenOrder(currentDbName(), table.getId());
            if (response == null || response.getCode() == HTTP_NOT_FOUND)
                return emptyOrderFor(table);

            return RestaurantOrderModel.fromJSON(new JSONObject(response.getData()));
        } finally {
            Log.d(TAG, "fetchOrderForTable finished");
        }
    }

    private static RestaurantOrderModel emptyOrderFor(RestaurantTableModel table) {
        RestaurantOrderModel order = new RestaurantOrderModel();
        order.setTableId(table.getId());
        order.setTableTitle(table.getTitle());
        return order;
    }

    /**
     * Sends the draft lines.
     *
     * androidOrderId must stay stable across retries of the same draft -- it is
     * what stops a resend after a dropped response from ordering the food
     * twice. It is derived from the table and the moment the draft was started,
     * so a genuine second round gets a new id while a retry keeps the old one.
     */
    public RestaurantOrderModel submitDraft(RestaurantOrderModel order, int androidOrderId) throws Exception {
        Log.d(TAG, "submitDraft start, table=" + order.getTableId() + ", orderId=" + androidOrderId);
        try {
            List<OrderLineModel> draft = order.getDraftLines();
            if (draft.isEmpty())
                throw new IllegalStateException("nothing to submit");

            int lineId = 1;
            for (OrderLineModel line : draft)
                if (line.getAndroidLineId() == 0)
                    line.setAndroidLineId(androidOrderId * 100 + lineId++);

            ResponseWebService response = mWebService.submitOrder(
                    currentDbName(), order.getTableId(), androidOrderId, "", draft);

            if (response == null)
                throw new Exception("responseWebService is null");

            JSONObject json = new JSONObject(response.getData());
            order.setNum(json.optInt("num"));
            order.markDraftAsSent();

            return order;
        } finally {
            Log.d(TAG, "submitDraft finished");
        }
    }
}
