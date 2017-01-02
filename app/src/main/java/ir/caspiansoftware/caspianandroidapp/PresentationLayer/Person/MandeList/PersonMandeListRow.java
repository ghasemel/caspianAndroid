package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.MandeList;

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
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.KalaBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PersonBLL;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/22/2016.
 */
public class PersonMandeListRow extends AListRowFragment<PersonModel> {
    private static final String TAG = "PersonMojoodiListRow";


    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        setCloseAfterSelection(false);
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
//        Intent intent = new Intent();
//        intent.putExtra(resultExtraName, personModel);
//        getActivity().setResult(Activity.RESULT_OK, intent);
    }


    private class PersonAdapter extends MyAdapter {

        public PersonAdapter(ArrayList<PersonModel> list) {
            super(getActivity(), list);
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_person_mande_list_row;
        }

        @Override
        protected List<ImageView> doForEachItem(View convertView, PersonModel personList, int position, boolean isNull) {
            TextView cell_radif = (TextView) convertView.findViewById(R.id.cell_radif);
            cell_radif.setText(String.valueOf(position + 1));

            TextView cell_code = (TextView) convertView.findViewById(R.id.cell_person_code);
            cell_code.setText(personList.getCode());

            TextView cell_name = (TextView) convertView.findViewById(R.id.cell_person_name);
            cell_name.setText(personList.getName());

            TextView cell_mande = (TextView) convertView.findViewById(R.id.cell_mande);
            cell_mande.setText(NumberExt.DigitSeparator(personList.getMande()));

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