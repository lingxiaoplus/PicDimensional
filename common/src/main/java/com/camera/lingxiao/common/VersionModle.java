package com.camera.lingxiao.common;

/**
 * Created by lingxiao on 2017/9/7.
 */

public class VersionModle {

    /**
     * versionName : 1.1
     * versionDes : 1.1重大更新，贴心的小bug修复，优化你的体验！
     * downloadUrl :
     * versionCode : 2
     */

    private String versionName;
    private String versionDes;
    private String downloadUrl;
    private int versionCode;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDes() {
        return versionDes;
    }

    public void setVersionDes(String versionDes) {
        this.versionDes = versionDes;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}

