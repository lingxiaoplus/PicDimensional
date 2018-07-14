package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

public class CosplayDetailModel {

    /**
     * Status : 200
     * Message : null
     * Data : {"Share":{"PhotoLists":[{"Id":70907,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/9ad78da9-f133-eee1-f9f2-442c9c7b010f.jpg?imageView2/2/w/540/q/100","SortIndex":1,"IsDefault":0},{"Id":70908,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/ac6017af-7133-eee1-f9f1-2fee86e77ced.jpg?imageView2/2/w/540/q/100","SortIndex":2,"IsDefault":0},{"Id":70909,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/c2ceda62-d133-eee1-f9f1-8ea6ab695932.jpg?imageView2/2/w/540/q/100","SortIndex":3,"IsDefault":0},{"Id":70910,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/3908482f-2133-eee1-f9f3-ba6f0040bb3f.jpg?imageView2/2/w/540/q/100","SortIndex":4,"IsDefault":0}],"NickName":null,"HeadPic":null,"Authentication":0,"KidneySign":null,"Account":null,"Domain":null,"IsFriendRecommend":false,"PhotoTypeName":"COS预告","ChUserId":0,"ChNickName":null,"ChDomain":null,"ChHeadPic":null,"ChAuthentication":0,"ChKidneySign":null,"ActionType":0,"IsRecommend":0,"IsPraise":0,"AllowFocus":0,"CreateTime":"0001-01-01T00:00:00","RefrenceId":null,"CommentContent":null,"RefrenceComment":null,"Id":10250,"UserId":8221,"ShareType":2,"PhotoType":2,"Title":"汉服 春景","RoleName":"汉服","CoserName":"雪殇","DefaultImage":"http://7xqgnb.com1.z0.glb.clouddn.com/9ad78da9-f133-eee1-f9f2-442c9c7b010f.jpg?imageMogr2/thumbnail/!280x280r/gravity/North/crop/280x280/interlace/1/quality/100","Content":"另一个小姐姐：柳雪","CreateDate":"2018-07-13T16:38:14.477","IPAddress":"222.190.122.89","Recommand":0,"Comment":0,"Praise":5,"ImgCount":4,"ViewCount":82,"Status":0},"User":{"UserId":8221,"Account":null,"Phone":null,"Password":null,"Email":null,"RoleType":0,"AreaCode":320100,"AreaName":null,"Domain":"coser8221","Status":1,"NickName":"雪殇","Gender":0,"Age":20,"AgeSecrecy":true,"Birthday":"1997-01-06T00:00:00","HeadPic":"http://7xqgnb.com1.z0.glb.clouddn.com/04e68227-a133-c63f-9e52-c26438d2b07a.jpg?imageMogr2/thumbnail/!280x280r/gravity/Center/crop/280x280/interlace/1/quality/100/format/jpg","KidneySign":"","AutoTag":"2","VipLevel":0,"Authentication":0,"FansCount":64,"FocusCount":9,"ImgCount":26,"ShareCount":1,"CommentCount":1,"AllowFocus":0,"FocusUserId":0,"Id":0,"Distance":null}}
     * TotalRecord : null
     */

    private String Status;
    private Object Message;
    private DataBean Data;
    private Object TotalRecord;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Object getMessage() {
        return Message;
    }

    public void setMessage(Object Message) {
        this.Message = Message;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public Object getTotalRecord() {
        return TotalRecord;
    }

    public void setTotalRecord(Object TotalRecord) {
        this.TotalRecord = TotalRecord;
    }

    public static class DataBean {
        /**
         * Share : {"PhotoLists":[{"Id":70907,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/9ad78da9-f133-eee1-f9f2-442c9c7b010f.jpg?imageView2/2/w/540/q/100","SortIndex":1,"IsDefault":0},{"Id":70908,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/ac6017af-7133-eee1-f9f1-2fee86e77ced.jpg?imageView2/2/w/540/q/100","SortIndex":2,"IsDefault":0},{"Id":70909,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/c2ceda62-d133-eee1-f9f1-8ea6ab695932.jpg?imageView2/2/w/540/q/100","SortIndex":3,"IsDefault":0},{"Id":70910,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/3908482f-2133-eee1-f9f3-ba6f0040bb3f.jpg?imageView2/2/w/540/q/100","SortIndex":4,"IsDefault":0}],"NickName":null,"HeadPic":null,"Authentication":0,"KidneySign":null,"Account":null,"Domain":null,"IsFriendRecommend":false,"PhotoTypeName":"COS预告","ChUserId":0,"ChNickName":null,"ChDomain":null,"ChHeadPic":null,"ChAuthentication":0,"ChKidneySign":null,"ActionType":0,"IsRecommend":0,"IsPraise":0,"AllowFocus":0,"CreateTime":"0001-01-01T00:00:00","RefrenceId":null,"CommentContent":null,"RefrenceComment":null,"Id":10250,"UserId":8221,"ShareType":2,"PhotoType":2,"Title":"汉服 春景","RoleName":"汉服","CoserName":"雪殇","DefaultImage":"http://7xqgnb.com1.z0.glb.clouddn.com/9ad78da9-f133-eee1-f9f2-442c9c7b010f.jpg?imageMogr2/thumbnail/!280x280r/gravity/North/crop/280x280/interlace/1/quality/100","Content":"另一个小姐姐：柳雪","CreateDate":"2018-07-13T16:38:14.477","IPAddress":"222.190.122.89","Recommand":0,"Comment":0,"Praise":5,"ImgCount":4,"ViewCount":82,"Status":0}
         * User : {"UserId":8221,"Account":null,"Phone":null,"Password":null,"Email":null,"RoleType":0,"AreaCode":320100,"AreaName":null,"Domain":"coser8221","Status":1,"NickName":"雪殇","Gender":0,"Age":20,"AgeSecrecy":true,"Birthday":"1997-01-06T00:00:00","HeadPic":"http://7xqgnb.com1.z0.glb.clouddn.com/04e68227-a133-c63f-9e52-c26438d2b07a.jpg?imageMogr2/thumbnail/!280x280r/gravity/Center/crop/280x280/interlace/1/quality/100/format/jpg","KidneySign":"","AutoTag":"2","VipLevel":0,"Authentication":0,"FansCount":64,"FocusCount":9,"ImgCount":26,"ShareCount":1,"CommentCount":1,"AllowFocus":0,"FocusUserId":0,"Id":0,"Distance":null}
         */

        private ShareBean Share;
        private UserBean User;

        public ShareBean getShare() {
            return Share;
        }

        public void setShare(ShareBean Share) {
            this.Share = Share;
        }

        public UserBean getUser() {
            return User;
        }

        public void setUser(UserBean User) {
            this.User = User;
        }

        public static class ShareBean {
            /**
             * PhotoLists : [{"Id":70907,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/9ad78da9-f133-eee1-f9f2-442c9c7b010f.jpg?imageView2/2/w/540/q/100","SortIndex":1,"IsDefault":0},{"Id":70908,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/ac6017af-7133-eee1-f9f1-2fee86e77ced.jpg?imageView2/2/w/540/q/100","SortIndex":2,"IsDefault":0},{"Id":70909,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/c2ceda62-d133-eee1-f9f1-8ea6ab695932.jpg?imageView2/2/w/540/q/100","SortIndex":3,"IsDefault":0},{"Id":70910,"ShareId":10250,"ShareType":2,"PicPath":"http://7xqgnb.com1.z0.glb.clouddn.com/3908482f-2133-eee1-f9f3-ba6f0040bb3f.jpg?imageView2/2/w/540/q/100","SortIndex":4,"IsDefault":0}]
             * NickName : null
             * HeadPic : null
             * Authentication : 0
             * KidneySign : null
             * Account : null
             * Domain : null
             * IsFriendRecommend : false
             * PhotoTypeName : COS预告
             * ChUserId : 0
             * ChNickName : null
             * ChDomain : null
             * ChHeadPic : null
             * ChAuthentication : 0
             * ChKidneySign : null
             * ActionType : 0
             * IsRecommend : 0
             * IsPraise : 0
             * AllowFocus : 0
             * CreateTime : 0001-01-01T00:00:00
             * RefrenceId : null
             * CommentContent : null
             * RefrenceComment : null
             * Id : 10250
             * UserId : 8221
             * ShareType : 2
             * PhotoType : 2
             * Title : 汉服 春景
             * RoleName : 汉服
             * CoserName : 雪殇
             * DefaultImage : http://7xqgnb.com1.z0.glb.clouddn.com/9ad78da9-f133-eee1-f9f2-442c9c7b010f.jpg?imageMogr2/thumbnail/!280x280r/gravity/North/crop/280x280/interlace/1/quality/100
             * Content : 另一个小姐姐：柳雪
             * CreateDate : 2018-07-13T16:38:14.477
             * IPAddress : 222.190.122.89
             * Recommand : 0
             * Comment : 0
             * Praise : 5
             * ImgCount : 4
             * ViewCount : 82
             * Status : 0
             */

            private Object NickName;
            private Object HeadPic;
            private int Authentication;
            private Object KidneySign;
            private Object Account;
            private Object Domain;
            private boolean IsFriendRecommend;
            private String PhotoTypeName;
            private int ChUserId;
            private Object ChNickName;
            private Object ChDomain;
            private Object ChHeadPic;
            private int ChAuthentication;
            private Object ChKidneySign;
            private int ActionType;
            private int IsRecommend;
            private int IsPraise;
            private int AllowFocus;
            private String CreateTime;
            private Object RefrenceId;
            private Object CommentContent;
            private Object RefrenceComment;
            private int Id;
            private int UserId;
            private int ShareType;
            private int PhotoType;
            private String Title;
            private String RoleName;
            private String CoserName;
            private String DefaultImage;
            private String Content;
            private String CreateDate;
            private String IPAddress;
            private int Recommand;
            private int Comment;
            private int Praise;
            private int ImgCount;
            private int ViewCount;
            private int Status;
            private List<PhotoListsBean> PhotoLists;

            public Object getNickName() {
                return NickName;
            }

            public void setNickName(Object NickName) {
                this.NickName = NickName;
            }

            public Object getHeadPic() {
                return HeadPic;
            }

            public void setHeadPic(Object HeadPic) {
                this.HeadPic = HeadPic;
            }

            public int getAuthentication() {
                return Authentication;
            }

            public void setAuthentication(int Authentication) {
                this.Authentication = Authentication;
            }

            public Object getKidneySign() {
                return KidneySign;
            }

            public void setKidneySign(Object KidneySign) {
                this.KidneySign = KidneySign;
            }

            public Object getAccount() {
                return Account;
            }

            public void setAccount(Object Account) {
                this.Account = Account;
            }

            public Object getDomain() {
                return Domain;
            }

            public void setDomain(Object Domain) {
                this.Domain = Domain;
            }

            public boolean isIsFriendRecommend() {
                return IsFriendRecommend;
            }

            public void setIsFriendRecommend(boolean IsFriendRecommend) {
                this.IsFriendRecommend = IsFriendRecommend;
            }

            public String getPhotoTypeName() {
                return PhotoTypeName;
            }

            public void setPhotoTypeName(String PhotoTypeName) {
                this.PhotoTypeName = PhotoTypeName;
            }

            public int getChUserId() {
                return ChUserId;
            }

            public void setChUserId(int ChUserId) {
                this.ChUserId = ChUserId;
            }

            public Object getChNickName() {
                return ChNickName;
            }

            public void setChNickName(Object ChNickName) {
                this.ChNickName = ChNickName;
            }

            public Object getChDomain() {
                return ChDomain;
            }

            public void setChDomain(Object ChDomain) {
                this.ChDomain = ChDomain;
            }

            public Object getChHeadPic() {
                return ChHeadPic;
            }

            public void setChHeadPic(Object ChHeadPic) {
                this.ChHeadPic = ChHeadPic;
            }

            public int getChAuthentication() {
                return ChAuthentication;
            }

            public void setChAuthentication(int ChAuthentication) {
                this.ChAuthentication = ChAuthentication;
            }

            public Object getChKidneySign() {
                return ChKidneySign;
            }

            public void setChKidneySign(Object ChKidneySign) {
                this.ChKidneySign = ChKidneySign;
            }

            public int getActionType() {
                return ActionType;
            }

            public void setActionType(int ActionType) {
                this.ActionType = ActionType;
            }

            public int getIsRecommend() {
                return IsRecommend;
            }

            public void setIsRecommend(int IsRecommend) {
                this.IsRecommend = IsRecommend;
            }

            public int getIsPraise() {
                return IsPraise;
            }

            public void setIsPraise(int IsPraise) {
                this.IsPraise = IsPraise;
            }

            public int getAllowFocus() {
                return AllowFocus;
            }

            public void setAllowFocus(int AllowFocus) {
                this.AllowFocus = AllowFocus;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }

            public Object getRefrenceId() {
                return RefrenceId;
            }

            public void setRefrenceId(Object RefrenceId) {
                this.RefrenceId = RefrenceId;
            }

            public Object getCommentContent() {
                return CommentContent;
            }

            public void setCommentContent(Object CommentContent) {
                this.CommentContent = CommentContent;
            }

            public Object getRefrenceComment() {
                return RefrenceComment;
            }

            public void setRefrenceComment(Object RefrenceComment) {
                this.RefrenceComment = RefrenceComment;
            }

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public int getUserId() {
                return UserId;
            }

            public void setUserId(int UserId) {
                this.UserId = UserId;
            }

            public int getShareType() {
                return ShareType;
            }

            public void setShareType(int ShareType) {
                this.ShareType = ShareType;
            }

            public int getPhotoType() {
                return PhotoType;
            }

            public void setPhotoType(int PhotoType) {
                this.PhotoType = PhotoType;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public String getRoleName() {
                return RoleName;
            }

            public void setRoleName(String RoleName) {
                this.RoleName = RoleName;
            }

            public String getCoserName() {
                return CoserName;
            }

            public void setCoserName(String CoserName) {
                this.CoserName = CoserName;
            }

            public String getDefaultImage() {
                return DefaultImage;
            }

            public void setDefaultImage(String DefaultImage) {
                this.DefaultImage = DefaultImage;
            }

            public String getContent() {
                return Content;
            }

            public void setContent(String Content) {
                this.Content = Content;
            }

            public String getCreateDate() {
                return CreateDate;
            }

            public void setCreateDate(String CreateDate) {
                this.CreateDate = CreateDate;
            }

            public String getIPAddress() {
                return IPAddress;
            }

            public void setIPAddress(String IPAddress) {
                this.IPAddress = IPAddress;
            }

            public int getRecommand() {
                return Recommand;
            }

            public void setRecommand(int Recommand) {
                this.Recommand = Recommand;
            }

            public int getComment() {
                return Comment;
            }

            public void setComment(int Comment) {
                this.Comment = Comment;
            }

            public int getPraise() {
                return Praise;
            }

            public void setPraise(int Praise) {
                this.Praise = Praise;
            }

            public int getImgCount() {
                return ImgCount;
            }

            public void setImgCount(int ImgCount) {
                this.ImgCount = ImgCount;
            }

            public int getViewCount() {
                return ViewCount;
            }

            public void setViewCount(int ViewCount) {
                this.ViewCount = ViewCount;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public List<PhotoListsBean> getPhotoLists() {
                return PhotoLists;
            }

            public void setPhotoLists(List<PhotoListsBean> PhotoLists) {
                this.PhotoLists = PhotoLists;
            }

            public static class PhotoListsBean {
                /**
                 * Id : 70907
                 * ShareId : 10250
                 * ShareType : 2
                 * PicPath : http://7xqgnb.com1.z0.glb.clouddn.com/9ad78da9-f133-eee1-f9f2-442c9c7b010f.jpg?imageView2/2/w/540/q/100
                 * SortIndex : 1
                 * IsDefault : 0
                 */

                private int Id;
                private int ShareId;
                private int ShareType;
                private String PicPath;
                private int SortIndex;
                private int IsDefault;

                public int getId() {
                    return Id;
                }

                public void setId(int Id) {
                    this.Id = Id;
                }

                public int getShareId() {
                    return ShareId;
                }

                public void setShareId(int ShareId) {
                    this.ShareId = ShareId;
                }

                public int getShareType() {
                    return ShareType;
                }

                public void setShareType(int ShareType) {
                    this.ShareType = ShareType;
                }

                public String getPicPath() {
                    return PicPath;
                }

                public void setPicPath(String PicPath) {
                    this.PicPath = PicPath;
                }

                public int getSortIndex() {
                    return SortIndex;
                }

                public void setSortIndex(int SortIndex) {
                    this.SortIndex = SortIndex;
                }

                public int getIsDefault() {
                    return IsDefault;
                }

                public void setIsDefault(int IsDefault) {
                    this.IsDefault = IsDefault;
                }
            }
        }

        public static class UserBean {
            /**
             * UserId : 8221
             * Account : null
             * Phone : null
             * Password : null
             * Email : null
             * RoleType : 0
             * AreaCode : 320100
             * AreaName : null
             * Domain : coser8221
             * Status : 1
             * NickName : 雪殇
             * Gender : 0
             * Age : 20
             * AgeSecrecy : true
             * Birthday : 1997-01-06T00:00:00
             * HeadPic : http://7xqgnb.com1.z0.glb.clouddn.com/04e68227-a133-c63f-9e52-c26438d2b07a.jpg?imageMogr2/thumbnail/!280x280r/gravity/Center/crop/280x280/interlace/1/quality/100/format/jpg
             * KidneySign :
             * AutoTag : 2
             * VipLevel : 0
             * Authentication : 0
             * FansCount : 64
             * FocusCount : 9
             * ImgCount : 26
             * ShareCount : 1
             * CommentCount : 1
             * AllowFocus : 0
             * FocusUserId : 0
             * Id : 0
             * Distance : null
             */

            private int UserId;
            private Object Account;
            private Object Phone;
            private Object Password;
            private Object Email;
            private int RoleType;
            private int AreaCode;
            private Object AreaName;
            private String Domain;
            private int Status;
            private String NickName;
            private int Gender;
            private int Age;
            private boolean AgeSecrecy;
            private String Birthday;
            private String HeadPic;
            private String KidneySign;
            private String AutoTag;
            private int VipLevel;
            private int Authentication;
            private int FansCount;
            private int FocusCount;
            private int ImgCount;
            private int ShareCount;
            private int CommentCount;
            private int AllowFocus;
            private int FocusUserId;
            private int Id;
            private Object Distance;

            public int getUserId() {
                return UserId;
            }

            public void setUserId(int UserId) {
                this.UserId = UserId;
            }

            public Object getAccount() {
                return Account;
            }

            public void setAccount(Object Account) {
                this.Account = Account;
            }

            public Object getPhone() {
                return Phone;
            }

            public void setPhone(Object Phone) {
                this.Phone = Phone;
            }

            public Object getPassword() {
                return Password;
            }

            public void setPassword(Object Password) {
                this.Password = Password;
            }

            public Object getEmail() {
                return Email;
            }

            public void setEmail(Object Email) {
                this.Email = Email;
            }

            public int getRoleType() {
                return RoleType;
            }

            public void setRoleType(int RoleType) {
                this.RoleType = RoleType;
            }

            public int getAreaCode() {
                return AreaCode;
            }

            public void setAreaCode(int AreaCode) {
                this.AreaCode = AreaCode;
            }

            public Object getAreaName() {
                return AreaName;
            }

            public void setAreaName(Object AreaName) {
                this.AreaName = AreaName;
            }

            public String getDomain() {
                return Domain;
            }

            public void setDomain(String Domain) {
                this.Domain = Domain;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public String getNickName() {
                return NickName;
            }

            public void setNickName(String NickName) {
                this.NickName = NickName;
            }

            public int getGender() {
                return Gender;
            }

            public void setGender(int Gender) {
                this.Gender = Gender;
            }

            public int getAge() {
                return Age;
            }

            public void setAge(int Age) {
                this.Age = Age;
            }

            public boolean isAgeSecrecy() {
                return AgeSecrecy;
            }

            public void setAgeSecrecy(boolean AgeSecrecy) {
                this.AgeSecrecy = AgeSecrecy;
            }

            public String getBirthday() {
                return Birthday;
            }

            public void setBirthday(String Birthday) {
                this.Birthday = Birthday;
            }

            public String getHeadPic() {
                return HeadPic;
            }

            public void setHeadPic(String HeadPic) {
                this.HeadPic = HeadPic;
            }

            public String getKidneySign() {
                return KidneySign;
            }

            public void setKidneySign(String KidneySign) {
                this.KidneySign = KidneySign;
            }

            public String getAutoTag() {
                return AutoTag;
            }

            public void setAutoTag(String AutoTag) {
                this.AutoTag = AutoTag;
            }

            public int getVipLevel() {
                return VipLevel;
            }

            public void setVipLevel(int VipLevel) {
                this.VipLevel = VipLevel;
            }

            public int getAuthentication() {
                return Authentication;
            }

            public void setAuthentication(int Authentication) {
                this.Authentication = Authentication;
            }

            public int getFansCount() {
                return FansCount;
            }

            public void setFansCount(int FansCount) {
                this.FansCount = FansCount;
            }

            public int getFocusCount() {
                return FocusCount;
            }

            public void setFocusCount(int FocusCount) {
                this.FocusCount = FocusCount;
            }

            public int getImgCount() {
                return ImgCount;
            }

            public void setImgCount(int ImgCount) {
                this.ImgCount = ImgCount;
            }

            public int getShareCount() {
                return ShareCount;
            }

            public void setShareCount(int ShareCount) {
                this.ShareCount = ShareCount;
            }

            public int getCommentCount() {
                return CommentCount;
            }

            public void setCommentCount(int CommentCount) {
                this.CommentCount = CommentCount;
            }

            public int getAllowFocus() {
                return AllowFocus;
            }

            public void setAllowFocus(int AllowFocus) {
                this.AllowFocus = AllowFocus;
            }

            public int getFocusUserId() {
                return FocusUserId;
            }

            public void setFocusUserId(int FocusUserId) {
                this.FocusUserId = FocusUserId;
            }

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public Object getDistance() {
                return Distance;
            }

            public void setDistance(Object Distance) {
                this.Distance = Distance;
            }
        }
    }
}
