package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.util.Log;

import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;

/**
 * Created by Ghasem on 3/8/2017.
 */

public class DaftarTafPLL {
    public static final String TAG = "DaftarTafPLL";

    private Context mContext;
    private IAsyncForm mAsyncForm;
    private IFragmentCallback mFragmentCallback;

    private PersonModel mPersonModel;

    public DaftarTafPLL(Context context, IAsyncForm asyncForm, IFragmentCallback fragmentCallback) {
        mContext = context;
        mAsyncForm = asyncForm;
        mFragmentCallback = fragmentCallback;
    }

    public void start(PersonModel personModel) {
        Log.d(TAG, "start()");

        if (personModel != null) {

        }
    }
}
