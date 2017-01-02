package ir.caspiansoftware.caspianandroidapp.Models;

import java.io.Serializable;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaModel implements Serializable {
    private int mID;
    private int mYearId_FK;
    private String mCode;
    private String mName;
    private String mVahedA;
    private String mVahedF;
    private double mMojoodi;
    private double mMohtavi;
    private double mPrice1;
    private double mPrice2;
    private double mPrice3;
    private double mPriceOmde;

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public int getYearId_FK() {
        return mYearId_FK;
    }

    public void setYearId_FK(int yearId_FK) {
        mYearId_FK = yearId_FK;
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

    public String getVahedA() {
        return mVahedA;
    }

    public void setVahedA(String vahedA) {
        mVahedA = vahedA;
    }

    public String getVahedF() {
        return mVahedF;
    }

    public void setVahedF(String vahedF) {
        mVahedF = vahedF;
    }

    public double getMojoodi() {
        return mMojoodi;
    }

    public void setMojoodi(double mojoodi) {
        mMojoodi = mojoodi;
    }

    public double getMohtavi() {
        return mMohtavi;
    }

    public void setMohtavi(double mohtavi) {
        mMohtavi = mohtavi;
    }

    public double getPrice1() {
        return mPrice1;
    }

    public void setPrice1(double price1) {
        mPrice1 = price1;
    }

    public double getPrice2() {
        return mPrice2;
    }

    public void setPrice2(double price2) {
        mPrice2 = price2;
    }

    public double getPrice3() {
        return mPrice3;
    }

    public void setPrice3(double price3) {
        mPrice3 = price3;
    }

    public double getPriceOmde() {
        return mPriceOmde;
    }

    public void setPriceOmde(double priceOmde) {
        mPriceOmde = priceOmde;
    }

    public long getActivePrice(char kalaPriceType) {
        switch (kalaPriceType) {
            case '1': return (long) getPrice1();
            case '2': return (long) getPrice2();
            case '3': return (long) getPrice3();
            case 'O': return (long) getPriceOmde();
            default: return 0;
        }
    }
}
