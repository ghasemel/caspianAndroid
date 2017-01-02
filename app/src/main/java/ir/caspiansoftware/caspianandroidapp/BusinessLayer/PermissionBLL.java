package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import ir.caspiansoftware.caspianandroidapp.Models.UserPermissionModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 8/19/2016.
 */
public class PermissionBLL {

    public static boolean mojoodiAccess() {
        if (Vars.PERMISSIONS != null) {
            UserPermissionModel userPermission =
                    Vars.PERMISSIONS.get(Vars.CONTEXT.getString(R.string.permission_mojoodi));

            if (userPermission != null)
                return userPermission.isAccess();
        }
        return false;
    }

    public static boolean mandeAccess() {
        if (Vars.PERMISSIONS != null) {
            UserPermissionModel userPermission =
                    Vars.PERMISSIONS.get(Vars.CONTEXT.getString(R.string.permission_mande));

            if (userPermission != null)
                return userPermission.isAccess();
        }
        return false;
    }


    public static boolean forceLocationSaving() {
        if (Vars.PERMISSIONS != null) {
            UserPermissionModel userPermission =
                    Vars.PERMISSIONS.get(Vars.CONTEXT.getString(R.string.permission_force_location));

            if (userPermission != null)
                return userPermission.isAccess();
        }
        return false;
    }

    public static boolean seeLocation() {
        if (Vars.PERMISSIONS != null) {
            UserPermissionModel userPermission =
                    Vars.PERMISSIONS.get(Vars.CONTEXT.getString(R.string.permission_see_location));

            if (userPermission != null)
                return userPermission.isAccess();
        }
        return false;
    }

    public static boolean lastSellPriceAccess() {
        if (Vars.PERMISSIONS != null) {
            UserPermissionModel userPermission =
                    Vars.PERMISSIONS.get(Vars.CONTEXT.getString(R.string.permission_last_sell_price));

            if (userPermission != null)
                return userPermission.isAccess();
        }
        return false;
    }
}
