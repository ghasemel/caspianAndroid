package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonLastSellInfoModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.Models.YearMaliModel;
import ir.caspiansoftware.caspianandroidapp.Models.UserModel;
import ir.caspiansoftware.caspianandroidapp.Models.UserPermissionModel;

/**
 * Created by Canada on 2/9/2016.
 */
public class ResponseToModel {
    private static final String TAG = "ResponseToModel";

    public static UserModel getUser(String loginResponse) throws JSONException {
        Object obj = new JSONTokener(loginResponse).nextValue();
        if (!(obj instanceof JSONObject))
            throw new JSONException("loginResponse is not a JSON object");

        JSONObject json = (JSONObject) obj;

        UserModel userModel = new UserModel();
        userModel.setUserId(json.getInt("userId"));
        userModel.setName(json.getString("name"));
        userModel.setUserName(json.getString("userName"));
        userModel.setActive(json.getBoolean("active"));
        userModel.setVisitorCode(json.getInt("visitorCode"));
        userModel.setKalaPriceType(json.getString("kalaPriceType").charAt(0));
        return userModel;
    }

    public static List<UserPermissionModel> getUserPermissionList(int userId, String permissionResponse) throws JSONException {
        Object obj = new JSONTokener(permissionResponse).nextValue();
        if (!(obj instanceof JSONArray))
            throw new JSONException("permissionResponse is not a JSON array");

        JSONArray array = (JSONArray) obj;
        if (array.length() == 0)
            return null;

        List<UserPermissionModel> list = new ArrayList<>(array.length());
        for (int j = 0; j < array.length(); j++) {
            UserPermissionModel userPermissionModel = getUserPermission(array.getJSONObject(j));
            userPermissionModel.setUserId(userId);
            list.add(userPermissionModel);
        }

        return list;
    }

    private static UserPermissionModel getUserPermission(JSONObject permission) throws JSONException {
        UserPermissionModel per = new UserPermissionModel();
        per.setPart(permission.getString("part"));
        per.setAccess(permission.getBoolean("access"));
        per.setWrite(permission.getBoolean("write"));
        return per;
    }

    public static ArrayList<YearMaliModel> getYearMaliList(String yearMaliResponse) throws JSONException {
        Object obj = new JSONTokener(yearMaliResponse).nextValue();
        if (!(obj instanceof JSONArray))
            throw new JSONException("permissionResponse is not a JSON array");

        JSONArray array = (JSONArray) obj;
        ArrayList<YearMaliModel> list = new ArrayList<>(array.length());
        for (int j = 0; j < array.length(); j++) {
            YearMaliModel yearMali = getYearMali(array.getJSONObject(j));
            list.add(yearMali);
        }
        return list;
    }

    public static YearMaliModel getYearMali(JSONObject yearMali) throws JSONException {
        YearMaliModel year = new YearMaliModel();
        year.setYear(yearMali.getInt("Year"));
        year.setDaftar(yearMali.getInt("Daftar"));
        year.setCompany(yearMali.getString("CompanyName"));
        year.setDataBase(yearMali.getString("DataBase"));
        return year;
    }

    public static List<KalaModel> getKalaList(String kalaListResponse, int yearMaliId) throws JSONException {
        Object obj = new JSONTokener(kalaListResponse).nextValue();
        if (!(obj instanceof JSONArray))
            throw new JSONException("kalaListResponse is not a JSON array");

        JSONArray array = (JSONArray) obj;
        if (array.length() == 0)
            return null;

        List<KalaModel> list = new ArrayList<>(array.length());
        for (int j = 0; j < array.length(); j++) {
            KalaModel kalaModel = getKala(array.getJSONObject(j));
            kalaModel.setYearId_FK(yearMaliId);
            list.add(kalaModel);
        }

        return list;
    }

    public static KalaModel getKala(JSONObject kala) throws JSONException {
        KalaModel kalaModel = new KalaModel();
        kalaModel.setCode(kala.getString("code"));
        kalaModel.setName(kala.getString("name"));
        kalaModel.setVahedA(kala.getString("vahed_a"));
        kalaModel.setVahedF(kala.getString("vahed_f"));
        kalaModel.setMohtavi(kala.getDouble("mohtavi"));
        kalaModel.setPrice1(kala.getInt("price1"));
        kalaModel.setPrice2(kala.getInt("price2"));
        kalaModel.setPrice3(kala.getInt("price3"));
        kalaModel.setPriceOmde(kala.getInt("price_omde"));
        kalaModel.setMojoodi(kala.getDouble("mojoodi"));
        return kalaModel;
    }

    public static PersonLastSellInfoModel getPersonLastSellInfo(String responseData) throws JSONException {
        Object obj = new JSONTokener(responseData).nextValue();
        if (!(obj instanceof JSONObject))
            throw new JSONException("responseData is not a JSON object");

        JSONObject json = (JSONObject) obj;

        PersonLastSellInfoModel sellInfoModel = new PersonLastSellInfoModel();
        sellInfoModel.setAvancePresent(json.getDouble("AvancePercent"));
        sellInfoModel.setPersonCode(json.getString("CustomerCode"));
        sellInfoModel.setFaktorNum(json.getInt("FaktorNum"));
        sellInfoModel.setLastDate(json.getString("LastDate"));
        sellInfoModel.setSellPrice(json.getDouble("SellPrice"));
        return sellInfoModel;
    }

    public static List<PersonModel> getPersonList(String personListResponse, int yearMaliId) throws JSONException {
        Object obj = new JSONTokener(personListResponse).nextValue();
        if (!(obj instanceof JSONArray))
            throw new JSONException("personListResponse is not a JSON array");

        JSONArray array = (JSONArray) obj;
        if (array.length() == 0)
            return null;

        List<PersonModel> list = new ArrayList<>(array.length());
        for (int j = 0; j < array.length(); j++) {
            PersonModel personModel = personJsonToObject(array.getJSONObject(j));
            personModel.setYearId_FK(yearMaliId);
            list.add(personModel);
        }

        return list;
    }

    public static PersonModel getPerson(String personResponse) throws JSONException {
        Object obj = new JSONTokener(personResponse).nextValue();
        if (!(obj instanceof JSONObject))
            throw new JSONException("personListResponse is not a JSON object");

        return personJsonToObject((JSONObject) obj);
    }

    private static PersonModel personJsonToObject(JSONObject person) throws JSONException {
        PersonModel personModel = new PersonModel();
        personModel.setCode(person.getString("code"));
        personModel.setName(person.getString("name"));
        personModel.setMande(person.getDouble("mande"));
        personModel.setTel(person.getString("tel"));
        personModel.setMobile(person.getString("mobile"));
        personModel.setAddress(person.getString("addr"));
        return personModel;
    }
}
