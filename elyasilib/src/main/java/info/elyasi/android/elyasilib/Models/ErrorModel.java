package info.elyasi.android.elyasilib.Models;

import android.content.ContentValues;
import android.content.Context;

import java.io.Serializable;


/**
 * Created by Canada on 7/28/2016.
 */
public class ErrorModel implements Serializable {

    private String mErrorMessage;
    private int mMessageId;
    private int mErrorNum;
    private IDoFormationOnMessage mFormationOnMessage;

    public ErrorModel() {

    }

    public ErrorModel(String errorMessage, int messageId, int errorNum) {
        setErrorMessage(errorMessage);
        setMessageId(messageId);
        setErrorNum(errorNum);
        mFormationOnMessage = null;
    }

    public ErrorModel(String errorMessage, int messageId, int errorNum, IDoFormationOnMessage doFormationOnMessage) {
        this(errorMessage, messageId, errorNum);
        mFormationOnMessage = doFormationOnMessage;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public void setMessageId(int messageId) {
        mMessageId = messageId;
    }


    public String getMessageString(Context context, String occurredErrorMessage) {
        if (mMessageId > 0) {
            String v = context.getString(mMessageId);
            if (mFormationOnMessage != null)
                return mFormationOnMessage.doFormatOnMessage(v, occurredErrorMessage);
            return v;
        }
        return mErrorMessage;
    }

    public int getErrorNum() {
        return mErrorNum;
    }

    public void setErrorNum(int errorNum) {
        mErrorNum = errorNum;
    }


    public interface IDoFormationOnMessage {
        String doFormatOnMessage(String message, String errorMessage);
    }
}
