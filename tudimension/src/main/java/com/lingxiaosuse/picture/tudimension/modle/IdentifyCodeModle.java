package com.lingxiaosuse.picture.tudimension.modle;

public class IdentifyCodeModle {

    /**
     * code : 000000
     * count : 1
     * create_date : 2018-09-02 20:59:19
     * mobile : 15182631360
     * msg : OK
     * smsid : a88b82407eae24e58aac558846a69849
     * uid :
     */

    private String code;
    private String count;
    private String create_date;
    private String mobile;
    private String msg;
    private String smsid;
    private String uid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSmsid() {
        return smsid;
    }

    public void setSmsid(String smsid) {
        this.smsid = smsid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "IdentifyCodeModle{" +
                "code='" + code + '\'' +
                ", count='" + count + '\'' +
                ", create_date='" + create_date + '\'' +
                ", mobile='" + mobile + '\'' +
                ", msg='" + msg + '\'' +
                ", smsid='" + smsid + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
