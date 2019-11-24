package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Sync;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.List;

import info.elyasi.android.elyasilib.Constant;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Dialogs.ProgressDialog;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.KalaBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.KalaPhotoBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PersonBLL;
import ir.caspiansoftware.caspianandroidapp.Enum.SyncType;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.KalaPhotoModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.SettingWebService;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 6/22/2016.
 */
public class SyncPLL {
    private static final String TAG = "SyncPLL";
    private Context mContext;
    private CaspianFragment mAsyncForm;
    private int mCount;
    private ProgressDialog mProgressDialog;
    private boolean mCancel = false;
    private final SyncType syncType;
    private final String kalaUrl;

    public SyncPLL(Context context, CaspianFragment fragment, SyncType syncType, IActivityCallback activityCallback, String kalaUrl) {
        mContext = context;
        mAsyncForm = fragment;
        this.syncType = syncType;
        this.kalaUrl = kalaUrl;
    }

    private String syncKala(AAsyncTask<Void, String, String> task) {
        try {
            KalaBLL kalaBLL = new KalaBLL(mContext);
            List<KalaModel> kalaList = kalaBLL.FetchKalaList(Vars.YEAR.getDataBase());

            kalaBLL.DeleteNotExistInList(kalaList);
            for (int i = 0; i < kalaList.size(); i++) {

                if (mCancel)
                    return Constant.CANCEL;

                kalaBLL.SyncWithDatabase(kalaList.get(i));
                task.progress(kalaList.get(i).getName());
            }

        } catch (Exception ex) {
            Log.d(TAG, "syncKala exception");
            task.setException(ex);
        }

        return Constant.SUCCESS;
    }

    private String syncPerson(AAsyncTask<Void, String, String> task) {
        try {
            PersonBLL personBLL = new PersonBLL(mContext);
            List<PersonModel> personList = personBLL.FetchPersonList();

            personBLL.DeleteNotExistInList(personList);
            for (int i = 0; i < personList.size(); i++) {

                if (mCancel)
                    return Constant.CANCEL;

                personBLL.SyncWithDatabase(personList.get(i));
                task.progress(personList.get(i).getName());
            }

        } catch (Exception ex) {
            Log.d(TAG, "syncPerson exception");
            task.setException(ex);
        }

        return Constant.SUCCESS;
    }

    private String syncKalaPhoto(AAsyncTask<Void, String, String> task) {
        try {
            KalaPhotoBLL photoBLL = new KalaPhotoBLL(mContext);
            List<KalaPhotoModel> photoList = photoBLL.FetchKalaPhotosList(Vars.YEAR.getDataBase());

            photoBLL.DeleteNotExistInList(photoList);
            for (int i = 0; i < photoList.size(); i++) {

                if (mCancel)
                    return Constant.CANCEL;

                KalaPhotoModel photoModel = photoList.get(i);
                Bitmap bitmap = photoBLL.downloadImage(photoModel, kalaUrl);
                if (bitmap == null)
                    return Constant.FAILED;

                if (!photoBLL.saveImage(photoModel, bitmap, mAsyncForm.getContext()))
                    return Constant.FAILED;

                photoBLL.SyncWithDatabase(photoList.get(i));
                task.progress(photoList.get(i).getCode() + " " + photoList.get(i).getTitle());
            }

        } catch (Exception ex) {
            Log.d(TAG, "syncKalaPhoto exception");
            task.setException(ex);
        }

        return Constant.SUCCESS;
    }

    private void sync() {
        Log.d(TAG, "startSync called");
        mAsyncForm.startProgress();

        class RunAsync extends AAsyncTask<Void, String, String> {

            private RunAsync(ProgressBar progressBar) {
                super(progressBar);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

                mProgressDialog.performStep(values[0]);
            }

            @Override
            protected String doInBackground(Void... objects) {
                Log.d(TAG, "doInBackground(): entered the function");

                if (mCancel || mCount == 0)
                    return Constant.CANCEL;

                String result = Constant.CANCEL;
                switch (syncType) {
                    case KALA:
                        result = syncKala(this);
                        break;

                    case PERSON:
                        result = syncPerson(this);
                        break;

                    case KALA_PHOTO:
                        result = syncKalaPhoto(this);
                        break;
                }

                Log.d(TAG, "doInBackground(): end of the function");
                return result; //Constant.SUCCESS;
            }

            @Override
            protected void onPostExecute(String response) {
                Log.d(TAG, "onPostExecute(): entered the function");
                if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                    ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                }

                mAsyncForm.stopProgress();

                if (isException()) {
                    mAsyncForm.showError(getException(), null);
                } else {
                    Log.d(TAG, "sync - " + response);

                    if (response.equals(Constant.SUCCESS)) {
                        mAsyncForm.messageBoxOK(syncType.getSyncTitle(),
                                String.format(
                                        mContext.getString(syncType.getSyncSuccess()),
                                        mCount
                                ), null);
                    }
                }
                Log.d(TAG, "onPostExecute(): end of the function");
            }
        }

        if (Vars.USER != null) {
            RunAsync runAsync = new RunAsync(mAsyncForm.getProgressBar());
            runAsync.execute();
        }
    }


    public void start() {
        Log.d(TAG, "start called");
        mAsyncForm.startProgress();
        if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
            ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
        }


        class RunAsync extends AAsyncTask<Void, Integer, Integer> {


            @Override
            protected Integer doInBackground(Void... objects) {
                Log.d(TAG, "doInBackground(): entered the function");


                int count = 0;
                switch (syncType) {
                    case KALA:
                        try {
                            KalaBLL kalaBLL = new KalaBLL(mContext);
                            count = kalaBLL.FetchKalaListCount(Vars.YEAR.getDataBase());
                        } catch (Exception ex) {
                            setException(ex);
                        }
                        break;

                    case PERSON:
                        try {
                            PersonBLL personBLL = new PersonBLL(mContext);
                            count = personBLL.FetchPersonListCount();

                        } catch (Exception ex) {
                            setException(ex);
                        }
                        break;

                    case KALA_PHOTO:
                        try {
                            KalaPhotoBLL photoBLL = new KalaPhotoBLL(mContext);
                            count = photoBLL.FetchKalaPhotosCount(Vars.YEAR.getDataBase());
                        } catch (Exception ex) {
                            setException(ex);
                        }
                        break;
                }


                Log.d(TAG, "doInBackground(): end of the function");
                return count;
            }

            @Override
            protected void onPostExecute(Integer count) {
                Log.d(TAG, "onPostExecute(): entered the function");
                mAsyncForm.stopProgress();

                if (isException()) {
                    mAsyncForm.showError(getException(), null);
                } else {
                    Log.d(TAG, "startSync - " + count);
                    mCount = count;

                    if (mCount > 0) {
                        mAsyncForm.messageBoxYesNo(syncType.getSyncTitle(),
                                String.format(mContext.getString(syncType.getSyncQuestion()), mCount),
                                new CountDialogKalaPersonCallback());
                    } else {
                        if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                            ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                        }

                        mAsyncForm.messageBoxOK(syncType.getSyncTitle(),
                                mContext.getString(R.string.sync_not_exist_message),
                                null);
                    }

                }
                Log.d(TAG, "onPostExecute(): end of the function");
            }
        }

        if (Vars.USER != null) {
            RunAsync runAsync = new RunAsync();
            runAsync.execute();
        }


    }

    class CountDialogKalaPersonCallback implements IDialogCallback<Object> {

        @Override
        public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
            if (dialogResult == DialogResult.Yes) {
                mProgressDialog = new ProgressDialog();
                mProgressDialog.setTitle(mContext.getString(syncType.getSyncTitle()));
                mProgressDialog.setMax(mCount);
                mProgressDialog.setDialogCallback(new ProgressDialogCallback());

                //mContext.getResources().getInteger(R.integer.popup_width),
                //mContext.getResources().getInteger(R.integer.popup_height));

                mProgressDialog.show(mAsyncForm.getActivity().getFragmentManager(), "sync_info");

                sync();
            } else {
                if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                    ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                }
            }
        }
    }

    class ProgressDialogCallback implements IDialogCallback<Integer> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
            if (dialogResult == DialogResult.Cancel) {
                mCancel = true;
            }
        }
    }

}
