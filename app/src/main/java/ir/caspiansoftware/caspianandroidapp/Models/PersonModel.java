package ir.caspiansoftware.caspianandroidapp.Models;

import java.io.Serializable;

/**
 * Created by Canada on 3/10/2016.
 */
public class PersonModel implements Serializable {

    private int mYearId_FK;
    private int mId;
    private String mCode;
    private String mName;
    private double mMande;
    private String tel;
    private String mobile;
    private String Address;

    public int getYearId_FK() {
        return mYearId_FK;
    }

    public void setYearId_FK(int yearId_FK) {
        mYearId_FK = yearId_FK;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getMande() {
        return mMande;
    }

    public void setMande(double mande) {
        mMande = mande;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
