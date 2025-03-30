package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.EntitySelection;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.Enum.EntityType;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Ghasem on 4/22/2017.
 */

public class EntityTypeSelectionFragment extends CaspianFragment implements IFragmentCallback {
    private static final String TAG = "DaftarTafFragment";

    private IActivityCallback mActivityCallback;

    private RadioButton rdPFaktor;
    private RadioButton rdMali;


    private Button mBtnOK;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_entity_type_selection;
    }

    @Override
    protected void mapViews(View parentView) {
        Log.d(TAG, "mapViews(): start...");

        rdPFaktor = parentView.findViewById(R.id.rdPFaktor);
        rdMali = parentView.findViewById(R.id.rdMali);

        rdPFaktor.setOnCheckedChangeListener(this::checkPFaktorChange);
        rdMali.setOnCheckedChangeListener(this::checkMaliChange);

        mBtnOK = parentView.findViewById(R.id.btn_OK);

        UIUtility.setButtonEffect(mBtnOK, this);
    }

    private void checkPFaktorChange(CompoundButton compoundButton, boolean b) {
        if (b) {
            rdMali.setChecked(false);
        }
    }

    private void checkMaliChange(CompoundButton compoundButton, boolean b) {
        if (b) {
            rdPFaktor.setChecked(false);
        }
    }

    @Override
    protected void afterMapViews(View parentView) {
        Log.d(TAG, "afterMapViews(): start...");
    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick(): start...");

        if (view.equals(mBtnOK)) {
            btnOK_click();
        }
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionType formActionType) {

    }

    private void btnOK_click() {
        Log.d(TAG, "btnOK_click(): enter");

        EntityType entityType = EntityType.PFAKTOR;
        if (rdMali.isChecked())
            entityType = EntityType.MALI;


        mActivityCallback.onMyFragmentCallBack(Actions.ACTION_ENTITY_SELECTION, null, entityType);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityCallback = (IActivityCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }
}
