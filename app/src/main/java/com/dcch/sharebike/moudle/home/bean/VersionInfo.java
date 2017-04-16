package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2016/8/7 0007.
 */
public class VersionInfo {
    private int versionCode;
    private String fileName;
    private String description;
    private String loadUrl;

    public VersionInfo(int versionCode, String fileName, String description, String loadUrl) {
        this.versionCode = versionCode;
        this.fileName = fileName;
        this.description = description;
        this.loadUrl = loadUrl;
    }

    public VersionInfo() {
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoadUrl() {
        return loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }
}
