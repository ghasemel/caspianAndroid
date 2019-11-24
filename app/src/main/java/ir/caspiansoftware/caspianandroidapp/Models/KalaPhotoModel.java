package ir.caspiansoftware.caspianandroidapp.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.Serializable;

import ir.caspiansoftware.caspianandroidapp.BusinessLayer.InitialSettingBLL;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 6/18/2016.
 */
public class KalaPhotoModel implements Serializable {
    private int id;
    private int yearIdFK;
    private int serverPhotoId;
    private String code;
    private String fileName;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerPhotoId() {
        return serverPhotoId;
    }

    public void setServerPhotoId(int serverPhotoId) {
        this.serverPhotoId = serverPhotoId;
    }

    public int getYearIdFK() {
        return yearIdFK;
    }

    public void setYearIdFK(int yearIdFK) {
        this.yearIdFK = yearIdFK;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        File imgFile = new File(getImageFullPath());

        if(imgFile.exists()){
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }

        return null;
    }

    public String getImageDirPath() {
        return "images/" + Vars.YEAR.getDaftar() + "/kala/" + code + "/";
    }

    public String getImageFullPath() {
        return InitialSettingBLL.INSTANCE.getAppPath() + "/" + getImageDirPath() + fileName;
    }
}
