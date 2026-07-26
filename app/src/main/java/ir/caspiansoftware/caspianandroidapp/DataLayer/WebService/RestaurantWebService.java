package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import android.util.Log;

import org.json.JSONArray;

import java.util.List;

import info.elyasi.android.elyasilib.WebService.NameValue;
import info.elyasi.android.elyasilib.WebService.RESTDotNetWebService;
import info.elyasi.android.elyasilib.WebService.RequestType;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.Models.OrderLineModel;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;

/**
 * Calls api/Restaurant/* on CaspianWebAPI.
 */
public class RestaurantWebService extends RESTDotNetWebService {
    private static final String TAG = "RestaurantWebService";

    public RestaurantWebService() {
        super(SettingWebService.getApiKey(), SettingWebService.getDeviceId(),
              SettingWebService.AUTHENTICATION_SCHEME, SettingWebService.TIME_OUT);
    }

    @Override
    protected String getControllerName() {
        return "Restaurant";
    }

    public ResponseWebService getTables(String dbName) throws Exception {
        Log.d(TAG, "getTables start");
        NameValue[] parameter = { new NameValue<>("dbName", dbName) };
        return sendRequest(SettingWebService.getAPI_URL(), "GetTables", parameter, true,
                RequestType.create(RequestType.RType.GET));
    }

    public ResponseWebService getGroups(String dbName) throws Exception {
        Log.d(TAG, "getGroups start");
        NameValue[] parameter = { new NameValue<>("dbName", dbName) };
        return sendRequest(SettingWebService.getAPI_URL(), "GetGroups", parameter, true,
                RequestType.create(RequestType.RType.GET));
    }

    public ResponseWebService getGroupItems(String dbName, int grkCode) throws Exception {
        Log.d(TAG, "getGroupItems start, grk=" + grkCode);
        NameValue[] parameter = {
                new NameValue<>("dbName", dbName),
                new NameValue<>("grkCode", String.valueOf(grkCode))
        };
        return sendRequest(SettingWebService.getAPI_URL(), "GetGroupItems", parameter, true,
                RequestType.create(RequestType.RType.GET));
    }

    public ResponseWebService getOpenOrder(String dbName, int tableId) throws Exception {
        Log.d(TAG, "getOpenOrder start, table=" + tableId);
        NameValue[] parameter = {
                new NameValue<>("dbName", dbName),
                new NameValue<>("tableId", String.valueOf(tableId))
        };
        return sendRequest(SettingWebService.getAPI_URL(), "GetOpenOrder", parameter, true,
                RequestType.create(RequestType.RType.GET));
    }

    /**
     * Submits the draft lines for a table.
     *
     * androidOrderId is the idempotency key. If the response is lost and the
     * waiter retries, the server matches on it and returns the original order
     * rather than creating a second one -- which on unreliable restaurant
     * wi-fi is the difference between one meal and two.
     */
    public ResponseWebService submitOrder(String dbName, int tableId, int androidOrderId,
                                          String des, List<OrderLineModel> lines) throws Exception {
        Log.d(TAG, "submitOrder start, table=" + tableId + ", orderId=" + androidOrderId);

        JSONArray jsonLines = new JSONArray();
        for (OrderLineModel line : lines)
            jsonLines.put(line.toJSON());

        NameValue[] parameter = {
                new NameValue<>("dbName", dbName),
                new NameValue<>("tableId", String.valueOf(tableId)),
                new NameValue<>("androidOrderId", String.valueOf(androidOrderId)),
                new NameValue<>("des", des == null ? "" : des),
                new NameValue<>("lines", jsonLines)
        };

        return sendRequest(SettingWebService.getAPI_URL(), "SubmitOrder", parameter, true,
                RequestType.create(RequestType.RType.POST));
    }
}
