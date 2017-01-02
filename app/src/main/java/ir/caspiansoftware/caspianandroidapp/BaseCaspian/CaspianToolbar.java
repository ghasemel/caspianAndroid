package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.view.View;

import info.elyasi.android.elyasilib.UI.UIUtility;

/**
 * Created by Canada on 3/9/2016.
 */
public class CaspianToolbar {

    public static void setToolbar(View.OnClickListener fragment, View toolbarView) {
        UIUtility.setIncludedButtonsEffect(toolbarView, fragment);
    }
}
