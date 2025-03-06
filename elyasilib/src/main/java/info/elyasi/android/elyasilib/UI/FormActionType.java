package info.elyasi.android.elyasilib.UI;

import java.io.Serializable;

/**
 * Created by Canada on 7/24/2016.
 */
public enum FormActionType implements Serializable {
    //Add,
    Edit,
    New,
    VIEW,
    SELECT_BED,
    SELECT_BES;
    //Select
//
//    public static final String Add = "add";
//    public static final String Edit = "edit";

    public static final String EXTRA_ACTION_TYPE = "EXTRA_ACTION_TYPE";
}
