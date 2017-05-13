package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.util.Log;

import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.UserBLL;
import ir.caspiansoftware.caspianandroidapp.Setting;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 6/22/2016.
 */

// it's no need to call
public class ShouldLogoutPLL {
    private static final String TAG = "ShouldLogoutPLL";

    public static void ShouldLogoutPLL(final Context context, final IAsyncForm fragment, final IActivityCallback activityCallback) {
        Log.d(TAG, "shouldLogout start");
        fragment.startProgress();

        class RunAsync extends AAsyncTask<Integer, Void, Boolean> {

            @Override
            protected Boolean doInBackground(Integer... params) {
                Log.d(TAG, "shouldLogout - doInBackground start");

                try {
                    UserBLL userBLL = new UserBLL(context);
                    return userBLL.checkForLogout(params[0]);
                } catch (Exception ex) {
                    setException(ex);
                } finally {
                    Log.d(TAG, "shouldLogout - doInBackground finished");
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean response) {
                Log.d(TAG, "shouldLogout - onPostExecute start");

                fragment.stopProgress();

                if (isException()) {
                    fragment.showError(getException(), null);
                } else {
                    if (response) {
                        activityCallback.fragment_callback(Actions.ACTION_LOGOUT, null, (Object) null);
                    }
                }
                Log.d(TAG, "shouldLogout - onPostExecute finished");
            }
        }

        if (Vars.USER != null) {
            RunAsync runAsync = new RunAsync();
            runAsync.execute(Vars.USER.getUserId());
        }
    }
}
