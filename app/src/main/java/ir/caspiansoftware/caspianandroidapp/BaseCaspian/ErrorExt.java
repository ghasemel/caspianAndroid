package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.content.Context;

import info.elyasi.android.elyasilib.Models.ErrorModel;
import info.elyasi.android.elyasilib.Utility.AErrorHandler;
import info.elyasi.android.elyasilib.Utility.LogExt;
import ir.caspiansoftware.caspianandroidapp.Setting;

/**
 * Created by Canada on 2/8/2016.
 */
public class ErrorExt extends AErrorHandler {

   // public static final Map<String, Integer> ERROR_DICTIONARY = new HashMap<>();

    private static ErrorExt sErrorExt;
    public static ErrorExt create(Context pContext) {
        if (sErrorExt == null) {
            sErrorExt = new ErrorExt(pContext);
        }
        return sErrorExt;
    }

    public static ErrorExt get() {
        return sErrorExt;
    }

    public static ErrorExt get(String errorMessage) {
        sErrorExt.ConvertToErrorModel(errorMessage);
        return sErrorExt;
    }

    public static ErrorExt get(Exception ex) {
        sErrorExt.setException(ex);
        return sErrorExt;
    }

    protected ErrorExt(Context pContext) {
        super(pContext);
    }

    protected ErrorModel ConvertToErrorModel(String errorMessage) {

        LogExt logExt = LogExt.get(Setting.APP_NAME, mContext);

        if (errorMessage == null) {
            logExt.WriteLine("errorMessage is null");
            return new ErrorModel(null, -1, -1);
        }

        logExt.WriteLine(errorMessage);

        //setErrorCode(-1);
        //setUserMessage(errorMessage);

        ErrorModel errorModel = CaspianErrors.get().getErrorModel(errorMessage);
        if (errorModel != null) {
            return errorModel;
        } else {
            return new ErrorModel(errorMessage, -1, -1);
        }

//        if (errorMessage.contains(CaspianErrors.failed_to_connect_to) && errorMessage.contains(Setting.getIP())) {
//            //failed to connect to /192.168.129.216 (port 5412) after 15000ms
//            setErrorCode(1000);
//            String v = mContext.getResources().getString(R.string.webservice_failed_connection);
//            //v = MessageFormat.format(v, Setting.API_IP);
//            setUserMessage(v);
//
//        } else if (errorMessage.contains("ResponseCode: 400")) {
//            // region ResponseCode: 400 - 2000
//            if (errorMessage.contains("Message: Bad AsyncRequest")) {
//                setErrorCode(2000);
//                String v = mContext.getResources().getString(R.string.webservice_failed_connection);
//                setUserMessage(v);
//            } else if (errorMessage.contains("Message: Not Found")) {
//                setErrorCode(2001);
//                String v = mContext.getResources().getString(R.string.not_found);
//                setUserMessage(v);
//            } else if (errorMessage.contains("Message: Invalid username or password")) {
//                setErrorCode(2002);
//                String v = mContext.getResources().getString(R.string.invalid_userName_password);
//                setUserMessage(v);
//            } else if (errorMessage.contains("Bad Request - Invalid Hostname")) {
//                setErrorCode(2003);
//                String v = mContext.getResources().getString(R.string.webservice_configuration_is_invalid);
//                setUserMessage(v);
//            }
//            // endregion ResponseCode: 400
//        } else if (errorMessage.contains("ResponseCode: 404")) {
//            // region ResponseCode: 404 - 3000
//            if (errorMessage.contains("Invalid username or password")) {
//                setErrorCode(3000);
//                String v = mContext.getResources().getString(R.string.user_incorrect_login);
//                setUserMessage(v);
//            }
//            // endregion ResponseCode: 404
//        } else if (errorMessage.contains("java.net.SocketTimeoutException")) {
//            setErrorCode(1001);
//            String v = mContext.getResources().getString(R.string.webservice_timeout);
//            setUserMessage(v);
//
//        } else if (errorMessage.contains("user is not active")) {
//            setErrorCode(1002);
//            String v = mContext.getResources().getString(R.string.user_not_active);
//            setUserMessage(v);
//
//        } else if (errorMessage.contains("ResponseCode: 401, Message: Unauthorized access")) {
//            // device not defined on the server or
//            // time between the device and server aren't sync
//            setErrorCode(1003);
//            String v = mContext.getResources().getString(R.string.unauthorized_access);
//            setUserMessage(v);
//        } else if (errorMessage.contains("ResponseCode: 404") && errorMessage.contains("No HTTP resource was found that matches the request URI")) {
//            setErrorCode(1004);
//            String v = mContext.getResources().getString(R.string.no_HTTP_resource_was_found);
//            setUserMessage(v);
//        } else if (errorMessage.contains("permissions are null")) {
//            setErrorCode(1005);
//            String v = mContext.getResources().getString(R.string.null_permission);
//            setUserMessage(v);
//        } else if (errorMessage.contains("ResponseCode: 401, Message: Time Unauthorized access")) {
//            setErrorCode(1006);
//            String v = mContext.getResources().getString(R.string.webservice_time_is_not_sync);
//
//            String[] parts = errorMessage.split("#");
//            if (parts.length > 2)
//                v = String.format(v, parts[2]);
//                //v = MessageFormat.format(v, parts[2]);
//
//            setUserMessage(v);
//        } else {
//            errorMessage = errorMessage.replace("# java.lang.Exception", "").trim();
//            int messageId = CaspianErrors.get().getDescriptionId(errorMessage);
//            if (messageId > 0) {
//                setUserMessage(mContext.getString(messageId));
//                setErrorCode(CaspianErrors.get().getErrorNumber(errorMessage));
//            }
//            //setErrorCode(1005);
//            //String v = mContext.getResources().getString(R.string.unknown_error);
//            //setUserMessage(v);
 //       }
    }
}
