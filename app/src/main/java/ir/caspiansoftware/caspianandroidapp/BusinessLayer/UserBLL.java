package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import info.elyasi.android.elyasilib.BLL.ABusinessLayer;
import info.elyasi.android.elyasilib.BLL.ResponseBLL;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.UserDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.UserPermissionDataSource;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.UserWebService;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.ResponseToModel;
import ir.caspiansoftware.caspianandroidapp.Models.UserModel;
import ir.caspiansoftware.caspianandroidapp.Models.UserPermissionModel;
import ir.caspiansoftware.caspianandroidapp.Setting;
import info.elyasi.android.elyasilib.Constant;
import info.elyasi.android.elyasilib.WebService.ResponseWebService;
import ir.caspiansoftware.caspianandroidapp.Vars;


/**
 * Created by Canada on 2/7/2016.
 */
public class UserBLL extends ABusinessLayer {
    private static final String TAG = "UserBLL";

    private UserWebService mUserWebService;

    public UserBLL(Context context) {
        super(context);
        mUserWebService = new UserWebService();
    }

    // region webService ***************************************************************************
    public boolean login(final String userName, final String password) throws Exception {
        Log.d(TAG, "login start");

        try {
            // check login credential
            ResponseWebService responseWebService = mUserWebService.Login(userName, password);
            if (responseWebService == null)
                throw new Exception("responseWebService is null");

            if (responseWebService.getCode() == Constant.ERROR)
                throw new Exception(responseWebService.getData());

            UserModel userModel = ResponseToModel.getUser(responseWebService.getData());

            // fetch userModel permissions from the server
            ResponseWebService permissionResponseWebService = mUserWebService.GetPermissions(userModel.getUserId());

            if (permissionResponseWebService.getCode() == Constant.ERROR)
                throw new Exception(permissionResponseWebService.getData());

            List<UserPermissionModel> permissions = ResponseToModel
                    .getUserPermissionList(userModel.getUserId(), permissionResponseWebService.getData());

            if (permissions == null) {
                throw new Exception("permissions are null");
            }

            // save userModel info
            userModel.setLogon(true);
            userModel.setLastLoginDate(new Date());
            Vars.USER = userModel;

            // save userModel info to database
            saveUser(Vars.USER);

            // save permissions to database
            savePermissions(Vars.USER.getUserId(), permissions);

            if (!userModel.isActive()) {
                throw new Exception("user is not active");
            }


            //return new ResponseBLL<>(Constant.SUCCESS, Constant.OK, "login"); //doOnSuccess(new ResponseWebService(Constant.SUCCESS, Constant.OK, "login"));
            return true;
        //} catch (Exception ex) {
        //    return doOnError(ex.getMessage(), "login");
        } finally {
            Log.d(TAG, "login finished");
        }
    }

    public Boolean checkForLogout(int userId) throws Exception {
        try {
            Log.d(TAG, "checkForLogout start");

            ResponseWebService responseWebService = mUserWebService.ShouldLogout(userId);

            if (responseWebService.getData().equals("true")) {
                mUserWebService.SetShouldLogout(userId, false);
            }

            Log.d(TAG, "checkForLogout finished");
            return responseWebService.getData().equals("true");
            //return new ResponseBLL<>(Constant.SUCCESS, responseWebService.getData().equals("true"), responseWebService.getCaller()); //doOnSuccess(new ResponseWebService(Constant.SUCCESS, data, responseWebService.getCaller()));
        //catch (Exception ex) {
        //   return doOnError(ex.getMessage(), "checkForLogout");
        } finally {
            Log.d(TAG, "checkForLogout finished");
        }

    }

    // endregion

    // region database *****************************************************************************
    public void saveUser(UserModel pUserModel) {

        try (UserDataSource userDataSource = new UserDataSource(mContext)) {
            // logout other users
            userDataSource.setOtherUsersLogout(pUserModel.getUserId());

            if (userDataSource.getById(pUserModel.getUserId()) == null) {
                // first login to this device
                userDataSource.insert(pUserModel);
            } else {
                // this user already logon to this device
                userDataSource.update(pUserModel);
            }
        }
    }

    public void savePermissions(int userId, List<UserPermissionModel> permissions) {
        try (UserPermissionDataSource dataSource = new UserPermissionDataSource(mContext)) {
            // delete old permissions
            dataSource.deleteAll(userId);

            // save new permissions
            for (UserPermissionModel p : permissions) {
                dataSource.insert(p);
            }
        }
    }

    public UserModel getLogonUser() {
        try (UserDataSource userDataSource = new UserDataSource(mContext)) {
            return userDataSource.getLogonUser();
        }
        //return null;
    }

    public void logout(UserModel pUserModel) {
        try (UserDataSource userDataSource = new UserDataSource(mContext)) {
            userDataSource.logout(pUserModel);
        }
    }

    public LinkedHashMap<String, UserPermissionModel> getUserPermissions(int userId) {
        try (UserPermissionDataSource dataSource = new UserPermissionDataSource(mContext)) {
            List<UserPermissionModel> permissions = dataSource.getAll(userId);

            if (permissions != null) {
                LinkedHashMap<String, UserPermissionModel> map = new LinkedHashMap<>(permissions.size());

                for (UserPermissionModel p : permissions) {
                    map.put(p.getPart(), p);
                }

                return map;
            }

            throw new RuntimeException(CaspianErrors.permissions_are_null);
        }
    }



    //endregion database
}
