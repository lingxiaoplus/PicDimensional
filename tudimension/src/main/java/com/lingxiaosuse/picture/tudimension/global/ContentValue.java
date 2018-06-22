package com.lingxiaosuse.picture.tudimension.global;

import android.os.Environment;

/**
 * Created by lingxiao on 2017/9/1.
 */

public class ContentValue {
    public static final String imgRule ="?imageView2/3/h/230";//图片规则，从服务器取230大小的图片
    public static final String bigImgRule ="?imageView2/3/h/1080";
    //升级接口
    public static final String UPDATEURL = "http://www.lingxiaosuse.cn/tudimension/update.json";
    public static final int PERMESSION_REQUEST_CODE = 200;
    //是否是第一次进入
    public static String ISFIRST_KEY = "isfirst_key";
    //服务器版本号
    public static String VERSION_CODE = "versino_code";
    //描述
    public static String VERSION_DES = "version_des";
    //下载地址
    public static String DOWNLOAD_URL = "download_url";

    //是否自动检测更新
    public static String IS_CHECK = "is_check";
    //是否开启日图
    public static String IS_OPEN_DAILY ="is_open_daily";
    //
    //干货集中营api
    public static String GANKURL = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/";

    //安卓壁纸的baseurl
    public static String BASE_URL = "http://service.picasso.adesk.com";
    //安卓壁纸的搜索
    public static String SEARCH_URL = "http://so.picasso.adesk.com";
    //保存的图片路径
    public static String PATH = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath() + "/tudimension";

    //百度识图
    public static String BAIDU_URL = "http://image.baidu.com/wiseshitu?guss=1&queryImageUrl=";
    //一言
    public static String HITOKOTO_URL = "http://api.hitokoto.cn/";
    //mzitu网址
    public static String MZITU_URL = "http://www.mzitu.com/";
    //浏览器标志
    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0";

    //收藏的网址
    public static String COLLECT_URL = "collect_url";

    //搜丝吧
    public static String SOUSIBA_URL = "http://www.sousi88.cc";
}
