package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 17-11-13.
 */

public class CategoryVerticalModle {

    /**
     * msg : success
     * res : {"category":[{"ename":"girl","atime":1.291266021E9,"name":"美女","cover":"http://img0.adesk.com/download/57e7155894e5cc60a375f653","sn":1,"nimgs":0,"uid":null,"type":1,"id":"4e4d610cdf714d2966000000","desc":""},{"ename":"animation","atime":1.291266057E9,"name":"动漫","cover":"http://img0.adesk.com/download/57e1111069401b6ca3675092","sn":2,"nimgs":0,"uid":null,"type":1,"id":"4e4d610cdf714d2966000003","desc":""},{"ename":"landscape","atime":1.291266049E9,"name":"风景","cover":"http://img0.adesk.com/download/57e9b82f94e5cc0a9e3e4148","sn":3,"nimgs":0,"uid":null,"type":1,"id":"4e4d610cdf714d2966000002","desc":""},{"ename":"game","atime":1.300683934E9,"name":"游戏","cover":"http://img0.adesk.com/download/57e4719c94e5cc3320a842d6","sn":4,"nimgs":0,"uid":null,"type":1,"id":"4e4d610cdf714d2966000007","desc":""},{"ename":"text","atime":1.359601742E9,"name":"文字","cover":"http://img0.adesk.com/download/57e3a91869401b37277ebd8f","sn":5,"nimgs":0,"uid":null,"type":1,"id":"5109e04e48d5b9364ae9ac45","desc":""},{"ename":"vision","atime":null,"name":"视觉","cover":"http://img0.adesk.com/download/57e374d094e5cc14c62ac7ff","sn":6,"nimgs":0,"uid":null,"type":1,"id":"4fb479f75ba1c65561000027","desc":""},{"ename":"emotion","atime":null,"name":"情感","cover":"http://img0.adesk.com/download/57e9b7f994e5cc0a9e3e3fdb","sn":7,"nimgs":0,"uid":null,"type":1,"id":"4ef0a35c0569795756000000","desc":""},{"ename":"creative","atime":null,"name":"设计","cover":"http://img0.adesk.com/download/57e4aacc94e5cc3204926349","sn":8,"nimgs":0,"uid":null,"type":1,"id":"4fb47a195ba1c60ca5000222","desc":""},{"ename":"celebrity","atime":1.359601746E9,"name":"明星","cover":"http://img0.adesk.com/download/57d6291794e5cc1662ab12b4","sn":9,"nimgs":0,"uid":null,"type":1,"id":"5109e05248d5b9368bb559dc","desc":""},{"ename":"stuff","atime":null,"name":"物语","cover":"http://img0.adesk.com/download/57e3cab494e5cc4d4267546b","sn":10,"nimgs":0,"uid":null,"type":1,"id":"4fb47a465ba1c65561000028","desc":""},{"ename":"art","atime":null,"name":"艺术","cover":"http://img0.adesk.com/download/57e3ad7794e5cc0dfd73401e","sn":11,"nimgs":0,"uid":null,"type":1,"id":"4ef0a3330569795757000000","desc":""},{"ename":"man","atime":1.29825154E9,"name":"男人","cover":"http://img0.adesk.com/download/57e3cb3594e5cc4d426756e2","sn":12,"nimgs":0,"uid":null,"type":1,"id":"4e4d610cdf714d2966000006","desc":""},{"ename":"cartoon","atime":1.291266067E9,"name":"卡通","cover":"http://img0.adesk.com/download/57e8655394e5cc322c85cffc","sn":13,"nimgs":0,"uid":null,"type":1,"id":"4e4d610cdf714d2966000004","desc":""},{"ename":"machine","atime":1.297756191E9,"name":"机械","cover":"http://img0.adesk.com/download/57e7696794e5cc15c9e09423","sn":13,"nimgs":0,"uid":null,"type":1,"id":"4e4d610cdf714d2966000005","desc":""},{"ename":"cityscape","atime":null,"name":"城市","cover":"http://img0.adesk.com/download/57e4e2e094e5cc2b50ce3664","sn":14,"nimgs":0,"uid":null,"type":1,"id":"4fb47a305ba1c60ca5000223","desc":""},{"ename":"animal","atime":1.291266042E9,"name":"动物","cover":"http://img0.adesk.com/download/57e6971394e5cc5209e89a1f","sn":16,"nimgs":0,"uid":null,"type":1,"id":"4e4d610cdf714d2966000001","desc":""},{"ename":"sport","atime":null,"name":"运动","cover":"http://img0.adesk.com/download/57e07cf794e5cc4c8af92a51","sn":17,"nimgs":0,"uid":null,"type":1,"id":"4ef0a34e0569795757000001","desc":""},{"ename":"movie","atime":null,"name":"影视","cover":"http://img0.adesk.com/download/57e3abb994e5cc0a2560e021","sn":18,"nimgs":0,"uid":null,"type":1,"id":"4e58c2570569791a19000000","desc":""}]}
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
             * ename : girl
             * atime : 1.291266021E9
             * name : 美女
             * cover : http://img0.adesk.com/download/57e7155894e5cc60a375f653
             * sn : 1
             * nimgs : 0
             * uid : null
             * type : 1
             * id : 4e4d610cdf714d2966000000
             * desc :
             */

            private String ename;
            private double atime;
            private String name;
            private String cover;
            private int sn;
            private int nimgs;
            private Object uid;
            private int type;
            private String id;
            private String desc;

            public String getEname() {
                return ename;
            }

            public void setEname(String ename) {
                this.ename = ename;
            }

            public double getAtime() {
                return atime;
            }

            public void setAtime(double atime) {
                this.atime = atime;
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

            public int getSn() {
                return sn;
            }

            public void setSn(int sn) {
                this.sn = sn;
            }

            public int getNimgs() {
                return nimgs;
            }

            public void setNimgs(int nimgs) {
                this.nimgs = nimgs;
            }

            public Object getUid() {
                return uid;
            }

            public void setUid(Object uid) {
                this.uid = uid;
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

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
