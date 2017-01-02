package ir.caspiansoftware.caspianandroidapp.Models;

import java.util.Date;

/**
 * Created by Canada on 2/9/2016.
 */
public class UserModel {

    private int mUserId;
    private String mUserName;
    private String mName;
    private boolean mActive;
    private Date mLastLoginDate;
    private boolean mLogon;
    private int mVisitorCode;
    private char mKalaPriceType;


    public UserModel() {
        mUserId = -1;
        mUserName = "";
        mKalaPriceType = 'N';
       // mLastLoginDate = new Date();
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public Date getLastLoginDate() {
        return mLastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        mLastLoginDate = lastLoginDate;
    }

    public void setLogon(boolean logon) {
        mLogon = logon;
    }

    public boolean isLogon() {
        return mLogon;
    }

    public void setName(String pName) {
        mName = pName;
    }

    public String getName() {
        return mName;
    }

    public void setActive(boolean pActive) {
        mActive = pActive;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setVisitorCode(int code) {
        mVisitorCode = code;
    }

    public int getVisitorCode() {
        return mVisitorCode;
    }

    public char getKalaPriceType() {
        return mKalaPriceType;
    }

    public void setKalaPriceType(char kalaPriceType) {
        mKalaPriceType = kalaPriceType;
    }
}
