package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 17-11-6.
 */

public class CommentModle {
        private MetaBean meta;
        private WallpaperBean wallpaper;
        private List<CommentBean> comment;
        private List<AlbumBean> album;
        private List<HotBean> hot;
        private List<?> subject;

        public MetaBean getMeta() {
            return meta;
        }

        public void setMeta(MetaBean meta) {
            this.meta = meta;
        }

        public WallpaperBean getWallpaper() {
            return wallpaper;
        }

        public void setWallpaper(WallpaperBean wallpaper) {
            this.wallpaper = wallpaper;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public List<AlbumBean> getAlbum() {
            return album;
        }

        public void setAlbum(List<AlbumBean> album) {
            this.album = album;
        }

        public List<HotBean> getHot() {
            return hot;
        }

        public void setHot(List<HotBean> hot) {
            this.hot = hot;
        }

        public List<?> getSubject() {
            return subject;
        }

        public void setSubject(List<?> subject) {
            this.subject = subject;
        }

        public static class MetaBean {
            /**
             * more : true
             */

            private boolean more;

            public boolean isMore() {
                return more;
            }

            public void setMore(boolean more) {
                this.more = more;
            }
        }

        public static class WallpaperBean {
            /**
             * isfavor : false
             */

            private boolean isfavor;

            public boolean isIsfavor() {
                return isfavor;
            }

            public void setIsfavor(boolean isfavor) {
                this.isfavor = isfavor;
            }
        }

        public static class CommentBean {

            private ReplyUserBean reply_user;
            private ReplyMetaBean reply_meta;
            private String content;
            private boolean isup;
            private UserBean user;
            private int atime;
            private String id;
            private int size;

            public ReplyUserBean getReply_user() {
                return reply_user;
            }

            public void setReply_user(ReplyUserBean reply_user) {
                this.reply_user = reply_user;
            }

            public ReplyMetaBean getReply_meta() {
                return reply_meta;
            }

            public void setReply_meta(ReplyMetaBean reply_meta) {
                this.reply_meta = reply_meta;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public boolean isIsup() {
                return isup;
            }

            public void setIsup(boolean isup) {
                this.isup = isup;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
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

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public static class ReplyUserBean {
            }

            public static class ReplyMetaBean {
            }

            public static class UserBean {

                private String gcid;
                private int follower;
                private String name;
                private int viptime;
                private int following;
                private int gender;
                private boolean isvip;
                private String id;
                private String avatar;
                private List<TitleBean> title;

                public String getGcid() {
                    return gcid;
                }

                public void setGcid(String gcid) {
                    this.gcid = gcid;
                }

                public int getFollower() {
                    return follower;
                }

                public void setFollower(int follower) {
                    this.follower = follower;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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

                public int getGender() {
                    return gender;
                }

                public void setGender(int gender) {
                    this.gender = gender;
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

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public List<TitleBean> getTitle() {
                    return title;
                }

                public void setTitle(List<TitleBean> title) {
                    this.title = title;
                }

                public static class TitleBean {
                    /**
                     * name : 砖家
                     * icon : http://s.adesk.com/achieve/title/zhuanjia.png?v=2
                     */

                    private String name;
                    private String icon;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public void setIcon(String icon) {
                        this.icon = icon;
                    }
                }
            }
        }

        public static class AlbumBean {
            /**
             * ename : 美腿特辑
             * isfeed : false
             * tag : []
             * id : 534644f948d5b95a9ae3a575
             * top : 9
             * type : 1
             * status : online
             * user : {"gcid":"","name":"ZhouKIMI","gender":1,"follower":204,"avatar":"http://img0.adesk.com/download/563b25ed94e5cc562285aad5","viptime":978278400,"following":0,"isvip":false,"id":"528c24922d74c818c3d9b730"}
             * favs : 17716
             * atime : 1397114105
             * desc : 狂秀美腿的季节要来啦~！
             * name : 美腿特辑
             * url : []
             * cover : http://img0.adesk.com/download/5815b7a794e5cc3778275681
             * lcover : http://img0.adesk.com/download/5815b7a794e5cc37782756ae
             * subname : 美腿
             * sn : 42
             */

            private String ename;
            private boolean isfeed;
            private String id;
            private int top;
            private int type;
            private String status;
            private UserBeanX user;
            private int favs;
            private int atime;
            private String desc;
            private String name;
            private String cover;
            private String lcover;
            private String subname;
            private int sn;
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

            public UserBeanX getUser() {
                return user;
            }

            public void setUser(UserBeanX user) {
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

            public static class UserBeanX {
                /**
                 * gcid :
                 * name : ZhouKIMI
                 * gender : 1
                 * follower : 204
                 * avatar : http://img0.adesk.com/download/563b25ed94e5cc562285aad5
                 * viptime : 978278400
                 * following : 0
                 * isvip : false
                 * id : 528c24922d74c818c3d9b730
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

        public static class HotBean {
            /**
             * reply_user : {}
             * reply_meta : {}
             * content : 沙发
             * isup : false
             * user : {"gcid":"6bcedc6b9d9e4bcac006b06812e9eb42","name":"zzhhtt","title":[],"gender":0,"follower":266,"avatar":"http://img0.adesk.com/download/555e937c9fd71a43892fe565","viptime":978278400,"following":72,"isvip":false,"id":"4f544969df714d52410005d0"}
             * atime : 1485605985
             * id : 588c8c6194e5cc255244575c
             * size : 4
             */

            private ReplyUserBeanX reply_user;
            private ReplyMetaBeanX reply_meta;
            private String content;
            private boolean isup;
            private UserBeanXX user;
            private int atime;
            private String id;
            private int size;

            public ReplyUserBeanX getReply_user() {
                return reply_user;
            }

            public void setReply_user(ReplyUserBeanX reply_user) {
                this.reply_user = reply_user;
            }

            public ReplyMetaBeanX getReply_meta() {
                return reply_meta;
            }

            public void setReply_meta(ReplyMetaBeanX reply_meta) {
                this.reply_meta = reply_meta;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public boolean isIsup() {
                return isup;
            }

            public void setIsup(boolean isup) {
                this.isup = isup;
            }

            public UserBeanXX getUser() {
                return user;
            }

            public void setUser(UserBeanXX user) {
                this.user = user;
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

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public static class ReplyUserBeanX {
            }

            public static class ReplyMetaBeanX {
            }

            public static class UserBeanXX {
                /**
                 * gcid : 6bcedc6b9d9e4bcac006b06812e9eb42
                 * name : zzhhtt
                 * title : []
                 * gender : 0
                 * follower : 266
                 * avatar : http://img0.adesk.com/download/555e937c9fd71a43892fe565
                 * viptime : 978278400
                 * following : 72
                 * isvip : false
                 * id : 4f544969df714d52410005d0
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
                private List<?> title;

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

                public List<?> getTitle() {
                    return title;
                }

                public void setTitle(List<?> title) {
                    this.title = title;
                }
            }
        }
}
