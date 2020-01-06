package info.elyasi.android.elyasilib.WebService;


import android.util.Log;

import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.HashMap;

import info.elyasi.android.elyasilib.Security.Cryptography;
import info.elyasi.android.elyasilib.Utility.StringExt;

/**
 * Created by Canada on 12/18/2015.
 */
public abstract class REST_HMAC {
    private static final String TAG = "REST_HMAC";

    private HttpRequest mHttpRequest;
    //private boolean mResultIsJson = false;
    //private ICallBack mResponseCallBack = null;
    private final String mAPI_KEY;
    private final String mAPP_ID;
    private final String mAuthenticationScheme;
    private int mTimeout; // in millisecond
    //private String mMethod;
    private RequestType mRequestType;

    public REST_HMAC(String pAPI_KEY, String pAPP_ID, String pAuthenticationScheme, int pTimeout) {
        mAPI_KEY = pAPI_KEY;
        mAPP_ID = pAPP_ID;
        mAuthenticationScheme = pAuthenticationScheme;
        mTimeout = pTimeout;
        mHttpRequest = new HttpRequest(true, mAuthenticationScheme, "", mTimeout);
    }

    // APPId, requestHttpMethod, requestUri, requestTimeStamp, nonce, requestContentBase64String
    protected static String createSignature(final String pApiKey, final String pAppId, final String pUri, final String pHttpMethod, final Long pRequestTimeStamp, final String pNonce, final String pContent) {
        try {
            String requestUri = URLEncoder.encode(pUri, "utf-8").toLowerCase();


            String content = "";
            if (pContent != null && !pContent.equals("")) {
                content = Cryptography.md5(pContent);
                Log.d(TAG, "parameters in md5: " + content);
            }

            String data = MessageFormat.format("{0}{1}{2}{3}{4}{5}", pAppId, pHttpMethod, requestUri, pRequestTimeStamp.toString(), pNonce, content);
            Log.d(TAG, "signature data: " + data);

            String signature = Cryptography.HMAC_SHA256(pApiKey, data);
            Log.d(TAG, "signature: " + signature);

            return signature;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    protected String createCredential(String pAPI_URL, HashMap<String, String> pParameters) {
        String nonce = generateNonce();
        Log.d(TAG, "nonce = " + nonce);
        Long requestTimeStamp = getTimeStamp();
        Log.d(TAG, "requestTimeStamp = " + requestTimeStamp);

        String data = null;
        try {
            switch (mRequestType.getType()) {
                case GET:
                case PUT_URL:
                    pAPI_URL += "?" + mHttpRequest.ConvertToGetData(pParameters);
                    break;

                case POST:
                case PUT_CONTENT: // for put with parameters in content
                    data = mHttpRequest.ConvertParametersToString(pParameters);
                    Log.d(TAG, "parameters: " + data);
                    Log.d(TAG, "parameters length: " + data.length());
                    break;
            }

        } catch (Exception ex) {
            Log.d(TAG, "createCredential - UnsupportedEncodingException in method parameter");
        }

        String signature = createSignature(
                mAPI_KEY,
                mAPP_ID,
                pAPI_URL,
                mRequestType.toString(),
                requestTimeStamp,
                nonce,
                data);

        String credential = MessageFormat.format("{0}:{1}:{2}:{3}",
                mAPP_ID,
                signature,
                nonce,
                requestTimeStamp.toString());

        Log.d(TAG, "credential: " + credential);

        return credential;
    }

    protected static Long getTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    protected static String generateNonce() {
        return StringExt.newGUID();
    }

    //******************************************
    protected HttpRequest createRequest(final String pAPI_URL, HashMap<String, String> pParameters) {
        String credential = createCredential(pAPI_URL, pParameters);
        return new HttpRequest(true, mAuthenticationScheme, credential, mTimeout);
    }

    //******************************************
   /* protected void sendRequestAsync(final String methodUrl, final String method, final NameValue[] pParameters, final boolean pParameterIsJSON, final RequestType requestType, final boolean resultIsJSON) throws InvalidParameterException {
        class AsyncRequest extends AsyncTask<Void, Void, Integer> {
            @Override
            protected Integer doInBackground(Void... params) {
                mMethod = method;
                return sendRequest(methodUrl + "/" + method, pParameters, pParameterIsJSON, requestType, resultIsJSON);
            }

            @Override
            protected void onPostExecute(Integer pVoid) {
                onMyFragmentCallBack();
                Log.d("TAG", mMethod + " Callback");
            }
        }

        AsyncRequest asyncRequest = new AsyncRequest();
        asyncRequest.execute();
    }*/
        //******************************************
    protected ResponseWebService sendRequest(String methodUrl, final String method, final NameValue[] parameters, boolean parameterIsJSON, final RequestType requestType) throws Exception {
        Log.d(TAG, "sendRequest executing...");
        if (methodUrl != null) {
            methodUrl = methodUrl + "/" + method;

            mRequestType = requestType;
            mHttpRequest.setParameterInJSON(parameterIsJSON);
            //mResultIsJson = resultIsJSON;

            Log.d(TAG, methodUrl + " - doInBackground");

            // create hashParam
            HashMap<String, String> hashParam = null;
            if (parameters != null && parameters.length > 0) {
                hashParam = new HashMap<>();
                for (NameValue p : parameters) {
                    hashParam.put(p.getName(), p.getValue().toString());
                }
            }
            String credential = createCredential(methodUrl, hashParam);
            mHttpRequest.setAuthorizationCredential(credential);
            mHttpRequest.SendRequest(methodUrl, hashParam, requestType);

        } else {
            Log.d(TAG, "methodUrl or pAPI_URL are empty");
            throw new InvalidParameterException("methodUrl or pAPI_URL can't be empty");
        }

        Log.d(TAG, "sendRequest finished");
        return new ResponseWebService(getResponseCode(), getResponseData(), method);
    }

    //******************************************
   // public void setResponseCallBack(ICallBack pCallBack) {
    //    mResponseCallBack = pCallBack;
    //}

    public String getResponseData() {
        return mHttpRequest.getResponseResult();
    }

    public int getResponseCode() {
        return mHttpRequest.getResponseCode();
    }

    public boolean isParameterInJSON() {
        return mHttpRequest.isParameterInJSON();
    }

    //public String getMethod() {
     //   return mMethod.toLowerCase();
    //}
    //******************************************

    /*
    protected void onMyFragmentCallBack() throws NullPointerException {
        if (mResponseCallBack != null) {
           // if (getResponseCode() == Constant.ERROR) {
           //     mResponseCallBack.onMyFragmentCallBack(mMethod, getResponseCode(), getResponseData());
            //    return;
            //}
            mResponseCallBack.onMyFragmentCallBack(mMethod, getResponseCode(), getResponseData());
        } else {
            throw new NullPointerException("ResponseCallBack is null");
        }
    }*/

    //*****************************************
    /*public String getResponseCodeDescription() {
        String val = "";
        switch (getResponseCode()) {
            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;

            case HttpURLConnection.HTTP_ACCEPTED:
                val = "HTTP_ACCEPTED " + String.valueOf(HttpsURLConnection.HTTP_ACCEPTED);
                break;
        }
        return val;
    }

/*
        public static final int HTTP_ACCEPTED = 202;
        public static final int HTTP_BAD_GATEWAY = 502;
        public static final int HTTP_BAD_METHOD = 405;
        public static final int HTTP_BAD_REQUEST = 400;
        public static final int HTTP_CLIENT_TIMEOUT = 408;
        public static final int HTTP_CONFLICT = 409;
        public static final int HTTP_CREATED = 201;
        public static final int HTTP_ENTITY_TOO_LARGE = 413;
        public static final int HTTP_FORBIDDEN = 403;
        public static final int HTTP_GATEWAY_TIMEOUT = 504;
        public static final int HTTP_GONE = 410;
        public static final int HTTP_INTERNAL_ERROR = 500;
        public static final int HTTP_LENGTH_REQUIRED = 411;
        public static final int HTTP_MOVED_PERM = 301;
        public static final int HTTP_MOVED_TEMP = 302;
        public static final int HTTP_MULT_CHOICE = 300;
        public static final int HTTP_NOT_ACCEPTABLE = 406;
        public static final int HTTP_NOT_AUTHORITATIVE = 203;
        public static final int HTTP_NOT_FOUND = 404;
        public static final int HTTP_NOT_IMPLEMENTED = 501;
        public static final int HTTP_NOT_MODIFIED = 304;
        public static final int HTTP_NO_CONTENT = 204;
        public static final int HTTP_OK = 200;
        public static final int HTTP_PARTIAL = 206;
        public static final int HTTP_PAYMENT_REQUIRED = 402;
        public static final int HTTP_PRECON_FAILED = 412;
        public static final int HTTP_PROXY_AUTH = 407;
        public static final int HTTP_REQ_TOO_LONG = 414;
        public static final int HTTP_RESET = 205;
        public static final int HTTP_SEE_OTHER = 303; */
}
