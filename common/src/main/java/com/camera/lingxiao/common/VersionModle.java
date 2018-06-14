package com.camera.lingxiao.common;

import java.util.List;

/**
 * Created by lingxiao on 2017/9/7.
 */

public class VersionModle {


    /**
     * versionCode : 3
     * downloadUrl : [""]
     * versionDes : ["3.0重大更新，贴心的小bug修复，优化你的体验！"]
     * versionName : ["3.0"]
     */

    private int versionCode;
    private List<String> downloadUrl;
    private List<String> versionDes;
    private List<String> versionName;

    @Override
    public String toString() {
        return "VersionModle{" +
                "versionCode='" + versionCode + '\'' +
                ", downloadUrl=" + downloadUrl +
                ", versionDes=" + versionDes +
                ", versionName=" + versionName +
                '}';
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public List<String> getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(List<String> downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public List<String> getVersionDes() {
        return versionDes;
    }

    public void setVersionDes(List<String> versionDes) {
        this.versionDes = versionDes;
    }

    public List<String> getVersionName() {
        return versionName;
    }

    public void setVersionName(List<String> versionName) {
        this.versionName = versionName;
    }
}

