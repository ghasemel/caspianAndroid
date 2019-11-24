package ir.caspiansoftware.caspianandroidapp.DataLayer.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.Models.DaftarTafReportModel;
import ir.caspiansoftware.caspianandroidapp.Models.DoreModel;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.KalaPhotoModel;
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

    private static YearMaliModel getYearMali(JSONObject yearMali) throws JSONException {
        YearMaliModel year = new YearMaliModel();
        year.setYear(yearMali.getInt("Year"));
        year.setDaftar(yearMali.getInt("Daftar"));
        year.setCompany(yearMali.getString("CompanyName"));
        year.setDataBase(yearMali.getString("DataBase"));
        year.setDoreModel(getDore(yearMali.getJSONObject("Dore")));
        return year;
    }

    private static DoreModel getDore(JSONObject doreMali) throws JSONException {
        DoreModel dore = new DoreModel();
        dore.setYear(doreMali.getInt("Year"));
        dore.setDaftar(doreMali.getInt("Daftar"));
        dore.setStartDore(doreMali.getString("DoreStart"));
        dore.setEndDore(doreMali.getString("DoreEnd"));
        return dore;
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

    public static List<KalaPhotoModel> getKalaPhotosList(String kalaPhotosListResponse, int yearMaliId) throws JSONException {
        Object obj = new JSONTokener(kalaPhotosListResponse).nextValue();
        if (!(obj instanceof JSONArray))
            throw new JSONException("kalaPhotosListResponse is not a JSON array");

        JSONArray array = (JSONArray) obj;
        if (array.length() == 0)
            return null;

        List<KalaPhotoModel> list = new ArrayList<>(array.length());
        for (int j = 0; j < array.length(); j++) {
            KalaPhotoModel photoModel = getKalaPhoto(array.getJSONObject(j));
            photoModel.setYearIdFK(yearMaliId);
            list.add(photoModel);
        }

        return list;
    }

    private static KalaModel getKala(JSONObject kala) throws JSONException {
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

    private static KalaPhotoModel getKalaPhoto(JSONObject kalaPhoto) throws JSONException {
        KalaPhotoModel photoModel = new KalaPhotoModel();
        photoModel.setServerPhotoId(kalaPhoto.getInt("id"));
        photoModel.setCode(kalaPhoto.getString("codeKala"));
        photoModel.setFileName(kalaPhoto.getString("fileName"));
        photoModel.setTitle(kalaPhoto.getString("title"));
        return photoModel;
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

    public static ArrayList<DaftarTafReportModel> getDaftarTafReport(String daftarTafReportList) throws JSONException {
        Object obj = new JSONTokener(daftarTafReportList).nextValue();
        if (!(obj instanceof JSONArray))
            throw new JSONException("daftarTafReportList is not a JSON array");

        JSONArray array = (JSONArray) obj;
        if (array.length() == 0)
            return null;

        long sum_mande = 0;

        ArrayList<DaftarTafReportModel> list = new ArrayList<>(array.length());
        for (int j = 0; j < array.length(); j++) {
            DaftarTafReportModel m = getDaftarTaf(array.getJSONObject(j));

            sum_mande += m.getMande();
            m.setMande(sum_mande);

            list.add(m);
        }

        return list;
    }

    private static DaftarTafReportModel getDaftarTaf(JSONObject daftarTafResponse) throws JSONException {
        DaftarTafReportModel daftarTaf = new DaftarTafReportModel();
        daftarTaf.setTarikh(daftarTafResponse.getString("tarikh"));
        daftarTaf.setCode(daftarTafResponse.getString("code"));
        daftarTaf.setBed(daftarTafResponse.getLong("bed"));
        daftarTaf.setBes(daftarTafResponse.getLong("bes"));
        daftarTaf.setDescription(daftarTafResponse.getString("des"));
        daftarTaf.setMande(daftarTafResponse.getLong("mande"));
        daftarTaf.setType(daftarTafResponse.getString("type"));

        return daftarTaf;
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
