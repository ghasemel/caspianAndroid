package ir.caspiansoftware.caspianandroidapp.Models;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import info.elyasi.android.elyasilib.Utility.IJson;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.Enum.MaliType;
import ir.caspiansoftware.caspianandroidapp.Vars;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Canada on 7/22/2016.
 */
@Getter
@Setter
public class MaliModel implements Serializable, Comparable<MaliModel>, IJson {
    private int id;
    private int yearId_FK;
    private int num;
    private MaliType maliType;
    private Integer personBedId_FK;
    private PersonModel personBedModel;

    private Integer personBesId_FK;
    private PersonModel personBesModel;

    private String maliDate;
    private String description;

    private String vcheckSarresidDate;
    private String vcheckBank;
    private String vcheckSerial;

    private long amount;

    private double lat;
    private double lon;

    private boolean synced;
    private String syncDate;
    private int atfNum;

    private Date createDate;

    public void setPersonBedModel(PersonModel personModel) {
        personBedModel = personModel;
        if (personBedModel != null) {
            personBedId_FK = personBedModel.getId();
        }
    }

    public void setPersonBesModel(PersonModel personModel) {
        personBesModel = personModel;
        if (personBesModel != null) {
            personBesId_FK = personBesModel.getId();
        }
    }

    public String getAmountString() {
        return NumberExt.DigitSeparator(amount);
    }

    public String getCreateDateInIsoFormat() {
        return Vars.iso8601Format.format(getCreateDate());
    }

    
    @Override
    public int compareTo(@NonNull MaliModel maliModel) {
        return 0;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        if (getPersonBesModel() == null)
            throw new RuntimeException(CaspianErrors.MALI_BES_INVALID);

        json.put("maliId", id);
        json.put("maliType", maliType.getValue());
        json.put("codeBes", personBesModel.getCode());
        json.put("codeBed", personBedModel != null ? personBedModel.getCode() : "");
        json.put("maliDate", maliDate);
        json.put("description", description);
        json.put("vcheckSarresidDate", vcheckSarresidDate);
        json.put("vcheckBank", vcheckBank);
        json.put("vcheckSerial", vcheckSerial);
        json.put("amount", amount);
        json.put("lat", getLat());
        json.put("lon", getLon());

        if (getCreateDate() == null)
            setCreateDate(new Date(System.currentTimeMillis()));
        json.put("createDate", getCreateDateInIsoFormat());

        return json;
    }
}
