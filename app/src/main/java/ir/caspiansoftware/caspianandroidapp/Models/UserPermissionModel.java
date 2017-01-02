package ir.caspiansoftware.caspianandroidapp.Models;

/**
 * Created by Canada on 2/9/2016.
 */
public class UserPermissionModel {
    private int mUserId;
    private String mPart;
    private boolean mWrite;
    private boolean mAccess;



    public UserPermissionModel() {
        mUserId = -1;
        mPart = "";
        mWrite = false;
        mAccess = false;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setPart(String part) {
        mPart = part;
    }

    public String getPart() {
        return mPart;
    }

    public void setWrite(boolean write) {
        mWrite = write;
    }

    public boolean isWrite() {
        return mWrite;
    }

    public void setAccess(boolean allow) {
        mAccess = allow;
    }

    public boolean isAccess() {
        return mAccess;
    }


}
