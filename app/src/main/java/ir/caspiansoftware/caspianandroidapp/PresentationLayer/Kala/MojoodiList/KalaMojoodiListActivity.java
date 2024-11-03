package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.MojoodiList;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import info.elyasi.android.elyasilib.UI.FormActionTypes;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Gallery.GalleryActivity;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/22/2016.
 */
public class KalaMojoodiListActivity extends CaspianActivitySingleFragment {



    @Override
    public void onCreate(Bundle savedBundleState) {
        //showAsPopup(this, 650, getResources().getInteger(R.integer.popup_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.kala_list_mojoodi);
        //forceRTLIfSupported();

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new KalaMojoodiListFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }

    @Override
    public void onMyFragmentCallBack(String actionName, FormActionTypes actionTypes, Object[] parameters) {
        switch (actionName) {
            case Actions.ACTION_TOOLBAR_EXIT:
                this.finish();
                break;

            case Actions.ACTION_OPEN_KALA_GALLERY:
                if (parameters != null && parameters.length > 0 && parameters[0] instanceof KalaModel) {
                    Intent i = new Intent(this, GalleryActivity.class);
                    i.putExtra(GalleryActivity.EXTRA_KALA, (KalaModel)parameters[0]);
                    startActivity(i);
                }
                break;
        }
    }
}
