package ir.caspiansoftware.caspianandroidapp;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.Models.UserModel;
import ir.caspiansoftware.caspianandroidapp.Models.UserPermissionModel;
import ir.caspiansoftware.caspianandroidapp.Models.YearMaliModel;

/**
 * Created by Canada on 8/16/2016.
 */
public class Vars {

    public static UserModel USER = null;
    public static YearMaliModel YEAR = null;
    public static LinkedHashMap<String, UserPermissionModel> PERMISSIONS = null;

    public static Context CONTEXT = null;
    //public static List<InitialSettingModel> SETTING_LIST = null;
    //public static Dictionary<String, String> USER_PERMISSION = null;

    public static DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
