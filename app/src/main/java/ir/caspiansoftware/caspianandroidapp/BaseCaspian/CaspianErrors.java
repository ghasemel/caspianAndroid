package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Models.ErrorListModel;
import info.elyasi.android.elyasilib.Models.ErrorModel;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.UserBLL;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.LoginActivity;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/27/2016.
 */
public class CaspianErrors {
    private static final String TAG = "CaspianErrors";

    public static final String INVOICE_NUM_INVALID = "invoice num is invalid";
    public static final String DATE_INVALID = "date is invalid";
    public static final String CUSTOMER_INVALID = "customer is invalid";
    public static final String SAVING_ERROR = "saving error";
    public static final String invoice_id_invalid = "invoice_id_invalid";
    public static final String year_not_specified = "year_not_specified";
    public static final String person_not_exist = "person_not_exist";
    public static final String database_need_drop = "database_need_drop";

    public static final String pfaktor_person_null = "pfaktor_person_null";
    public static final String pfaktor_kala_null = "pfaktor_kala_null";
    public static final String gps_is_off = "gps_is_off";
    public static final String location_not_available = "location_not_available";

    public static final String permissions_are_null = "permissions are null";

    public static final String gps_provider_not_exist = "provider doesn't exist: gps";
    //public static final String didnot_sell_already = "didn't sell already";

    //private static LinkedHashMap<String, Integer> sErrorsList = new LinkedHashMap<>();
    private static List<ErrorListModel> sErrorsList;


    private static CaspianErrors sCaspianErrors = null;
    public static CaspianErrors get() {
        if (sCaspianErrors == null) {
            sCaspianErrors = new CaspianErrors();
        }
        return sCaspianErrors;
    }

    private CaspianErrors() {
        sErrorsList = new ArrayList<>();

        ErrorListModel errorList1000 = new ErrorListModel("common error", 1000, false);
        errorList1000.addSubError("failed to connect to ", R.string.webservice_failed_connection);
        errorList1000.addSubError("java.net.SocketTimeoutException", R.string.webservice_timeout);
        errorList1000.addSubError("user is not active", R.string.user_not_active);
        errorList1000.addSubError(permissions_are_null, R.string.null_permission);
        errorList1000.addSubError("ResponseCode: 401, Message: Time Unauthorized access",
                R.string.webservice_time_is_not_sync, new ErrorModel.IDoFormationOnMessage() {
                    @Override
                    public String doFormatOnMessage(String message, String errorMessage) {
                        String[] parts = errorMessage.split("#");
                        if (parts.length > 2)
                            return String.format(message, parts[2]);
                        return message;
                    }
                });
        errorList1000.addSubError("ResponseCode: 401, Message: Unauthorized access", R.string.device_not_defined_on_server);
        errorList1000.addSubError("ResponseCode: 303, Message", R.string.user_should_logout, new ErrorModel.IDoFormationOnMessage() {
            @Override
            public String doFormatOnMessage(String message, String errorMessage) {
                UserBLL userBLL = new UserBLL(Vars.CONTEXT);
                userBLL.logout(Vars.USER);

                Intent i = new Intent(Vars.CONTEXT, LoginActivity.class);

                // clear history of activities
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // start login activity
                Vars.CONTEXT.startActivity(i);

                return message;
            }
        });
        sErrorsList.add(errorList1000);

        ErrorListModel errorList400 = new ErrorListModel("ResponseCode: 400", 2000, true);
        errorList400.addSubError("Message: Bad AsyncRequest", R.string.webservice_failed_connection);
        errorList400.addSubError("Message: Not Found", R.string.not_found);
        errorList400.addSubError("Message: Invalid username or password", R.string.invalid_userName_password);
        errorList400.addSubError("Bad Request - Invalid Hostname", R.string.webservice_configuration_is_invalid);
        sErrorsList.add(errorList400);

        // not found
        ErrorListModel errorList404 = new ErrorListModel("ResponseCode: 404", 3000, true);
        errorList404.addSubError("Invalid username or password", R.string.user_incorrect_login);
        errorList404.addSubError("No HTTP resource was found that matches the request URI", R.string.no_HTTP_resource_was_found);
        errorList404.addSubError("ResponseCode: 404, Message: no mali years found", R.string.no_year_mali_found);
        errorList404.addSubError("ResponseCode: 404, Message: didn't sell already", R.string.not_already_sold);
        errorList404.addSubError("ResponseCode: 404, Message: there is not any price", R.string.not_already_sold);
        sErrorsList.add(errorList404);

        ErrorListModel errorList4000 = new ErrorListModel("invoice errors", 4000, false);
        errorList4000.addSubError(INVOICE_NUM_INVALID, R.string.invoice_num_is_invalid);
        errorList4000.addSubError(DATE_INVALID, R.string.date_invalid);
        errorList4000.addSubError(CUSTOMER_INVALID, R.string.customer_invalid);
        errorList4000.addSubError(SAVING_ERROR, R.string.error_saving);
        errorList4000.addSubError(invoice_id_invalid, R.string.error_saving);
        errorList4000.addSubError(year_not_specified, R.string.year_not_specified);
        errorList4000.addSubError(person_not_exist, R.string.person_not_exist, new ErrorModel.IDoFormationOnMessage() {
            @Override
            public String doFormatOnMessage(String message, String errorMessage) {
                String[] parts = errorMessage.split("#");
                if (parts.length == 2)
                    return String.format(message, parts[1]);
                return message;
            }
        });
        errorList4000.addSubError(database_need_drop, R.string.database_need_create);
        errorList4000.addSubError(pfaktor_person_null, R.string.pfaktor_person_is_null);
        errorList4000.addSubError(pfaktor_kala_null, R.string.pfaktor_kala_is_null);
        errorList4000.addSubError(gps_is_off, R.string.gps_should_be_ON);
        errorList4000.addSubError(location_not_available, R.string.location_not_available);
        errorList4000.addSubError(gps_provider_not_exist, R.string.gps_module_not_available);
        sErrorsList.add(errorList4000);


        ErrorListModel errorList500 = new ErrorListModel("exception errors", 500, false);
        errorList500.addSubError("ResponseCode: 500, Message: you can see just bed", R.string.you_can_just_see_bed);
        sErrorsList.add(errorList500);
    }

    public ErrorModel getErrorModel(String errorString) {
        Log.d(TAG, errorString);
        for (ErrorListModel errorList: sErrorsList) {
            if (errorList.hasErrorModel(errorString)) {
                return errorList.getErrorModel(errorString);
            }
        }
        return null;
    }
//    public ErrorModel getErrorModel(String errorMessage) {
//        int index = 0;
//        for (String key : sErrorsList.keySet()) {
//            if (errorMessage.contains(key)) {
//                ErrorModel errorModel = new ErrorModel();
//                errorModel.setErrorNum(index + 1000);
//                errorModel.setMessageId((int) sErrorsList.values().toArray()[index]);
//                return errorModel;
//            }
//            index++;
//        }
//        return null;
//    }

//    public int getDescriptionId(String errorMessage) {
//
//        if (sErrorsList.containsKey(errorMessage))
//            return sErrorsList.get(errorMessage);
//        return -1;
//    }
//
//    public int getErrorNumber(String errorMessage) {
//        int num = 1000;
//        for (String key : sErrorsList.keySet()) {
//            if (key.equals(errorMessage)) {
//                return num;
//            }
//            num++;
//        }
//        return -1;
//    }


}
