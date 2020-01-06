package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PersonBLL;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonLastSellInfoModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 9/10/2016.
 */
public class LastSellPricePLL extends APLL_TWO_MODEL<PersonModel, KalaModel, PersonLastSellInfoModel>  {

    private static final String TAG = "LastSellPricePLL";
    /*private Context mContext;
    private IAsyncForm mAsyncForm;
    private IFragmentCallback mFragmentCallback;
    private boolean mCancel = false;
    private PersonModel mPerson;
    private KalaModel mKala;*/

    public LastSellPricePLL(Context context, IAsyncForm fragment, IFragmentCallback fragmentCallback) {
        super(context, fragment, fragmentCallback);
    }

    @Override
    protected int getMessageBoxTitle() {
        return R.string.person_last_sell_price_title;
    }

    @Override
    protected String getMessageBoxText() {
        return String.format(
                getContext().getString(R.string.person_last_sell_price_question),
                getModel2().getName(),
                getModel().getName()
        );
    }

    @Override
    protected void onCancelClick() {
        getFragmentCallback().onMyActivityCallback(Actions.ACTION_GET_LAST_SELL_PRICE, Activity.RESULT_CANCELED, null);
    }

    @Override
    protected PersonLastSellInfoModel doInBackgroundThread(RunAsync runAsync) throws Exception {
        Log.d(TAG, "doInBackgroundThread(): entered the function");

        PersonBLL personBLL = new PersonBLL(getContext());

        PersonLastSellInfoModel lastSellInfo =
                personBLL.FetchPersonLastSellInfo(getModel().getCode(), getModel2().getCode());

            if (lastSellInfo != null)
                runAsync.updateProgressbar(String.valueOf(String.valueOf(lastSellInfo.getSellPrice())));

        return lastSellInfo;
    }

    @Override
    protected void onBackgroundComplete(PersonLastSellInfoModel lastSellInfo) {
        Log.d(TAG, "onBackgroundComplete(): entered the function");
        getFragmentCallback().onMyActivityCallback(Actions.ACTION_GET_LAST_SELL_PRICE, lastSellInfo, null);
    }


    /* public void start(PersonModel person, KalaModel kala) {
        Log.d(TAG, "start()");
        if (person != null) {
            if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
            }

            mPerson = person;
            Log.d(TAG, "personCode: " + person.getCode());

            mKala = kala;
            Log.d(TAG, "kalaCode: " + kala.getCode());

            mAsyncForm.startProgress();

            mAsyncForm.messageBoxYesNo(
                    R.string.person_last_sell_price_title,
                    String.format(
                            mContext.getString(R.string.person_last_sell_price_question),
                            kala.getName(),
                            person.getName()
                    ),
                    new LastSellPriceDialogCallBack());

        }
    }*/

    /*private class LastSellPriceDialogCallBack implements IDialogCallback<Integer> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
            if (dialogResult != DialogResult.Yes) {
                mAsyncForm.stopProgress();
                if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                    ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                }
                mFragmentCallback.onMyActivityCallback(Actions.ACTION_GET_LAST_SELL_PRICE, Activity.RESULT_CANCELED, null);
                return;
            }

//            mProgressDialog = new ProgressDialog();
//            mProgressDialog.setTitle(mContext.getString(R.string.person_mande_title));
//            mProgressDialog.setMax(1);
//            mProgressDialog.setDialogCallback(new ProgressDialogCallback());
//            //mProgressDialog.setAutoClose(false);
//            mProgressDialog.show(mAsyncForm.getActivity().getFragmentManager(), "send_PFaktor");


            class RunAsync extends AAsyncTask<Void, String, PersonLastSellInfoModel> {

                public RunAsync(ProgressBar progressBar) {
                    super(progressBar);
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);

//                    mProgressDialog.performStep(values[0]);
                }

                @Override
                protected PersonLastSellInfoModel doInBackground(Void... voids) {
                    Log.d(TAG, "doInBackground(): entered the function");

                    PersonBLL personBLL = new PersonBLL(mContext);

                    PersonLastSellInfoModel lastSellInfo = null;
                    try {
                        lastSellInfo = personBLL.FetchPersonLastSellInfo(mPerson.getCode(), mKala.getCode());
                        if (lastSellInfo != null)
                            publishProgress(String.valueOf(String.valueOf(lastSellInfo.getSellPrice())));
                    } catch (Exception ex) {
                        setException(ex);
                    }

                    return lastSellInfo;
                }

                @Override
                protected void onPostExecute(PersonLastSellInfoModel lastSellInfo) {
                    Log.d(TAG, "onPostExecute(): entered the function");
                    mAsyncForm.stopProgress();
//                    mProgressDialog.Close();
//
                    if (isException()) {
//                        mProgressDialog.Close();
                        mAsyncForm.showError(getException(), null);
                    } else if (lastSellInfo != null) {
                        mFragmentCallback.onMyActivityCallback(Actions.ACTION_GET_LAST_SELL_PRICE, lastSellInfo, null);
                    }


                    if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                        ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                    }
                }
            }


            if (Vars.USER != null) {
                RunAsync runAsync = new RunAsync(mAsyncForm.getProgressBar());
                runAsync.execute();
            }
        }
    }*/
}
