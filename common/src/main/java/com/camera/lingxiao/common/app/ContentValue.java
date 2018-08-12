package com.camera.lingxiao.common.app;

import android.os.Environment;

import com.camera.lingxiao.common.R;

/**
 * Created by lingxiao on 2017/9/1.
 */

public class ContentValue {
    public static final String imgRule ="?imageView2/3/h/230";//图片规则，从服务器取230大小的图片
    public static final String bigImgRule ="?imageView2/3/h/1080";
    public static final String hor_720ImgRule ="?imageView2/3/h/720";

    public static final String vertical720_ImgRule ="?imageView16/9/h/1080";
    public static final String vertical1080_ImgRule ="?imageView16/9/h/1080";
    //升级接口
    public static final String UPDATEURL = "https://www.lingxiaosuse.cn/tudimension/update.json";
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
    public static final String GANKURL = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/";

    //安卓壁纸的baseurl
    public static final String BASE_URL = "http://service.picasso.adesk.com";
    /**
     * 竖屏 最热
     */
    public static final String VERTICAL_URLS = "/v1/vertical/vertical" +
            "?limit=30?adult=false&first=1&order=hot";
    /**
     * banner
     */
    public static final String BANNER_URL = "/v1/wallpaper/";
    /**
     * 主页面
     */
    public static final String HOMEPAGE_URL = "/v3/homepage";

    /**
     * 竖屏
     */
    public static final String VERTICAL_URL = "/v1/vertical/vertical";

    /**
     * 专辑
     */
    public static final String SPECIAL_URL = "/v1/wallpaper/album";
    /**
     * 分类
     */
    public static final String CATEGORY_URL = "/v1/wallpaper/category";
    /**
     * 竖屏分类
     */
    public static final String CATEGORY_VERTICAL_URL = "/v1/vertical/category";

    /**
     * 评论
     */
    public static final String COMMENT_URL = "/v2/wallpaper/wallpaper";

    /**
     * 每次请求多少个数据
     */
    public static final int limit = 30;
    /**
     * 轮播图
     */
    public static final String TYPE_ALBUM = "album";
    /**
     * 分类
     */
    public static final String TYPE_CATEGORY = "category";

    //安卓壁纸的搜索
    public static final String SEARCH_URL = "http://so.picasso.adesk.com";
    //关键词
    public static final String SEARCH_KEY_URL = "/v1/push/keyword?versionCode=181&channel=huawei&first=0&adult=false";

    //保存的图片路径
    public static final String PATH = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath() + "/tudimension";

    //百度识图
    public static final String BAIDU_URL = "http://image.baidu.com/wiseshitu?guss=1&queryImageUrl=";
    //搜狗识图
    public static final String SOUGOU_URL = "http://pic.sogou.com/";
    //google识图
    public static final String GOOGLE_URL = "https://images.google.com/imghp?hl=zh-CN&gws_rd=ssl";

    //一言
    public static final String HITOKOTO_URL = "http://api.hitokoto.cn/";
    //mzitu网址
    public static final String MZITU_URL = "http://www.mzitu.com/";

    /**
     * cosplay.la
     */
    public static final String COSPLAY_LA_URL = "http://api.cosplay.la/share/GetPhotoList";

    public static final String COSPLAY_LA_DETAIL_URL = "http://api.cosplay.la/share/GetById";
    /**
     * cosplay requestcode
     */
    public static final String COSPLAY_REQUEST_CODE = "8A409431-D3EC-443F-A3B6-098F105B26B0";

    //浏览器标志
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0";

    //收藏的网址
    public static final String COLLECT_URL = "collect_url";

    //搜丝吧
    public static final String SOUSIBA_URL = "http://www.sousi88.cc";

    /**
     * 当前皮肤的id
     */
    public static final String SKIN_ID = "skin_id";
    /**
     * 记录侧滑模块
     */
    public static final String DRAWER_MODEL = "drawer_model";
    /**
     * 设置轮播图时间
     */
    public static final int BANNER_TIMER = 2000;

    /**
     * 设置浏览图片的分辨率
     */
    public static final String PIC_RESOLUTION = "pic_resolution";

}
