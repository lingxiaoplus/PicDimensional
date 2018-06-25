package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 2017/9/1.
 */

public class BannerModle {

        private List<WallpaperBean> wallpaper;

        public List<WallpaperBean> getWallpaper() {
            return wallpaper;
        }

        public void setWallpaper(List<WallpaperBean> wallpaper) {
            this.wallpaper = wallpaper;
        }

        public static class WallpaperBean {
            /**
             * views : 0
             * ncos : 16
             * rank : 25002
             * tag : ["动漫","二次元","粉色","可爱","和服","萝莉"]
             * wp : http://img0.adesk.com/wallpaper?imgid=587de26ce7bce7755f360510
             * xr : false
             * cr : false
             * favs : 1113
             * atime : 1484917394
             * id : 587de26ce7bce7755f360510
             * desc :
             * thumb : http://img0.adesk.com/download/58820a92e7bce7234fa79f21
             * img : http://img0.adesk.com/download/58820a92e7bce7234fa79f2b
             * cid : ["4e4d610cdf714d2966000003"]
             * url : []
             * preview : http://img0.adesk.com/download/58820a92e7bce7234fa79f01
             * store : adesk
             */

            private int views;
            private int ncos;
            private int rank;
            private String wp;
            private boolean xr;
            private boolean cr;
            private int favs;
            private int atime;
            private String id;
            private String desc;
            private String thumb;
            private String img;
            private String preview;
            private String store;
            private List<String> tag;
            private List<String> cid;
            private List<?> url;

            public int getViews() {
                return views;
            }

            public void setViews(int views) {
                this.views = views;
            }

            public int getNcos() {
                return ncos;
            }

            public void setNcos(int ncos) {
                this.ncos = ncos;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public String getWp() {
                return wp;
            }

            public void setWp(String wp) {
                this.wp = wp;
            }

            public boolean isXr() {
                return xr;
            }

            public void setXr(boolean xr) {
                this.xr = xr;
            }

            public boolean isCr() {
                return cr;
            }

            public void setCr(boolean cr) {
                this.cr = cr;
            }

            public int getFavs() {
                return favs;
            }

            public void setFavs(int favs) {
                this.favs = favs;
            }

            public int getAtime() {
                return atime;
            }

            public void setAtime(int atime) {
                this.atime = atime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getPreview() {
                return preview;
            }

            public void setPreview(String preview) {
                this.preview = preview;
            }

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public List<String> getTag() {
                return tag;
            }

            public void setTag(List<String> tag) {
                this.tag = tag;
            }

            public List<String> getCid() {
                return cid;
            }

            public void setCid(List<String> cid) {
                this.cid = cid;
            }

            public List<?> getUrl() {
                return url;
            }

            public void setUrl(List<?> url) {
                this.url = url;
            }
        }
}
