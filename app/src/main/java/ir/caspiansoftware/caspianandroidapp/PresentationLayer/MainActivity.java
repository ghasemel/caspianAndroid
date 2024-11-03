package ir.caspiansoftware.caspianandroidapp.PresentationLayer;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


import java.util.List;

import info.elyasi.android.elyasilib.UI.AListRowFragment;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivityTwoFragments;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.ErrorExt;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.GoToForm;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.UserBLL;
import ir.caspiansoftware.caspianandroidapp.Enum.SyncType;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Gallery.GalleryActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.SendPreInvoiceListPLL;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Sync.SyncPLL;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Sync.SyncTypeActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Confirm.PFaktorConfirmListActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Confirm.PFaktorConfirmListFragment;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.MojoodiList.KalaMojoodiListActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.MojoodiList.KalaMojoodiListFragment;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.DaftarTaf.DaftarTafActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.MandeList.PersonMandeListActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.MandeList.PersonMandeListFragment;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Year.YearListActivity;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;
import ir.caspiansoftware.caspianandroidapp.Vars;


/**
 * Created by Canada on 1/17/2016.
 */
public class MainActivity extends CaspianActivityTwoFragments {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_YEAR = 1;
    private static final int REQUEST_CODE_NEW_PFAKTOR = 2;
    private static final int REQUEST_CODE_SYNC_TYPE = 3;

    //private LinearLayout mBtnLogout;
    private LinearLayout mBtnExit;
    private IFragmentCallback mFragmentCallback;
    private IFragmentCallback mFragmentDetailCallback;

    //region ActivitySection
    @Override
    public void onCreate(Bundle savedBundleState) {
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_main, R.string.not_found);

        super.onCreate(savedBundleState);

        // exit button
        mBtnExit = (LinearLayout) findViewById(R.id.actionbar_main_exit);

        CaspianActionbar.setActionbarEvents(this);

        setActionbarTitleDefault();
    }

    // comes from background to foreground
    @Override
    public void onRestart() {
        super.onRestart();

        // call firstFragment to check validity of user
        // it will call when another activity is opened and now it is closed
        //FirstFragment.backToForegroundIsValid(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        // has a detail fragment
        if (this.getFragmentDetailContainer() != null && this.getFragmentDetailContainer().equals(fragment)) {
            if (findViewById(R.id.fragmentDetailContainer) == null) { //&& getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                //fragment.setRetainInstance(false);

                // on rotate to Patriot when we had two fragments already
                Log.d(TAG, "FragmentDetailContainer removed!");
                removeFragmentDetail();
            } else {
                setActionbarTitleForFragmentDetail(fragment);
                mFragmentDetailCallback = (IFragmentCallback) fragment;
            }
        } else {
            mFragmentCallback = (IFragmentCallback) fragment;
        }
    }
    //endregion ActivitySection

    @Override
    public Fragment createFragment() {
        return new MainMenuFragment();
    }

    @Override
    public void onClick(View sender) {
        // exit button
        if (sender.equals(mBtnExit)) {
            exit();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onMyFragmentCallBack(String actionName, FormActionTypes actionTypes, Object... parameter) {
        Log.d(TAG, "onMyFragmentCallBack() called: actionName = " + actionName);

        switch (actionName) {
            case Actions.ACTION_LOGOUT:
                logout();
                break;

            case Actions.ACTION_YEAR_MALI:
                showYearMaliForm();
                break;

            case Actions.ACTION_PRE_INVOICE:
                PFaktorConfirmListActivity.showNewInvoiceForResult(this, REQUEST_CODE_NEW_PFAKTOR);
                break;

            case Actions.ACTION_TOOLBAR_EXIT:
                removeDetail();
                break;

            case Actions.ACTION_SYNC:
                startSync();
                break;

            case Actions.ACTION_PRE_INVOICE_LIST:
                showPreInvoiceList();
                break;

            case Actions.ACTION_CONFIRM_LIST:
                showConfirmList();
                break;

            case Actions.ACTION_CONFIRM_PFaktor:
                if (parameter != null && parameter[0] instanceof List) {
                    confirmPreInvoice((List<MPFaktorModel>) parameter[0]);
                }
                break;

            case Actions.ACTION_CONFIRM_PFaktor_DONE:
                Log.d(TAG, "action =" + Actions.ACTION_CONFIRM_PFaktor_DONE);
                updatePFaktorConfirmList();
                break;

            case Actions.ACTION_KALA_MOJOODI_LIST:
                showKalaMojoodiListShow();
                break;

            case Actions.ACTION_PERSON_MANDE_LIST:
                showPersonMandeListShow();
                break;

            case Actions.ACTION_DAF_TAF:
                showDafTaf();
                break;

            case Actions.ACTION_OPEN_KALA_GALLERY:
                if (parameter != null && parameter.length > 0 && parameter[0] instanceof KalaModel)
                    showGalleryKala((KalaModel) parameter[0]);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_YEAR:
                if (resultCode != Activity.RESULT_CANCELED) {
                    setActionbarTitleDefault();
                    mFragmentCallback.onMyActivityCallback(Actions.ACTION_YEAR_MALI, null, null);
                }
                break;

            case REQUEST_CODE_NEW_PFAKTOR:
                updatePFaktorConfirmList();
                break;

            case REQUEST_CODE_SYNC_TYPE:
                if (data != null && data.getExtras() != null) {
                    SyncPLL syncPLL = new SyncPLL(getApplicationContext(),
                            (CaspianFragment) getFragmentContainer(),
                            (SyncType) data.getExtras().get(SyncTypeActivity.EXTRA_SYNC_TYPE),
                            this,
                            SettingWebService.getImageURL()
                    );
                    syncPLL.start();
                }
                break;
        }
    }

    private void confirmPreInvoice(List<MPFaktorModel> selectedInvoiceList) {
        Log.d(TAG, "confirmPreInvoice()");
        if (getFragmentContainer() instanceof IAsyncForm) {
            SendPreInvoiceListPLL pll =
                    new SendPreInvoiceListPLL
                            (
                                    getApplicationContext(),
                                    (IAsyncForm) getFragmentContainer(),
                                    this
                            );

            pll.start(selectedInvoiceList);
        }
    }

    private void showDafTaf() {
        Intent i = new Intent(getApplicationContext(), DaftarTafActivity.class);
        startActivity(i);
    }

    private void showKalaMojoodiListShow() {
        Log.d(TAG, "showKalaMojoodiListShow()");

        // create fragment instance
        KalaMojoodiListFragment fragment = new KalaMojoodiListFragment();

        // create activity instance
        Intent i = new Intent(this, KalaMojoodiListActivity.class);
        //i.putExtra(AListRowFragment.EXTRA_RETURN_NAME, "");

        try {
            // for tow fragment in one activity
            this.startDetailFragment(fragment, i);

            // for single fragment
            //startActivity(i);
        } catch (Exception ex) {
            ErrorExt errorExt = ErrorExt.get(ex);
            showError(errorExt.getUserMessage());
        }
    }

    private void showGalleryKala(KalaModel kala) {
        Log.d(TAG, "showGalleryKala()");

        // create activity instance
        Intent i = new Intent(this, GalleryActivity.class);
        i.putExtra(GalleryActivity.EXTRA_KALA, kala);

        startActivity(i);
    }

    private void showPersonMandeListShow() {
        Log.d(TAG, "showPersonMandeListShow()");

        // create fragment instance
        PersonMandeListFragment fragment = new PersonMandeListFragment();

        // create activity instance
        Intent i = new Intent(this, PersonMandeListActivity.class);
        //i.putExtra(AListRowFragment.EXTRA_RETURN_NAME, "");

        try {
            // for tow fragment in one activity
            this.startDetailFragment(fragment, i);

            // for single fragment
            //startActivity(i);
        } catch (Exception ex) {
            ErrorExt errorExt = ErrorExt.get(ex);
            showError(errorExt.getUserMessage());
        }
    }

    private void updatePFaktorConfirmList() {
        if (getFragmentDetailContainer() != null && getFragmentDetailContainer() instanceof IFragmentCallback) {
            ((IFragmentCallback) getFragmentDetailContainer())
                    .onMyActivityCallback(PFaktorConfirmListFragment.REFRESH_LIST, null, null);
        }
    }

    private void showConfirmList() {
        Log.d(TAG, "showConfirmList()");

        // create fragment instance
        PFaktorConfirmListFragment fragment = new PFaktorConfirmListFragment();

        // create activity instance
        Intent i = new Intent(this, PFaktorConfirmListActivity.class);

        try {
            // for tow fragment in one activity
            this.startDetailFragment(fragment, i);

            // for single fragment
            //startActivity(i);
        } catch (Exception ex) {
            ErrorExt errorExt = ErrorExt.get(ex);
            showError(errorExt.getUserMessage());
        }
    }

    private void removeDetail() {
        this.removeFragmentDetail();
        this.setActionbarTitleDefault();
    }


    // region menu actions *************************************************************************
    public void startSync() {
        Log.d(TAG, "startSync start");
        if (getFragmentContainer() != null && getFragmentContainer() instanceof CaspianFragment) {
            Intent i = new Intent(getApplicationContext(), SyncTypeActivity.class);
            startActivityForResult(i, REQUEST_CODE_SYNC_TYPE);
        }
    }

    public void showPreInvoiceList() {

    }

    public void showYearMaliForm() {
        Intent i = new Intent(this, YearListActivity.class);
        i.putExtra(AListRowFragment.EXTRA_RETURN_NAME, "");
        startActivityForResult(i, REQUEST_CODE_YEAR);
    }

    public void logout() {
        try {
            UserBLL userBLL = new UserBLL(getApplicationContext());
            userBLL.logout(Vars.USER);
            GoToForm.goToLoginPage(this);
        } catch (Exception ex) {
            ErrorExt errorExt = ErrorExt.get(ex);
            showError(errorExt.getUserMessage());
        }
    }

    private void exit() {
        try {
            GoToForm.closeApp(this);
        } catch (Exception ex) {
            ErrorExt errorExt = ErrorExt.get(ex);
            showError(errorExt.getUserMessage());
        }
    }
    // endregion menu actions

    // region actionbar ****************************************************************************
    private void setActionbarTitleDefault() {
        String title = Vars.YEAR != null ? Vars.YEAR.getCompany() :
                getResources().getString(R.string.app_full_title);

        setActionbarTitle(title, R.id.actionbar_title);
    }


    private void setActionbarTitleForFragmentDetail(Fragment fragment) {
        if (fragment instanceof PFaktorConfirmListFragment) {
            setActionbarTitle(R.string.preInvoice_list_title, R.id.actionbar_title);
        }
    }
    // endregion actionbar

    // for option menu on actionbar
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/


}
