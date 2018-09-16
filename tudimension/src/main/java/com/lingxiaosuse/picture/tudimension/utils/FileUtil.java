package com.lingxiaosuse.picture.tudimension.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 获取文件夹下所有文件集合
     * @param dirPath
     * @return
     */
    public static List<File> getFiles(String dirPath) {
        try {
            File file = new File(dirPath);
            List<File> fileList = new ArrayList<>();
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    fileList.add(f);
                } else {
                    getFiles(f.getAbsolutePath());
                }
            }
            return fileList;
        } catch (NullPointerException e) {
            //ToastUtils.show("出错了："+e.getMessage());
            Log.i("seedownloadimgact", "出错了");
        }
        return null;
    }

    /**
     * 获取文件大小
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        size = size / 1024 / 1024; //mb
        return size;
    }
}
