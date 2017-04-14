package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2016/8/7 0007.
 */
public class VersionInfo {

    /**
     * resultStatus : 1
     * versioncode : 2
     * filename : app
     * description : 这是2.0的版本增加了用户中心的功能
     * loadurl : http://192.168.1.108:8080/app-release.apk
     */

    private String resultStatus;
    private int versioncode;
    private String filename;
    private String description;
    private String loadurl;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoadurl() {
        return loadurl;
    }

    public void setLoadurl(String loadurl) {
        this.loadurl = loadurl;
    }
}
