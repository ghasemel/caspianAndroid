package ir.caspiansoftware.caspianandroidapp.Models;

import info.elyasi.android.elyasilib.Persian.PersianConvert;

/**
 * Created by Canada on 3/3/2016.
 */
public class YearMaliModel {
    private int mId;
    private int mUserId;
    private int mYear;
    private int mDaftar;
    private String mCompany;
    private String mDataBase;
    private boolean mIsCurrent;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getDaftar() {
        return mDaftar;
    }

    public void setDaftar(int daftar) {
        mDaftar = daftar;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String company) {
        mCompany = company;
    }

    public String getDataBase() {
        return mDataBase;
    }

    public void setDataBase(String dataBase) {
        mDataBase = dataBase;
    }

    public boolean isCurrent() {
        return mIsCurrent;
    }

    public void setCurrent(boolean current) {
        mIsCurrent = current;
    }

    @Override
    public String toString() {
        //return PersianConvert.ConvertNumbersToPersian(getCompany() + " - دفتر: " + getDaftar() + " - سال مالی: " + getYear());
        return getCompany() + " - دفتر: " + getDaftar() + " - سال مالی: " + getYear();
    }
}
