package com.lingxiaosuse.picture.tudimension.utils;

import android.text.TextUtils;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检验字符串是否合法
 */

public class StringUtils {
    /**
     * 检验用户名是否合法
     */
    public static boolean CheckUsername(String username){
        if (TextUtils.isEmpty(username)){
            return false;
        }else {
            return username.matches("^[a-zA-Z][0-9a-zA-Z]{4,19}$");
        }
    }
    /**
    * 检验密码的长度
    */
    public static boolean CheckPsd(String psd){
        if (TextUtils.isEmpty(psd)){
            return false;
        }else{
            return psd.matches("^[0-9a-zA-Z]{4,19}$");
        }
    }

    public static String getFirstChar(String text){
        if (TextUtils.isEmpty(text)){
            return null;
        }else {
            return text.substring(0,1).toUpperCase();
        }
    }
    public static String getDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
    /**
     *将date格式字符串转换为时间
     */
    public static String strToDate(String strDate){
        Date date = new Date(Long.valueOf(strDate)*1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType) {
        try {
            Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
            String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
            Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
            return date;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    public static String longToString(long time,String formatType){
        return dateToString(longToDate(time,formatType),formatType);
    }
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    /**
     *将long转换为单位
     */
    public static String getDataSize(long size) {
        if (size < 0) {
            size = 0;
        }
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "0";
        }
    }

    /**
     *格式化字符串为数字
     */
    public static String getPatternPageNum(String msg) {
        String regIP = "[^0-9]";
        //编译正则表达式
        Pattern pattern = Pattern.compile(regIP);
        Matcher matcher = pattern.matcher(msg);
        return matcher.replaceAll("");
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static boolean isNotEmpty(String tmp) {
        if (null == tmp){
            return false;
        }
        if (tmp.isEmpty()){
            return false;
        }
        return true;
    }
}
