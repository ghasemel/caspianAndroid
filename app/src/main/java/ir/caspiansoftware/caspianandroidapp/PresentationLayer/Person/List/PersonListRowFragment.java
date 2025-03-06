package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.UI.AListRowFragment;
import info.elyasi.android.elyasilib.UI.FormActionType;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PersonBLL;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/18/2016.
 */
public class PersonListRowFragment extends AListRowFragment<PersonModel> {
    private static final String TAG = "PersonListRowFragment";


    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
    }

    @Override
    protected ArrayList<PersonModel> inBackground(Object... params) throws Exception {
        Log.d(TAG, "inBackground(): function entered");

        if (getActivity() != null && getActivity().getApplicationContext() != null && Vars.USER != null) {
            Log.d(TAG, "YearId: " + Vars.YEAR.getId());

            PersonBLL personBLL = new PersonBLL(getActivity().getApplicationContext());
            return personBLL.getPersonListByYearId(Vars.YEAR.getId());
        }
        return null;
    }

    @Override
    protected PersonAdapter postExecute(ArrayList<PersonModel> personList) throws Exception {
        if (personList != null)
            return new PersonAdapter(personList);
            //throw new Exception("empty person list");

        //mList = personList;

        return null;
    }

    @Override
    protected void onListItemClicked(PersonModel personModel, String resultExtraName) throws Exception {
        Intent intent = new Intent();
        intent.putExtra(getExtraReturnName(), personModel);

        // Retrieve actionType from fragment arguments
        if (getActivity() != null && getActivity().getIntent() != null) {
            FormActionType actionType = (FormActionType) getActivity().getIntent().getSerializableExtra(FormActionType.EXTRA_ACTION_TYPE);
            intent.putExtra(FormActionType.EXTRA_ACTION_TYPE, actionType);
        }

        getActivity().setResult(Activity.RESULT_OK, intent);
    }


    private class PersonAdapter extends MyAdapter {

        public PersonAdapter(ArrayList<PersonModel> list) {
            super(getActivity(), list);
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_person_list_row;
        }

        @Override
        protected List<ImageView> doForEachItem(View convertView, PersonModel personModel, int position, boolean isNull) {
            TextView cell_code = (TextView) convertView.findViewById(R.id.cell_person_code);
            cell_code.setText(personModel.getCode());

            TextView cell_description = (TextView) convertView.findViewById(R.id.cell_person_description);
            cell_description.setText(personModel.getName());

            return null;
        }

        protected boolean objectIncludeTheFilterConstraints(PersonModel personModel, String[] constraints) {
            if (constraints == null || constraints.length != 2)
                return false;

            //String code = PersianConvert.ConvertDigitsToPersian(constraints[0]);
            String code = constraints[0];
            String name = PersianConvert.ConvertDigitsToLatin(constraints[1]);

            boolean flag = true;
            if (code.length() > 0) {
                flag = personModel.getCode().contains(code);
            }

            if (name.length() > 0) {
                flag = (flag && personModel.getName().contains(name));
            }

            return flag;
        }
    }
}
