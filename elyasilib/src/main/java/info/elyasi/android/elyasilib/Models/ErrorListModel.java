package info.elyasi.android.elyasilib.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canada on 7/28/2016.
 */
public class ErrorListModel {
    List<ErrorModel> mSubErrorList;
    private String mListErrorMessage;
    private int mListErrorNum;
    private boolean mConsiderListErrorMessage;

    public ErrorListModel(String listErrorMessage, int listErrorNum, boolean considerListErrorMessage) {
        mListErrorMessage = listErrorMessage;
        mListErrorNum = listErrorNum;
        mSubErrorList = new ArrayList<>();
        mConsiderListErrorMessage = considerListErrorMessage;
    }

    public boolean hasErrorModel(String occurredErrorMessage) {
        return getErrorModel(occurredErrorMessage) != null;
    }

    public ErrorModel getErrorModel(String occurredErrorMessage) {
        if (mSubErrorList != null) {
            if (mConsiderListErrorMessage && !occurredErrorMessage.contains(mListErrorMessage)) {
                return null;
            }

            for (ErrorModel e : mSubErrorList) {
                if (occurredErrorMessage.contains(e.getErrorMessage()))
                    return e;
            }
        }
        return null;
    }

    public ErrorModel addSubError(String errorMessage, int messageId) {
        if (mSubErrorList != null) {
            ErrorModel errorModel = new ErrorModel(
                    errorMessage, messageId, mSubErrorList.size() + mListErrorNum
            );
            mSubErrorList.add(errorModel);
            return errorModel;
        }
        return null;
    }

    public ErrorModel addSubError(String errorMessage, int messageId, ErrorModel.IDoFormationOnMessage doFormationOnMessage) {
        if (mSubErrorList != null) {
            ErrorModel errorModel = new ErrorModel(
                    errorMessage, messageId, mSubErrorList.size() + mListErrorNum, doFormationOnMessage
            );
            mSubErrorList.add(errorModel);
            return errorModel;
        }
        return null;
    }
}
