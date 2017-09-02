package com.lingxiaosuse.picture.tudimension.utils;

import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

/**
 * Created by lingxiao on 2017/8/4.
 */

public class DownloadImageResult {
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tudimension";
    public String getFilePath() {
        return path;
    }

    public void onResult(String photoPath) {

    }

    public void onFail() {

    }
}
