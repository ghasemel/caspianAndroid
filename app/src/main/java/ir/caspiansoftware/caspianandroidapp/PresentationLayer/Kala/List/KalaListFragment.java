package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.List;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianSearchableListFragment;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.R;


/**
 * Created by Canada on 7/22/2016.
 */
public class KalaListFragment extends CaspianSearchableListFragment<KalaModel> {
    private static final String TAG = "KalaListFragment";

    private EditText mSearchCode;
    private EditText mSearchName;


    @Override
    public void onClick(View view) {

    }

    @Override
    protected void mapViews(View parentView) {
        mSearchCode = (EditText) parentView.findViewById(R.id.search_kala_code);
        mSearchName = (EditText) parentView.findViewById(R.id.search_kala_name);

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
        return R.layout.fragment_kala_list;
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
        return getResources().getString(R.string.fragment_kala_list_row_tag);
    }
}
