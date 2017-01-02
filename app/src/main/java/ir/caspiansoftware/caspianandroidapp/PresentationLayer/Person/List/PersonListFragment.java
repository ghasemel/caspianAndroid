package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.List;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianSearchableListFragment;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/18/2016.
 */
public class PersonListFragment extends CaspianSearchableListFragment<PersonModel> {
    private static final String TAG = "PersonListFragment";

    private EditText mSearchCode;
    private EditText mSearchName;



    @Override
    public void onClick(View view) {

    }

    @Override
    protected void mapViews(View parentView) {
        mSearchCode = (EditText) parentView.findViewById(R.id.search_person_code);
        mSearchName = (EditText) parentView.findViewById(R.id.search_person_name);

        Log.d(TAG, "mapViews(): function end");
    }

    @Override
    protected EditText[] getSearchEditTexts() {
        return new EditText[] {
                mSearchCode,
                mSearchName
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person_list;
    }

    @Override
    protected String[] getSearchedTextValueOnTextChange() {
        return new String[] {
                mSearchCode.getText().toString(),
                mSearchName.getText().toString()
        };
    }

    @Override
    protected String getFragmentRowTagValue() {
        return getResources().getString(R.string.fragment_person_list_row_tag);
    }
}
