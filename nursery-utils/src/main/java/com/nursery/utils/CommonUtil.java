package com.nursery.utils;

import com.alibaba.druid.util.StringUtils;
import com.nursery.common.model.CommonAttrs;

import java.util.Random;
import java.util.UUID;

public class CommonUtil {

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    public static String getRandomNum(int x, int y) {
        Random random = new Random();
        x = random.nextInt(x);
        y = random.nextInt(y);
        return x + y + "";
    }

    public static String getVerifyCode(int i) {
        return null;
    }


    //获取经验
    public static String getExperience(String experienceCode) {
        switch (experienceCode) {
            case "0":
                return "不限";
            case "1":
                return "在校生";
            case "2":
                return "应届生";
            case "3":
                return "1年以内";
            case "4":
                return "1-3年";
            case "5":
                return "3-5年";
            case "6":
                return "5-10年";
            case "7":
                return "10年以上";
        }
        return "不限";
    }

    /**
     * 学历要求
     *
     * @param requireEduDB 学历code
     * @return
     */
    public static String getRequureEdu(String requireEduDB) {
        switch (requireEduDB) {
            case "0":
                return "不限";
            case "1":
                return "大专";
            case "2":
                return "本科";
            case "3":
                return "硕士";
            case "4":
                return "博士";
        }
        return "不限";
    }

    public static String getUrlByReferer(String referer) {
        if (StringUtils.isEmpty(referer)) {
            return "/index";
        }
        return referer.substring(22, referer.length());
    }

    public static String getIntroduce(String id) {
        return "其他职业者";
    }

    public static String getPlace(String placeId) {
        String number = placeId.substring(13, placeId.length());
        switch (number) {
            case "0":
                return "金水";
            case "1":
                return "管城回族";
            case "2":
                return "中原";
            case "3":
                return "二七";
            case "4":
                return "新郑";
            case "5":
                return "惠济";
            case "6":
                return "中牟";
            case "7":
                return "荥阳";
            case "8":
                return "新密";
            case "9":
                return "巩义";
            case "10":
                return "上街";
            case "11":
                return "登封";
            case "12":
                return "不限";
        }
        return "";
    }

    public static String getEdc(String edcId) {
        return edcId.substring(8, edcId.length());
    }

    public static String getCondition(String conditionId) {
        return conditionId.substring(14, conditionId.length());
    }

    public static String[] getPutTime(String putTimeId) {
        String number = putTimeId.substring(12, putTimeId.length());
        String[] dates = new String[2];
        switch (number) {
            case "1":
                dates[0] = DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getDayBegin());
                dates[1] = DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getDayEnd());
                return dates;
            case "2":
                dates[0] =  DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getBeginDayOfYesterday());
                dates[1] =  DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getEndDayOfTomorrow());
                return dates;
            case "3":
                dates[0] =  DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getBeginDayOfWeek());
                dates[1] =  DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getEndDayOfWeek());
                return dates;
            case "4":
                dates[0] =  DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getBeginDayOfMonth());
                dates[1] =  DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getEndDayOfMonth());
                return dates;
            case "5":
                dates[0] =  DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getBeginDayOfYear());
                dates[1] =  DateUtils.getStrDate(CommonAttrs.SQL_DATE_FORMAT,DateUtils.getEndDayOfYear());
                return dates;
            default:
                return null;
        }
        /*一天以内 三天以内 七天以内 一个月以内 一年以内*/
    }
}