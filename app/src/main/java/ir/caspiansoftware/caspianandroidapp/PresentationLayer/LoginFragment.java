package ir.caspiansoftware.caspianandroidapp.PresentationLayer;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.GoToForm;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.InitialSettingBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.UserBLL;
import info.elyasi.android.elyasilib.Constant;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 12/17/2015.
 */
public class LoginFragment extends CaspianFragment {

    private ProgressBar mProgressBar;
    private Button mLoginButton;
    private Button mExitButton;
    //private TextView mErrorLabel;
    private EditText mUserNameTextBox;
    private EditText mPasswordTextBox;
    private View mInitialSettingHidden;
    private int mInitialSettingHiddenCounter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void afterMapViews(View parentView) {
        mInitialSettingHiddenCounter = 0;
    }

    private void initialSettingHiddenCounterIncrement() {
        mInitialSettingHiddenCounter++;
        if (mInitialSettingHiddenCounter == 4) {
            Toast.makeText(getActivity().getApplicationContext(),
                    String.valueOf(mInitialSettingHiddenCounter), Toast.LENGTH_SHORT).show();
        }

        if (mInitialSettingHiddenCounter == 7) {
            mInitialSettingHiddenCounter = 0;
            GoToForm.goToInitialSettingPage(getActivity());
        }
    }

    protected void mapViews(View parentView) {
        mProgressBar = (ProgressBar) parentView.findViewById(R.id.login_progressBar);
        //UIUtility.setProgressBarColor(mProgressBar, getResources().getColor(R.color.progressbar));

        //mErrorLabel = (TextView) parentView.findViewById(R.id.login_error);

        mUserNameTextBox = (EditText) parentView.findViewById(R.id.login_userName);
        mPasswordTextBox = (EditText) parentView.findViewById(R.id.login_pass);

        mLoginButton = (Button) parentView.findViewById(R.id.btnLogin);
        mLoginButton.setOnClickListener(this);
        mLoginButton.setOnTouchListener(this);

        mExitButton = (Button) parentView.findViewById(R.id.btnExit);
        mExitButton.setOnClickListener(this);
        mExitButton.setOnTouchListener(this);

        mInitialSettingHidden = parentView.findViewById(R.id.initial_setting_hidden);
        mInitialSettingHidden.setOnClickListener(this);
    }

    // region IAsyncForm
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    private void buttonExit_onClick() {
        GoToForm.closeApp(getActivity());
    }

    @Override
    public void onClick(View pView) {
        if (pView.equals(mLoginButton)) {
            buttonLogin_onClick();
        } else if (pView.equals(mExitButton)) {
            buttonExit_onClick();
        } else if (pView.equals(mInitialSettingHidden)) {
            initialSettingHiddenCounterIncrement();
        }
    }

    @Override
    public boolean onTouch(View sender, MotionEvent motionEvent) {
        UIUtility.onTouchEffect(sender, motionEvent);
        return false;
    }

    // region login
    private void buttonLogin_onClick() {

        if (mUserNameTextBox.getText().toString().trim().equals(Constant.STRING_EMPTY)) {
            Toast.makeText(getActivity(), R.string.login_emptyUserName, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (mPasswordTextBox.getText().toString().trim().equals(Constant.STRING_EMPTY)) {
            Toast.makeText(getActivity(), R.string.login_emptyPassword, Toast.LENGTH_LONG)
                    .show();
            return;
        }


        // load initial setting
        if (InitialSettingBLL.load() == null) {
            GoToForm.goToInitialSettingPage(getActivity());
            return;
        }


        startProgress();

        class RunAsync extends AAsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    UserBLL userBLL = new UserBLL(getActivity().getApplicationContext());
                    return userBLL.login(params[0], params[1]);
                } catch (Exception ex) {
                    setException(ex);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean response) {
                stopProgress();

                if (isException()) {
                    showError(getException(), null);
                } else {
                    GoToForm.goToMainPage(getActivity());
                }
            }
        }

        RunAsync runAsync = new RunAsync();
        runAsync.execute(PersianConvert.ConvertDigitsToLatin(mUserNameTextBox.getText()), PersianConvert.ConvertDigitsToLatin(mPasswordTextBox.getText()));
    }


    // endregion
}
