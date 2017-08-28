package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class CategoryModle {

    /**
     * msg : success
     * res : {"category":[{"count":50741,"ename":"girl","rname":"美女","cover_temp":"56a964df69401b2866828acb","name":"美女","cover":"http://img5.adesk.com/599e3be3e7bce729760f3c56?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":1,"filter":[],"sn":1,"icover":"564d831f69401b5aed4a86ca","atime":1291266021,"type":1,"id":"4e4d610cdf714d2966000000","picasso_cover":"599e3be3e7bce729760f3c56"},{"count":93572,"ename":"animation","rname":"动漫","cover_temp":"56a221c969401b3f4aa6700a","name":"动漫","cover":"http://img5.adesk.com/5988fc049a1aa326f546129c?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":4,"id":"4e4d610cdf714d2966000003","icover":"5880889ae7bce7755f3607d9","sn":2,"atime":1291266057,"type":1,"filter":[],"picasso_cover":"5988fc049a1aa326f546129c"},{"count":72666,"ename":"landscape","rname":"风景","cover_temp":"56a770e269401b756c748b28","name":"风景","cover":"http://img5.adesk.com/599e4ea4e7bce72c6220835a?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":3,"id":"4e4d610cdf714d2966000002","icover":"58734362e7bce76b93ca2739","sn":3,"atime":1291266049,"type":1,"filter":[],"picasso_cover":"599e4ea4e7bce72c6220835a"},{"count":14459,"ename":"game","rname":"游戏","cover_temp":"569f40fa69401b26c648eb87","name":"游戏","cover":"http://img5.adesk.com/599fa61b042208226cac5908?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":15,"filter":[],"sn":4,"icover":"5866127069401b347e0bd82b","atime":1300683934,"type":1,"id":"4e4d610cdf714d2966000007","picasso_cover":"599fa61b042208226cac5908"},{"count":9644,"ename":"text","rname":"文字","cover_temp":"56a1f92369401b3f529d3a3f","name":"文字","cover":"http://img5.adesk.com/599ff37de7bce729500bb9f5?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":5,"filter":[],"sn":5,"icover":"5863803069401b347e0bcd78","atime":1359601742,"type":1,"id":"5109e04e48d5b9364ae9ac45","picasso_cover":"599ff37de7bce729500bb9f5"},{"count":8134,"ename":"vision","rname":"视觉","cover_temp":"56a076f769401b323d865538","name":"视觉","cover":"http://img5.adesk.com/59782024042208073d4cc8d9?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":8,"filter":[],"sn":6,"icover":"57f8be3d69401b347e0ab423","type":1,"id":"4fb479f75ba1c65561000027","picasso_cover":"59782024042208073d4cc8d9"},{"count":15103,"ename":"emotion","rname":"情感","cover_temp":"56a03f5369401b26beeaea1d","name":"情感","cover":"http://img5.adesk.com/599feb4ae7bce729500bb9ee?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":2,"id":"4ef0a35c0569795756000000","icover":"5862265c69401b347e0bc6a3","sn":7,"type":1,"filter":[],"picasso_cover":"599feb4ae7bce729500bb9ee"},{"count":8214,"ename":"creative","rname":"设计","cover_temp":"569b34af69401b7dd39e9fc3","name":"设计","cover":"http://img5.adesk.com/599d431fe7bce72994d8f6a0?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":9,"id":"4fb47a195ba1c60ca5000222","icover":"5836bfb269401b34865eaa05","sn":8,"type":1,"filter":[],"picasso_cover":"599d431fe7bce72994d8f6a0"},{"count":19797,"ename":"celebrity","rname":"明星","cover_temp":"56a9a70669401b338161138c","name":"明星","cover":"http://img5.adesk.com/59a00c37e7bce72c2732e22d?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":6,"id":"5109e05248d5b9368bb559dc","icover":"5460349269401b3a428a47a7","sn":9,"atime":1359601746,"type":1,"filter":[],"picasso_cover":"59a00c37e7bce72c2732e22d"},{"count":23969,"ename":"stuff","rname":"物语","cover_temp":"56a61f1c69401b54eff72f31","name":"物语","cover":"http://img5.adesk.com/599a8790e7bce71f6576a870?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":10,"filter":[],"sn":10,"icover":"514fc5c548d5b9633caeef86","type":1,"id":"4fb47a465ba1c65561000028","picasso_cover":"599a8790e7bce71f6576a870"},{"count":10872,"ename":"art","rname":"艺术","cover_temp":"569f927669401b26beeae9e4","name":"艺术","cover":"http://img5.adesk.com/599ff709e7bce72961f0fefa?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":16,"filter":[],"sn":11,"icover":"586381ea69401b34865f1729","type":1,"id":"4ef0a3330569795757000000","picasso_cover":"599ff709e7bce72961f0fefa"},{"count":4229,"ename":"man","rname":"男人","cover_temp":"569b541d69401b7dc8ce2c68","name":"男人","cover":"http://img5.adesk.com/598ac42de7bce76b21db6f81?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":13,"id":"4e4d610cdf714d2966000006","icover":"5565466469401b4db69654cb","sn":12,"atime":1298251540,"type":1,"filter":[],"picasso_cover":"598ac42de7bce76b21db6f81"},{"count":26161,"ename":"cartoon","rname":"卡通","cover_temp":"56a03cda69401b26beeae9f4","name":"卡通","cover":"http://img5.adesk.com/59a37d0de7bce729760f3d7f?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":11,"id":"4e4d610cdf714d2966000004","icover":"584e8e1d69401b347e0ba161","sn":13,"atime":1291266067,"type":1,"filter":[],"picasso_cover":"59a37d0de7bce729760f3d7f"},{"count":23698,"ename":"machine","rname":"机械","cover_temp":"56a99e1f69401b1ce58c12dc","name":"机械","cover":"http://img5.adesk.com/599ff3c3e7bce729500bb9f6?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":12,"id":"4e4d610cdf714d2966000005","icover":"587f2a85e7bce7750997720a","sn":13,"atime":1297756191,"type":1,"filter":[],"picasso_cover":"599ff3c3e7bce729500bb9f6"},{"count":13628,"ename":"cityscape","rname":"城市","cover_temp":"569b540969401b7dd39ea06d","name":"城市","cover":"http://img5.adesk.com/5993f4c5e7bce76b48d6fe14?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":7,"filter":[],"sn":14,"icover":"585b850369401b34865f08a3","type":1,"id":"4fb47a305ba1c60ca5000223","picasso_cover":"5993f4c5e7bce76b48d6fe14"},{"count":19477,"ename":"animal","rname":"动物","cover_temp":"56a4d1da69401b753a684e69","name":"动物","cover":"http://img5.adesk.com/599fd9cce7bce72961f0fee9?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":14,"filter":[],"sn":16,"icover":"58807eade7bce77509977376","atime":1291266042,"type":1,"id":"4e4d610cdf714d2966000001","picasso_cover":"599fd9cce7bce72961f0fee9"},{"count":7903,"ename":"sport","rname":"运动","cover_temp":"56a08e6a69401b3241740a24","name":"运动","cover":"http://img5.adesk.com/599fda7ee7bce72994d8f7b1?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":17,"filter":[],"sn":17,"icover":"586228c969401b34865f11d9","type":1,"id":"4ef0a34e0569795757000001","picasso_cover":"599fda7ee7bce72994d8f7b1"},{"count":18343,"ename":"movie","rname":"影视","cover_temp":"56a59cbe69401b753a684f7a","name":"影视","cover":"http://img5.adesk.com/5995561ee7bce76bc03a8839?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480","rank":18,"filter":[],"sn":18,"icover":"58aecf3369401b34865f35d1","type":1,"id":"4e58c2570569791a19000000","picasso_cover":"5995561ee7bce76bc03a8839"}]}
     * code : 0
     */

    private String msg;
    private ResBean res;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResBean {
        private List<CategoryBean> category;

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public static class CategoryBean {
            /**
             * count : 50741
             * ename : girl
             * rname : 美女
             * cover_temp : 56a964df69401b2866828acb
             * name : 美女
             * cover : http://img5.adesk.com/599e3be3e7bce729760f3c56?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480
             * rank : 1
             * filter : []
             * sn : 1
             * icover : 564d831f69401b5aed4a86ca
             * atime : 1291266021
             * type : 1
             * id : 4e4d610cdf714d2966000000
             * picasso_cover : 599e3be3e7bce729760f3c56
             */

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
}
