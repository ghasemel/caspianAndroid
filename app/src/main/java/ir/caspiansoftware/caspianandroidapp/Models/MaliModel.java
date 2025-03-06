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
    private int personBedId_FK;
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

        if (getPersonBedModel() == null)
            throw new RuntimeException(CaspianErrors.mali_bed_null);

        json.put("mali_id", id);
        json.put("type", maliType.getValue());
        json.put("bed_code", personBedModel.getCode());
        json.put("bes_code", personBesModel != null ? personBesModel.getCode() : "");
        json.put("mali_date", maliDate);
        json.put("des", description);
        json.put("vcheck_sarresid", vcheckSarresidDate);
        json.put("vcheck_bank", vcheckBank);
        json.put("vcheck_serial", vcheckSerial);
        json.put("amount", amount);
        json.put("lat", getLat());
        json.put("lon", getLon());

        if (getCreateDate() == null)
            setCreateDate(new Date(System.currentTimeMillis()));
        json.put("create_date", getCreateDateInIsoFormat());

        return json;
    }
}
