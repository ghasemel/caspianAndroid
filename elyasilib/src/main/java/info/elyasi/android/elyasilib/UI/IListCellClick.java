package info.elyasi.android.elyasilib.UI;

import android.view.View;

/**
 * Created by Canada on 7/27/2016.
 */
public interface IListCellClick<TItem> {
    void OnCellClick(TItem item, int row, int cellId, View cellView);
}
