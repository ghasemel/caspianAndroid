package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import info.elyasi.android.elyasilib.UI.ASearchableListFragment;
import info.elyasi.android.elyasilib.Utility.AErrorHandler;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/22/2016.
 */
public abstract class CaspianSearchableListFragment<TModel> extends ASearchableListFragment<TModel> {




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

    @Override
    protected int getActionbarTitleTextViewId() {
        return R.id.actionbar_title;
    }
}
