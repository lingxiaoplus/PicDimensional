package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 2017/9/4.
 */

public class SpecialModle {


        private List<AlbumBean> album;
        private List<BannerBean> banner;

        public List<AlbumBean> getAlbum() {
            return album;
        }

        public void setAlbum(List<AlbumBean> album) {
            this.album = album;
        }

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public static class AlbumBean {
            /**
             * ename :
             * isfeed : false
             * tag : []
             * id : 59a67fafe7bce729760f3ea0
             * top : 0
             * type : 1
             * status : online
             * user : {"gcid":"","name":"蛋蛋君","gender":0,"follower":2321,"avatar":"http://s.adesk.com/picasso/avatar_default.png","viptime":978278400,"following":0,"isvip":false,"id":"561f6b2194e5cc423617f328"}
             * favs : 17
             * atime : 1504083887
             * desc : 她是《卫子夫》中大气聪慧的平阳公主，是《那年花开月正圆》中敢爱敢恨的千红，她是周丽淇，一路一“淇”走
             * name : 【独家】周丽淇
             * url : []
             * cover : http://img5.adesk.com/59a68f2fe7bce729bf14832b?imageView2/3/h/240
             * lcover : http://img5.adesk.com/59a68f2fe7bce729bf14832b?imageView2/3/h/720
             * subname :
             * sn : 999
             * nickname : 光点电影院
             */

            private String ename;
            private boolean isfeed;
            private String id;
            private int top;
            private int type;
            private String status;
            private UserBean user;
            private int favs;
            private int atime;
            private String desc;
            private String name;
            private String cover;
            private String lcover;
            private String subname;
            private int sn;
            private String nickname;
            private List<?> tag;
            private List<?> url;

            public String getEname() {
                return ename;
            }

            public void setEname(String ename) {
                this.ename = ename;
            }

            public boolean isIsfeed() {
                return isfeed;
            }

            public void setIsfeed(boolean isfeed) {
                this.isfeed = isfeed;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
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

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getLcover() {
                return lcover;
            }

            public void setLcover(String lcover) {
                this.lcover = lcover;
            }

            public String getSubname() {
                return subname;
            }

            public void setSubname(String subname) {
                this.subname = subname;
            }

            public int getSn() {
                return sn;
            }

            public void setSn(int sn) {
                this.sn = sn;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public List<?> getTag() {
                return tag;
            }

            public void setTag(List<?> tag) {
                this.tag = tag;
            }

            public List<?> getUrl() {
                return url;
            }

            public void setUrl(List<?> url) {
                this.url = url;
            }

            public static class UserBean {
                /**
                 * gcid :
                 * name : 蛋蛋君
                 * gender : 0
                 * follower : 2321
                 * avatar : http://s.adesk.com/picasso/avatar_default.png
                 * viptime : 978278400
                 * following : 0
                 * isvip : false
                 * id : 561f6b2194e5cc423617f328
                 */

                private String gcid;
                private String name;
                private int gender;
                private int follower;
                private String avatar;
                private int viptime;
                private int following;
                private boolean isvip;
                private String id;

                public String getGcid() {
                    return gcid;
                }

                public void setGcid(String gcid) {
                    this.gcid = gcid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getGender() {
                    return gender;
                }

                public void setGender(int gender) {
                    this.gender = gender;
                }

                public int getFollower() {
                    return follower;
                }

                public void setFollower(int follower) {
                    this.follower = follower;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public int getViptime() {
                    return viptime;
                }

                public void setViptime(int viptime) {
                    this.viptime = viptime;
                }

                public int getFollowing() {
                    return following;
                }

                public void setFollowing(int following) {
                    this.following = following;
                }

                public boolean isIsvip() {
                    return isvip;
                }

                public void setIsvip(boolean isvip) {
                    this.isvip = isvip;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }
        }

        public static class BannerBean {
            /**
             * value : {"status":"online","ename":"","atime":1499221912,"url":[],"user":{"gcid":"","name":"该昵称已被占有","gender":1,"follower":2306,"avatar":"http://img0.adesk.com/download/5864acb09fd71a34ac937f84","viptime":1482992719,"following":0,"isvip":false,"id":"5864ac4f9fd71a34ac937f81"},"cover":"http://img5.adesk.com/59a7bad7e7bce729d564f2cc?imageView2/3/h/240","name":"左手倒影右手年华","tag":[],"sn":999,"id":"595c4f98e7bce76b48d6eb35","lcover":"http://img5.adesk.com/59a7bad7e7bce729d564f2cc?imageView2/3/h/720","favs":455,"type":1,"isfeed":false,"desc":"换一个视角看世界，那些让人窒息的美丽倒影"}
             * offtm : 1500537627
             * target : 595c4f98e7bce76b48d6eb35
             * img : 5970631be7bce76b7e5779df
             * new_img : null
             * new_thumb : http://img0.adesk.com/download/http://img0.adesk.com/download/5970631be7bce76b7e5779df
             * oid : null
             * thumb : http://img0.adesk.com/download/5970631be7bce76b7e5779df
             * module : 5
             * _id : 5970631be7bce76b7e5779e2
             * reco :
             * ontm : 1500537627
             * desc :
             * atime : 1500537627
             * type : 7
             * id : 5970631be7bce76b7e5779e2
             * market : []
             * uid : 507b922bcd29911da5b9bea8
             */

            private ValueBean value;
            private int offtm;
            private String target;
            private String img;
            private Object new_img;
            private String new_thumb;
            private Object oid;
            private String thumb;
            private int module;
            private String _id;
            private String reco;
            private int ontm;
            private String desc;
            private int atime;
            private int type;
            private String id;
            private String uid;
            private List<?> market;

            public ValueBean getValue() {
                return value;
            }

            public void setValue(ValueBean value) {
                this.value = value;
            }

            public int getOfftm() {
                return offtm;
            }

            public void setOfftm(int offtm) {
                this.offtm = offtm;
            }

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public Object getNew_img() {
                return new_img;
            }

            public void setNew_img(Object new_img) {
                this.new_img = new_img;
            }

            public String getNew_thumb() {
                return new_thumb;
            }

            public void setNew_thumb(String new_thumb) {
                this.new_thumb = new_thumb;
            }

            public Object getOid() {
                return oid;
            }

            public void setOid(Object oid) {
                this.oid = oid;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public int getModule() {
                return module;
            }

            public void setModule(int module) {
                this.module = module;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getReco() {
                return reco;
            }

            public void setReco(String reco) {
                this.reco = reco;
            }

            public int getOntm() {
                return ontm;
            }

            public void setOntm(int ontm) {
                this.ontm = ontm;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
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

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public List<?> getMarket() {
                return market;
            }

            public void setMarket(List<?> market) {
                this.market = market;
            }

            public static class ValueBean {
                /**
                 * status : online
                 * ename :
                 * atime : 1499221912
                 * url : []
                 * user : {"gcid":"","name":"该昵称已被占有","gender":1,"follower":2306,"avatar":"http://img0.adesk.com/download/5864acb09fd71a34ac937f84","viptime":1482992719,"following":0,"isvip":false,"id":"5864ac4f9fd71a34ac937f81"}
                 * cover : http://img5.adesk.com/59a7bad7e7bce729d564f2cc?imageView2/3/h/240
                 * name : 左手倒影右手年华
                 * tag : []
                 * sn : 999
                 * id : 595c4f98e7bce76b48d6eb35
                 * lcover : http://img5.adesk.com/59a7bad7e7bce729d564f2cc?imageView2/3/h/720
                 * favs : 455
                 * type : 1
                 * isfeed : false
                 * desc : 换一个视角看世界，那些让人窒息的美丽倒影
                 */

                private String status;
                private String ename;
                private int atime;
                private UserBeanX user;
                private String cover;
                private String name;
                private int sn;
                private String id;
                private String lcover;
                private int favs;
                private int type;
                private boolean isfeed;
                private String desc;
                private List<?> url;
                private List<?> tag;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getEname() {
                    return ename;
                }

                public void setEname(String ename) {
                    this.ename = ename;
                }

                public int getAtime() {
                    return atime;
                }

                public void setAtime(int atime) {
                    this.atime = atime;
                }

                public UserBeanX getUser() {
                    return user;
                }

                public void setUser(UserBeanX user) {
                    this.user = user;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getSn() {
                    return sn;
                }

                public void setSn(int sn) {
                    this.sn = sn;
                }

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

                public int getFavs() {
                    return favs;
                }

                public void setFavs(int favs) {
                    this.favs = favs;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public boolean isIsfeed() {
                    return isfeed;
                }

                public void setIsfeed(boolean isfeed) {
                    this.isfeed = isfeed;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public List<?> getUrl() {
                    return url;
                }

                public void setUrl(List<?> url) {
                    this.url = url;
                }

                public List<?> getTag() {
                    return tag;
                }

                public void setTag(List<?> tag) {
                    this.tag = tag;
                }

                public static class UserBeanX {
                    /**
                     * gcid :
                     * name : 该昵称已被占有
                     * gender : 1
                     * follower : 2306
                     * avatar : http://img0.adesk.com/download/5864acb09fd71a34ac937f84
                     * viptime : 1482992719
                     * following : 0
                     * isvip : false
                     * id : 5864ac4f9fd71a34ac937f81
                     */

                    private String gcid;
                    private String name;
                    private int gender;
                    private int follower;
                    private String avatar;
                    private int viptime;
                    private int following;
                    private boolean isvip;
                    private String id;

                    public String getGcid() {
                        return gcid;
                    }

                    public void setGcid(String gcid) {
                        this.gcid = gcid;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getGender() {
                        return gender;
                    }

                    public void setGender(int gender) {
                        this.gender = gender;
                    }

                    public int getFollower() {
                        return follower;
                    }

                    public void setFollower(int follower) {
                        this.follower = follower;
                    }

                    public String getAvatar() {
                        return avatar;
                    }

                    public void setAvatar(String avatar) {
                        this.avatar = avatar;
                    }

                    public int getViptime() {
                        return viptime;
                    }

                    public void setViptime(int viptime) {
                        this.viptime = viptime;
                    }

                    public int getFollowing() {
                        return following;
                    }

                    public void setFollowing(int following) {
                        this.following = following;
                    }

                    public boolean isIsvip() {
                        return isvip;
                    }

                    public void setIsvip(boolean isvip) {
                        this.isvip = isvip;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }
                }
            }
        }
}
