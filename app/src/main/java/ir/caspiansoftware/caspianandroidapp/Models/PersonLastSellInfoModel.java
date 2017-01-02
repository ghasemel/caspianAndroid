package ir.caspiansoftware.caspianandroidapp.Models;

/**
 * Created by Canada on 9/7/2016.
 */
public class PersonLastSellInfoModel {
    private String mLastDate;
    private int mFaktorNum;
    private double mSellPrice;
    //private double mPriceAfterAvance;
    private String mPersonCode;
    private double mAvancePresent;

    public String getLastDate() {
        return mLastDate;
    }

    public void setLastDate(String lastDate) {
        mLastDate = lastDate;
    }

    public int getFaktorNum() {
        return mFaktorNum;
    }

    public void setFaktorNum(int faktorNum) {
        mFaktorNum = faktorNum;
    }

    public double getSellPrice() {
        return mSellPrice;
    }

    public void setSellPrice(double sellPrice) {
        mSellPrice = sellPrice;
    }

    public double getPriceAfterAvance() {
       return mSellPrice - mSellPrice * mAvancePresent / 100;
    }

    public String getPersonCode() {
        return mPersonCode;
    }

    public void setPersonCode(String personCode) {
        mPersonCode = personCode;
    }

    public double getAvancePresent() {
        return mAvancePresent;
    }

    public void setAvancePresent(double avancePresent) {
        mAvancePresent = avancePresent;
    }
}
