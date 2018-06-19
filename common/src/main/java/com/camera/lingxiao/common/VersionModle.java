package com.camera.lingxiao.common;

/**
 * Created by lingxiao on 2017/9/7.
 */

public class VersionModle {
    /**
     * versionCode : 3
     * downloadUrl :
     * versionDes : 3.0重大更新，贴心的小bug修复，优化你的体验！
     * versionName : 3.0
     */

    private int versionCode;
    private String downloadUrl;
    private String versionDes;
    private String versionName;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionDes() {
        return versionDes;
    }

    public void setVersionDes(String versionDes) {
        this.versionDes = versionDes;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return "VersionModle{" +
                "versionCode=" + versionCode +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", versionDes='" + versionDes + '\'' +
                ", versionName='" + versionName + '\'' +
                '}';
    }
}

