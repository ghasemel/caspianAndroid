package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import info.elyasi.android.elyasilib.UI.AAsyncFragment;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Utility.AErrorHandler;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/8/2016.
 */
public abstract class CaspianFragment extends AAsyncFragment {
    private static final String TAG = "CaspianFragment";



    // region IAsyncForm
    protected TextView getErrorLabel() {
        return null;
    }

    protected View getStarterControl() {
        return null;
    }

    @Override
    protected AErrorHandler getErrorHandler() {
        return ErrorExt.get();
    }
    //endregion
}
