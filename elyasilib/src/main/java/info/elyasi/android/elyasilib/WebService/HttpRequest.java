package info.elyasi.android.elyasilib.WebService;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import info.elyasi.android.elyasilib.Utility.JsonExt;

/**
 * Created by Canada on 12/18/2015.
 */
public class HttpRequest {
    private static final String TAG = "HttpRequest";
    public static final int ERROR = -1;


    private boolean mParameterInJSON = true;
    private boolean mAuthorization = false;
    private String mAuthorizationScheme = "";
    private String mAuthorizationCredential = "";
    private static final String sContentType = "application/json";

    private int mResponseCode = 0;
    private String mResponseResult = "";
    private int mTimeout; // in millisecond

    public HttpRequest() {
    }

    public HttpRequest(boolean pAuthorization, String pAuthorizationScheme, String pAuthorizationCredential, int pTimeout) {
        mAuthorization = pAuthorization;
        mAuthorizationScheme = pAuthorizationScheme;
        mAuthorizationCredential = pAuthorizationCredential;
        mTimeout = pTimeout;
    }

    public boolean isParameterInJSON() {
        return mParameterInJSON;
    }

    public void setParameterInJSON(boolean value) {
        mParameterInJSON = value;
    }

    // HTTP GET request
    private HttpURLConnection createGetConnection(String requestURL, HashMap<String, String> pGetParams) throws Exception {
        requestURL += "?" + ConvertToGetData(pGetParams);
        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setRequestProperty("Accept", sContentType);

        conn.setReadTimeout(mTimeout);
        conn.setConnectTimeout(mTimeout);
        if (mAuthorization) {
            conn.setRequestProperty("Authorization", mAuthorizationScheme + " " + mAuthorizationCredential);
        }

        return conn;
    }

    // Http POST AsyncRequest
    private HttpURLConnection createPostConnection(String requestURL, HashMap<String, String> pPostParams) throws Exception {
        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", sContentType);

        conn.setReadTimeout(mTimeout);
        conn.setConnectTimeout(mTimeout);
        if (mAuthorization) {
            conn.setRequestProperty("Authorization", mAuthorizationScheme + " " + mAuthorizationCredential);
        }

        String data = ConvertParametersToString(pPostParams);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(data);

        writer.flush();
        writer.close();
        os.close();

        return conn;
    }

    // Http PUT AsyncRequest with parameters in content
    private HttpURLConnection createPutConnectionWithContent(String requestURL, HashMap<String, String> pPutParams) throws Exception {
        requestURL += "?" + ConvertToGetData(pPutParams);
        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", sContentType);

        conn.setReadTimeout(mTimeout);
        conn.setConnectTimeout(mTimeout);
        if (mAuthorization) {
            conn.setRequestProperty("Authorization", mAuthorizationScheme + " " + mAuthorizationCredential);
        }

        String data = ConvertParametersToString(pPutParams);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(data);

        writer.flush();
        writer.close();
        os.close();

        return conn;
    }

    // Http PUT AsyncRequest with parameters in url
    private HttpURLConnection createPutConnection(String requestURL, HashMap<String, String> pPutParams) throws Exception {
        requestURL += "?" + ConvertToGetData(pPutParams);
        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PUT");
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", sContentType);

        conn.setReadTimeout(mTimeout);
        conn.setConnectTimeout(mTimeout);
        if (mAuthorization) {
            conn.setRequestProperty("Authorization", mAuthorizationScheme + " " + mAuthorizationCredential);
        }

        return conn;
    }

    public String SendRequest(String requestURL, HashMap<String, String> params, final RequestType pRequestMethod) throws Exception {
        Log.d(TAG, "AsyncRequest executing...");

        //try {
            HttpURLConnection conn = null;
            switch (pRequestMethod.getType()) {
                case GET:
                    conn = createGetConnection(requestURL, params);
                    break;

                case POST:
                    conn = createPostConnection(requestURL, params);
                    break;

                case PUT_URL:
                    conn = createPutConnection(requestURL, params);
                    break;

                case PUT_CONTENT:
                    conn = createPutConnectionWithContent(requestURL, params);
                    break;

                case DELETE:
                    break;
            }

            if (conn == null) {
                throw new Exception("HttpURLConnection value is null");
            }

            mResponseResult = "";
            mResponseCode = conn.getResponseCode();
            if (mResponseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    mResponseResult += line;
                }

                conn.disconnect();
            } else {
                String msg = "";
                if (conn.getErrorStream() != null) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    while ((line = br.readLine()) != null) {
                        msg += line;
                    }
                }
                if (msg.isEmpty()) {
                    msg = conn.getResponseMessage();
                }

                conn.disconnect();
                Log.d(TAG, "ResponseCode: " + mResponseCode + ", Message: " + msg);
                throw new Exception("ResponseCode: " + mResponseCode + ", Message: " + msg);
            }

        //} catch (Exception e) {
            //e.printStackTrace();
            //mResponseCode = ERROR;
            //mResponseResult = ErrorExt.ExtractExceptionDetail(e);
        //}

        Log.d(TAG, "AsyncRequest finished");
        return mResponseResult;
    }

    public String ConvertParametersToString(HashMap<String, String> params) throws UnsupportedEncodingException, JSONException {
        String result = "";
        if (params != null) {
            if (mParameterInJSON) {
                JSONObject json = new JSONObject();

                for (Map.Entry<String, String> p : params.entrySet()) {
                    if (JsonExt.isJSONArray(p.getValue()))
                        json.put(p.getKey(), JsonExt.toJsonArray(p.getValue()));
                    else
                        json.put(p.getKey(), p.getValue());
                }
                result = json.toString();
                //result = new JSONObject(params).toString();
            } else {
                for (Map.Entry<String, String> entry: params.entrySet()) {
                    if (!result.equals(""))
                        result += ",";
                    result += entry.getValue();
                }
            }
        }
        return result;
    }

    public String ConvertToGetData(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }

        return result.toString();
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getResponseResult() {
        return mResponseResult;
    }

    public boolean isAuthorization() {
        return mAuthorization;
    }

    public void setAuthorization(boolean pAuthorization) {
        mAuthorization = pAuthorization;
    }

    /*
    public RequestType getRequestMethod() {
        return mRequestMethod;
    }

    public void setRequestMethod(RequestType pRequestMethod) {
        mRequestMethod = pRequestMethod;
    }*/

    public String getAuthorizationScheme() {
        return mAuthorizationScheme;
    }

    public void setAutorizationScheme(String pAutorizationScheme) {
        mAuthorizationScheme = pAutorizationScheme;
    }

    public String getAuthorizationCredential() {
        return mAuthorizationCredential;
    }

    public void setAuthorizationCredential(String pAutorizationCredential) {
        mAuthorizationCredential = pAutorizationCredential;
    }

    public String getContentType() {
        return sContentType;
    }

//    public void setContentType(String pContentType) {
//        sContentType = pContentType;
//    }


}
