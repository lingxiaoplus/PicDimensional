package com.lingxiaosuse.picture.tudimension.modle;

import java.util.ArrayList;

/**
 * Created by lingxiao on 2017/8/2.
 */

public class HomePageModle {
    public String msg;  //success or failed
    public Resources res;
    public class Resources{
        public ArrayList<Picture> wallpaper;  //热门图片
        public ArrayList<HomeImg> homepage;  //首页轮播图

        public ArrayList<Picture> getWallpaper() {
            return wallpaper;
        }

        public void setWallpaper(ArrayList<Picture> wallpaper) {
            this.wallpaper = wallpaper;
        }

        public ArrayList<HomeImg> getHomepage() {
            return homepage;
        }

        public void setHomepage(ArrayList<HomeImg> homepage) {
            this.homepage = homepage;
        }
    }
    public class Picture{
        public String img;  //原始图片地址
        public ArrayList<String> tag;  //标签类别
        public long atime;    //时间
        public String preview; //压缩过后的图片地址

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public ArrayList<String> getTag() {
            return tag;
        }

        public void setTag(ArrayList<String> tag) {
            this.tag = tag;
        }

        public long getAtime() {
            return atime;
        }

        public void setAtime(long atime) {
            this.atime = atime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String id;
        public String store;
        public String desc;
    }
    public class HomeImg{
        public ArrayList<HomeDes> items;
        public String title;    //小编精选

        public ArrayList<HomeDes> getItems() {
            return items;
        }

        public void setItems(ArrayList<HomeDes> items) {
            this.items = items;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
    public class HomeDes{
        public boolean status;      //先判断是否为true，如果为true，才往下解析
        public slidePic value;

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String thumb;        //轮播图 -- 广告之类的
        public boolean isStatus() {
            return status;
        }
        public void setStatus(boolean status) {
            this.status = status;
        }

    }

    public class slidePic{
        public long atime;
        public String cover;        //图片
        public String lcover; //720分辨率图片
        public String id; //图片id，用于拼接url

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLcover() {
            return lcover;
        }

        public void setLcover(String lcover) {
            this.lcover = lcover;
        }

        public long getAtime() {
            return atime;
        }

        public void setAtime(long atime) {
            this.atime = atime;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String desc;         //描述
        public String name;         //小标题
    }

}
