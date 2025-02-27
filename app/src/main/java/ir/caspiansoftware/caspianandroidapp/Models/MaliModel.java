package ir.caspiansoftware.caspianandroidapp.Models;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import info.elyasi.android.elyasilib.Utility.IJson;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/22/2016.
 */
public class MaliModel implements Serializable, Comparable<MaliModel>, IJson {
    private int mId;
    private int mYearId_FK;
    private int mNum;
    private int mPersonId_FK;
    private PersonModel mPersonModel;
    private String mDate;
    private String mDescription;

    private double mLat;
    private double mLon;

    private boolean mSynced;
    private String mSyncDate;
    private int mAtfNum;

    // related to view MPFaktorView
    private long mPriceTotal;
    private Date mCreateDate;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getYearId_FK() {
        return mYearId_FK;
    }

    public void setYearId_FK(int yearId_FK) {
        mYearId_FK = yearId_FK;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public PersonModel getPersonModel() {
        return mPersonModel;
    }

    public void setPersonModel(PersonModel personModel) {
        mPersonModel = personModel;
        if (mPersonModel != null) {
            setPersonId_FK(mPersonModel.getId());
        }
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getPersonId_FK() {
        return mPersonId_FK;
    }

    public void setPersonId_FK(int personId_FK) {
        mPersonId_FK = personId_FK;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isSynced() {
        return mSynced;
    }

    public void setSynced(boolean synced) {
        mSynced = synced;
    }

    public String getSyncDate() {
        return mSyncDate;
    }

    public void setSyncDate(String syncDate) {
        mSyncDate = syncDate;
    }

    public int getAtfNum() {
        return mAtfNum;
    }

    public void setAtfNum(int atfNum) {
        mAtfNum = atfNum;
    }


    public long getPriceTotal() {
        return mPriceTotal;
    }

    public String getPriceTotalString() {
        return NumberExt.DigitSeparator(getPriceTotal());
    }

    public void setPriceTotal(long priceTotal) {
        mPriceTotal = priceTotal;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        this.mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        this.mLon = lon;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public String getCreateDateInIsoFormat() {
        return Vars.iso8601Format.format(getCreateDate());
    }

    public void setCreateDate(Date mCreateDate) {
        this.mCreateDate = mCreateDate;
    }

    @Override
    public int compareTo(@NonNull MaliModel mpFaktorModel) {
        return 0;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        if (getPersonModel() == null)
            throw new RuntimeException(CaspianErrors.pfaktor_person_null);

        json.put("faktor_id", getId());
        json.put("person_code", getPersonModel().getCode());
        json.put("date", getDate());
        json.put("des", getDescription());
        json.put("lat", getLat());
        json.put("lon", getLon());

        if (getCreateDate() == null)
            setCreateDate(new Date(System.currentTimeMillis()));
        json.put("create_date", getCreateDateInIsoFormat());

        return json;
    }
}
