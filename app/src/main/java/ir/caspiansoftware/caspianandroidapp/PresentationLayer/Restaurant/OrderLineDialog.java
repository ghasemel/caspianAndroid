package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Restaurant;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ir.caspiansoftware.caspianandroidapp.Models.MenuItemModel;
import ir.caspiansoftware.caspianandroidapp.Models.OrderLineModel;
import ir.caspiansoftware.caspianandroidapp.Models.RestaurantOrderModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Exact quantity + kitchen note for one dish.
 *
 * Deliberately behind a long-press: ordering eight kebabs or asking for "no
 * onion" is real but uncommon, and putting it in the main tap path would slow
 * down every ordinary order.
 */
final class OrderLineDialog {

    interface OnChanged {
        void changed();
    }

    private OrderLineDialog() { }

    static void show(final Fragment fragment,
                     final MenuItemModel item,
                     final RestaurantOrderModel order,
                     final OnChanged callback) {

        View view = LayoutInflater.from(fragment.getActivity())
                .inflate(R.layout.dialog_order_line, null);

        TextView title = view.findViewById(R.id.dialogLineTitle);
        final EditText countBox = view.findViewById(R.id.dialogLineCount);
        final EditText specBox = view.findViewById(R.id.dialogLineSpec);

        title.setText(item.getName());

        // Pre-fill from the existing unsent line for this dish, if any, so the
        // dialog edits rather than silently starts over.
        OrderLineModel existing = null;
        for (OrderLineModel line : order.getLines())
            if (!line.isSent() && line.getCode().equals(item.getCode())) {
                existing = line;
                break;
            }

        countBox.setText(existing == null ? "1" : String.valueOf((long) existing.getCount()));
        specBox.setText(existing == null ? "" : existing.getSpec());

        final OrderLineModel target = existing;

        new AlertDialog.Builder(fragment.getActivity())
                .setView(view)
                .setPositiveButton(R.string.restaurant_dialog_ok, (dialog, which) -> {
                    double count = parseCount(countBox.getText().toString());
                    String spec = specBox.getText().toString().trim();

                    if (count <= 0) {
                        if (target != null)
                            order.removeLine(target);
                    } else if (target != null) {
                        target.setCount(count);
                        target.setSpec(spec);
                    } else {
                        OrderLineModel line = new OrderLineModel(item, count);
                        line.setSpec(spec);
                        order.getLines().add(line);
                    }

                    callback.changed();
                })
                .setNegativeButton(R.string.restaurant_dialog_cancel, null)
                .show();
    }

    private static double parseCount(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (Exception ex) {
            return 0;
        }
    }
}
