package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class CategoryModle {
    private List<CategoryBean> category;

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public static class CategoryBean {

        private int count;
        private String ename;
        private String rname;
        private String cover_temp;
        private String name;
        private String cover;
        private int rank;
        private int sn;
        private String icover;
        private int atime;
        private int type;
        private String id;
        private String picasso_cover;
        private List<?> filter;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getRname() {
            return rname;
        }

        public void setRname(String rname) {
            this.rname = rname;
        }

        public String getCover_temp() {
            return cover_temp;
        }

        public void setCover_temp(String cover_temp) {
            this.cover_temp = cover_temp;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getSn() {
            return sn;
        }

        public void setSn(int sn) {
            this.sn = sn;
        }

        public String getIcover() {
            return icover;
        }

        public void setIcover(String icover) {
            this.icover = icover;
        }

        public int getAtime() {
            return atime;
        }

        public void setAtime(int atime) {
            this.atime = atime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPicasso_cover() {
            return picasso_cover;
        }

        public void setPicasso_cover(String picasso_cover) {
            this.picasso_cover = picasso_cover;
        }

        public List<?> getFilter() {
            return filter;
        }

        public void setFilter(List<?> filter) {
            this.filter = filter;
        }
    }

}
