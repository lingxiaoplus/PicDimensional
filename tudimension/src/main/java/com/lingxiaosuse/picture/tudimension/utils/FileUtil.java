package com.lingxiaosuse.picture.tudimension.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    public static void isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (downloadFile.exists()) {
            if (downloadFile.isFile()){
                downloadFile.delete();
                downloadFile.mkdirs();
            }
        }else {
            downloadFile.mkdirs();
        }
    }
}
