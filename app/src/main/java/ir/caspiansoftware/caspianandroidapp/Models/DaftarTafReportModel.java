package ir.caspiansoftware.caspianandroidapp.Models;

import java.io.Serializable;

/**
 * Created by Canada on 5/12/2017.
 */

public class DaftarTafReportModel implements Serializable {

    private String mTarikh;
    private String mDescription;
    private long mBed;
    private long mBes;
    private String mCode;
    private long mMande;
    private String mType;


    public String getTarikh() {
        return mTarikh;
    }

    public void setTarikh(String tarikh) {
        mTarikh = tarikh;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public long getBed() {
        return mBed;
    }

    public void setBed(long bed) {
        mBed = bed;
    }

    public long getBes() {
        return mBes;
    }

    public void setBes(long bes) {
        mBes = bes;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public long getMande() {
        return mMande;
    }

    public void setMande(long mande) {
        mMande = mande;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type.equals("BED") ? "بد" : "بس";
    }
}
