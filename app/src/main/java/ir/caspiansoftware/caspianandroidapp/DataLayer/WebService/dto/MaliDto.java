package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.dto;

import java.io.Serializable;
import java.util.Date;

import ir.caspiansoftware.caspianandroidapp.Enum.MaliType;
import lombok.Data;

@Data
public class MaliDto implements Serializable {

    private int maliId;
    private MaliType maliType;
    private String codeBed;
    private String codeBes;
    private String maliDate;
    private String description;
    private String vcheckSarresidDate;
    private String vcheckBank;
    private String vcheckSerial;
    private long amount;
    private double lat;
    private double lon;
    private Date createDate;
}
