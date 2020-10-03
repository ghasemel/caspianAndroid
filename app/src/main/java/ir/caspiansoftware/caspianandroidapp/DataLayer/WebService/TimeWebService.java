package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import android.util.Log;

import org.apache.commons.net.time.TimeTCPClient;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import info.elyasi.android.elyasilib.WebService.NameValue;
import info.elyasi.android.elyasilib.WebService.RESTDotNetWebService;
import info.elyasi.android.elyasilib.WebService.RequestType;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 8/3/2016.
 */
public class TimeWebService {
    private static final String TAG = "TimeWebService";

    /**
     * Needs to be call in background
     */
    public Date getCurrentDateTime() throws Exception {
        Log.d(TAG, "uploadPFaktorList(): started");

        TimeTCPClient client = new TimeTCPClient();
        try {
            // Set timeout of 60 seconds
            client.setDefaultTimeout(60000);
            // Connecting to time server
            // Other time servers can be found at : http://tf.nist.gov/tf-cgi/servers.cgi#
            // Make sure that your program NEVER queries a server more frequently than once every 4 seconds
            client.connect("time.nist.gov");
            Date date = client.getDate();
            client.disconnect();

            Log.d(TAG, "Current date: " + Vars.iso8601Format.format(date));
            return date;
        } finally {
            //client.disconnect();
            Log.d(TAG, "uploadPFaktorList(): finished");
        }
    }
}
