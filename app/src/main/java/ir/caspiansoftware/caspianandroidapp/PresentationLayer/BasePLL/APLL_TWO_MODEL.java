package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.widget.ProgressBar;

import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;

/**
 * Created by Ghasem on 3/8/2017.
 */

public abstract class APLL_TWO_MODEL<TModel1, TModel2, TResult> extends APLL<TModel1, TResult> {

    private TModel2 mModel2;

    APLL_TWO_MODEL(Context context, IAsyncForm asyncForm, IFragmentCallback fragmentCallback) {
        super(context, asyncForm, fragmentCallback);
    }

    public void start(TModel1 model1, TModel2 model2) {
        if (model2 != null) {
            mModel2 = model2;

            super.start(model1);
        }
    }

    @Override
    public void start(TModel1 model1) {
        throw new UnsupportedOperationException("start method with one model is not applicable");
    }

    public TModel2 getModel2() {
        return mModel2;
    }

    public void setModel2(TModel2 model2) {
        mModel2 = model2;
    }
}
