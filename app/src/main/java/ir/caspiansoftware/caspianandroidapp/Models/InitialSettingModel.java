package ir.caspiansoftware.caspianandroidapp.Models;

/**
 * Created by Canada on 6/4/2016.
 */
public class InitialSettingModel {

    private String mName;
    private String mValue;
    //private String ApiKey;
    //private String AppId;
    //private int Port;

    public InitialSettingModel() {}

    public InitialSettingModel(String mName, String mValue) {
        this.mName = mName;
        this.mValue = mValue;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }




}
