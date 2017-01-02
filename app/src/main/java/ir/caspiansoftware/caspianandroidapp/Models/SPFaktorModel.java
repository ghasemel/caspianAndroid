package ir.caspiansoftware.caspianandroidapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

import info.elyasi.android.elyasilib.Utility.IJson;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;

/**
 * Created by Canada on 7/22/2016.
 */
public class SPFaktorModel implements Serializable, IJson {
    private int mId;
    private int mMPFaktorId_FK;
    private int mKalaId_FK;
    private KalaModel mKalaModel;
    private long mPrice;
    private double mSCount;
    private double mMCount;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getMPFaktorId_FK() {
        return mMPFaktorId_FK;
    }

    public void setMPFaktorId_FK(int MPFaktorId_FK) {
        mMPFaktorId_FK = MPFaktorId_FK;
    }

    public KalaModel getKalaModel() {
        return mKalaModel;
    }

    public void setKalaModel(KalaModel kalaModel) {
        mKalaModel = kalaModel;
        if (mKalaModel != null) {
            setKalaId_FK(mKalaModel.getID());
        }
    }

    public int getKalaId_FK() {
        return mKalaId_FK;
    }

    public void setKalaId_FK(int kalaId_FK) {
        mKalaId_FK = kalaId_FK;
    }

    public long getPrice() {
        return mPrice;
    }

    public void setPrice(long price) {
        mPrice = price;
    }

    public double getSCount() {
        return mSCount;
    }

    public void setSCount(double SCount) {
        mSCount = SCount;
    }

    public double getMCount() {
        return mMCount;
    }

    public void setMCount(double MCount) {
        mMCount = MCount;
    }

    public BigDecimal getTotalPrice() {
        return new BigDecimal(mSCount * mPrice);
    }

    public String getTotalPriceString() {
        return NumberExt.DigitSeparator(getTotalPrice());
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        if (getKalaModel() == null)
            throw new RuntimeException(CaspianErrors.pfaktor_kala_null);

        json.put("spfaktor_id", getId());
        json.put("kala_code", getKalaModel().getCode());
        json.put("price", getPrice());
        json.put("scount", getSCount());
        json.put("mcount", getMCount());

        return json;
    }


}
