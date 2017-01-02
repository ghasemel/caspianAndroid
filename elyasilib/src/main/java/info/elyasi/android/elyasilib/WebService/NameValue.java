package info.elyasi.android.elyasilib.WebService;

/**
 * Created by Canada on 12/18/2015.
 */
public class NameValue<TValue> {
    private String mName;
    private TValue mValue;

    public NameValue(String pName, TValue pValue) {
        mName = pName;
        mValue = pValue;
    }

    public String getName() {
        return mName;
    }

    public TValue getValue() {
        return mValue;
    }
    public void setName(String pName) {
        mName = pName;
    }

    public void setValue(TValue pValue) {
        mValue = pValue;
    }
}
