package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 17-11-9.
 */

public class VerticalModle {

        private List<?> ads;
        private List<VerticalBean> vertical;

        public List<?> getAds() {
            return ads;
        }

        public void setAds(List<?> ads) {
            this.ads = ads;
        }

        public List<VerticalBean> getVertical() {
            return vertical;
        }

        public void setVertical(List<VerticalBean> vertical) {
            this.vertical = vertical;
        }

        public static class VerticalBean {

            private int views;
            private int ncos;
            private int rank;
            private String wp;
            private boolean xr;
            private boolean cr;
            private int favs;
            private double atime;
            private String id;
            private String desc;
            private String thumb;
            private String img;
            private String rule;
            private String preview;
            private String store;
            private List<?> tag;
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

            public double getAtime() {
                return atime;
            }

            public void setAtime(double atime) {
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

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
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

            public List<?> getTag() {
                return tag;
            }

            public void setTag(List<?> tag) {
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
