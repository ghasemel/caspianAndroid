package ir.caspiansoftware.caspianandroidapp.PresentationLayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Security.Cryptography;
import info.elyasi.android.elyasilib.Persian.PersianDate;
import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.UIFilter;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.GoToForm;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.DatabaseBackup;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.InitialSettingBLL;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.CaspianDataBaseHelper;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.KalaDataSource;
import ir.caspiansoftware.caspianandroidapp.Setting;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 6/4/2016.
 */
public class InitialSettingFragment extends CaspianFragment implements IFragmentCallback {
    private static final String TAG = "InitialSettingFragment";

    private Button mSaveButton;
    private Button mCancelButton;
    private Button mBackupButton;
    private EditText mIPEditText;
    private TextView mApiEditText;
    private TextView mDeviceIdEditText;
    private EditText mPortEditText;

    private ImageView mBtnApiKeyGenerator;
    private ImageView mBtnAppIdGenerator;

    private View mCaspianLogo;
    private int mCaspianLogoCounter = 0;

    private IActivityCallback mActivityCallback;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_initial_setting;
    }

    @Override
    protected void mapViews(View parentView) {
        mCancelButton = (Button) parentView.findViewById(R.id.initial_btnCancel);
        mCancelButton.setOnClickListener(this);
        mCancelButton.setOnTouchListener(this);

        mBackupButton = (Button) parentView.findViewById(R.id.initial_btnBackup);
        mBackupButton.setOnClickListener(this);
        mBackupButton.setOnTouchListener(this);

        mSaveButton = (Button) parentView.findViewById(R.id.initial_btnSave);
        mSaveButton.setOnClickListener(this);
        mSaveButton.setOnTouchListener(this);

        mIPEditText = (EditText) parentView.findViewById(R.id.initial_ip);
        mApiEditText = (TextView) parentView.findViewById(R.id.initial_api_key);
        mDeviceIdEditText = (TextView) parentView.findViewById(R.id.initial_device_id);
        mPortEditText = (EditText) parentView.findViewById(R.id.initial_port);

        mIPEditText.setFilters(UIFilter.getFilterForIP());

        mBtnApiKeyGenerator = (ImageView) parentView.findViewById(R.id.initial_btnApiKeyGenerate);
        mBtnApiKeyGenerator.setOnClickListener(this);
        mBtnApiKeyGenerator.setOnTouchListener(this);

        mBtnAppIdGenerator = (ImageView) parentView.findViewById(R.id.initial_btnDeviceIdGenerate);
        mBtnAppIdGenerator.setOnClickListener(this);
        mBtnAppIdGenerator.setOnTouchListener(this);

        mCaspianLogo = parentView.findViewById(R.id.caspian_logo);
        mCaspianLogo.setOnClickListener(this);

        // load current value
        mIPEditText.setText(SettingWebService.getIP());
        mApiEditText.setText(SettingWebService.getApiKey());
        mDeviceIdEditText.setText(SettingWebService.getDeviceId());
        mPortEditText.setText(SettingWebService.getPortString());
    }

    @Override
    public ProgressBar getProgressBar() { return null; }

    @Override
    public void onClick(View view) {
        Log.d(TAG, view.toString());

        if (view.equals(mCancelButton)) {
            cancel();
        } else if (view.equals(mSaveButton)) {
            save();
        } else if (view.equals(mBackupButton)) {
            shareDatabaseBackup();
        } else if (view.equals(mBtnApiKeyGenerator)) {
            mActivityCallback.onMyFragmentCallBack(InitialSettingActivity.ACTION_API_GENERATOR, null, (Object) null);
        } else if (view.equals(mBtnAppIdGenerator)) {
            mActivityCallback.onMyFragmentCallBack(InitialSettingActivity.ACTION_DEVICE_ID_GENERATOR, null, (Object) null);
        } else if (view.equals(mCaspianLogo)) {
            caspianLogoCounterIncrement();
        }
    }

    @Override
    public boolean onTouch(View sender, MotionEvent motionEvent) {
        UIUtility.onTouchEffect(sender, motionEvent);
        return false;
    }

    /**
     * Copies the local SQLite database and hands it to the Android share sheet
     * so a user can send it to support.
     *
     * This is diagnostic tooling. Some faults -- a FOREIGN KEY constraint
     * failure during upload, for instance -- live in one user's data and cannot
     * be reproduced or reasoned about from the code alone. Getting the actual
     * database beats guessing at the cause.
     */
    private void shareDatabaseBackup() {
        try {
            Context context = getActivity().getApplicationContext();

            // Flush the journal first. There is a caspian_db.sqlite-journal
            // beside the database, and copying only the main file can produce a
            // snapshot missing the newest writes -- which for a data
            // investigation is exactly the part that matters.
            checkpointDatabase(context);

            File source = context.getDatabasePath(Setting.CASPIAN_DB);
            if (!source.exists()) {
                showError(getString(R.string.initial_backup_failed), null);
                return;
            }

            // Naming and the byte copy live in DatabaseBackup so they can be
            // unit tested; see DatabaseBackupTest.
            File target = new File(context.getFilesDir(),
                    DatabaseBackup.buildFileName(
                            SettingWebService.getDeviceId(), PersianDate.getToday()));

            DatabaseBackup.copyFile(source, target);

            Uri uri = FileProvider.getUriForFile(
                    context, context.getPackageName() + ".provider", target);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("application/octet-stream");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.putExtra(Intent.EXTRA_SUBJECT, target.getName());
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(
                    share, getString(R.string.initial_backup_share)));

        } catch (Exception ex) {
            Log.e(TAG, "shareDatabaseBackup failed", ex);
            showError(ex, null);
        }
    }

    /**
     * Opens and closes a connection so SQLite writes the journal back into the
     * main database file before it is copied.
     */
    private void checkpointDatabase(Context context) {
        KalaDataSource dataSource = new KalaDataSource(context);
        try {
            dataSource.open();
        } finally {
            dataSource.close();
        }
    }

    private void caspianLogoCounterIncrement() {
        mCaspianLogoCounter++;
        if (mCaspianLogoCounter == 4) {
            Toast.makeText(getActivity().getApplicationContext(),
                    String.valueOf(mCaspianLogoCounter), Toast.LENGTH_SHORT).show();
        }

        if (mCaspianLogoCounter == 7) {
            mCaspianLogoCounter = 0;
            messageBoxYesNo(R.string.reset_app_title, R.string.reset_app_question, R.drawable.warning,
                    new IDialogCallback() {
                        @Override
                        public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
                            if (dialogResult == DialogResult.Yes) {
                                CaspianDataBaseHelper.DropDataBase(getActivity().getApplicationContext());
                                Vars.USER = null;
                                Vars.YEAR = null;
                                messageBoxOK(R.string.reset_app_title, R.string.successful_operation, new IDialogCallback() {
                                    @Override
                                    public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
                                        GoToForm.goToFirstPage(getActivity());
                                    }
                                });
                            }
                        }
                    });
        }
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionType formActionType) {
        Log.d(TAG, parameter.toString());

        try {
            String key = Cryptography.md5Base64(parameter.toString());
            switch (actionName) {
                case InitialSettingActivity.ACTION_API_GENERATOR:
                    mApiEditText.setText(key);
                    break;


                case InitialSettingActivity.ACTION_DEVICE_ID_GENERATOR:
                    mDeviceIdEditText.setText(key);
                    break;
            }
        } catch (Exception ex) {
            showError(ex, null);
        }

//        if (parameter instanceof String[]) {
//            String[] values = (String[]) parameter;
//            if (values.length == 2) {
//                Log.d(TAG, values[0] + " - " + values[1]);
//
//                if (!values[1].equals("cancel")) {
//                    String key = "";
//                    try {
//                        key = Cryptography.md5Base64(values[1]);
//                    } catch (Exception ex) { }
//
//
//                    if (values[0].equals(InitialSettingActivity.ACTION_API_GENERATOR)) {
//                        mApiEditText.setText(key);
//                    } else if (values[0].equals(InitialSettingActivity.ACTION_DEVICE_ID_GENERATOR)) {
//                        mDeviceIdEditText.setText(key);
//                    }
//                }
//            }
//        }
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
    //public String getUserName() {
    //    if (getActivity().getIntent().hasExtra(InitialSettingFragment.EXTRA_USERNAME))
    //        return getActivity().getIntent().getStringExtra(InitialSettingFragment.EXTRA_USERNAME);
    //    return null;
    //}

    private void save() {
        //String[] ss = mIPEditText.getText().toString().split("\\.");
        if (mIPEditText.getText().toString().split("\\.").length != 4) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.initial_ip_error, Toast.LENGTH_LONG).show();
            return;
        }

        if (mApiEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.initial_API_error, Toast.LENGTH_LONG).show();
            return;
        }

        if (mDeviceIdEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.initial_DeviceID_error, Toast.LENGTH_LONG).show();
            return;
        }

        if (mPortEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.initial_port_error, Toast.LENGTH_LONG).show();
            return;
        }


        //if (getUserName() != null) {
        InitialSettingBLL.setIP(mIPEditText.getText().toString());
        InitialSettingBLL.setApiKey(mApiEditText.getText().toString());
        InitialSettingBLL.setDeviceId(mDeviceIdEditText.getText().toString());
        InitialSettingBLL.setPort(Integer.parseInt(mPortEditText.getText().toString()));

        try {
            InitialSettingBLL.save();

            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.success_save, Toast.LENGTH_LONG).show();

            GoToForm.goToLoginPage(getActivity());
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.failed, Toast.LENGTH_LONG).show();
        }
        //}
    }

    private void cancel() {
        GoToForm.goToLoginPage(getActivity());
    }
}
