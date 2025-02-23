package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import android.util.Log;

import org.json.JSONArray;

import java.util.List;

import info.elyasi.android.elyasilib.WebService.NameValue;
import info.elyasi.android.elyasilib.WebService.RESTDotNetWebService;
import info.elyasi.android.elyasilib.WebService.RequestType;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;

/**
 * Created by Canada on 8/3/2016.
 */
public class MaliWebService extends RESTDotNetWebService {
    private static final String TAG = "PFaktorWebService";


    public MaliWebService() {
        super(SettingWebService.getApiKey(), SettingWebService.getDeviceId(), SettingWebService.AUTHENTICATION_SCHEME, SettingWebService.TIME_OUT);
    }


    @Override
    protected String getControllerName() {
        return "PFaktor";
    }


    public ResponseWebService uploadPFaktorList(List<MaliModel> maliModels, String dbName) throws Exception {
        Log.d(TAG, "uploadPFaktorList(): started");

        try {

            JSONArray jsonArray = new JSONArray();
            for (MaliModel maliModel : maliModels) {
                jsonArray.put(maliModel.toJSON());
            }

            Log.d(TAG, "Faktor to JSON: " + (jsonArray.toString()));

            NameValue[] parameter = {
                    new NameValue<>("dbName", dbName),
                    new NameValue<>("pfaktor_list", jsonArray)
            };

            return sendRequest(SettingWebService.getAPI_URL(),
                    "UploadPFaktors", parameter, true, RequestType.create(RequestType.RType.POST));

            //return responseWebService;
        } finally {
            Log.d(TAG, "uploadPFaktorList(): finished");
        }
    }
}
