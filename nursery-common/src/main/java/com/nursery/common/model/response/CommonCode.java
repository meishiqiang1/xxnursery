package com.nursery.common.model.response;

import lombok.ToString;

@ToString
public enum CommonCode implements ResultCode{

    SUCCESS(true,10000,"操作成功"),
    UNAUTHENTICATED(false,10001,"此操作需要登陆系统！"),
    UNAUTHORISE(false,10002,"权限不足，无权操作！"),
    INVALID_PARAM(false,10003,"非法参数！"),
    LOGIN_CHANNEL_IS_FAIL(false,10004,"请选择正确的登录方式！"),
    FAIL(false,11111,"操作失败！"),
    IS_EXIST(false,11112,"抱歉,已存在！"),
    PASSWORDISEQUALITY(false,11113,"密码不能喝原密码相同！"),
    PASSWORDISNOTFORMAT(false,11114,"密码格式不正确！"),
    SELECTISFAIL(false,11115,"查询失败！"),
    AUDIT_PASS_RESULT_NOT(true,11116,"审核操作失败，请查看服务器情况！"),
    AUDIT_PASS_RESULT_YES(true,11117,"提交审核成功！"),
    SQL_YJ_ISNOT(false,11118,"数据库语句出错！"),
    CONSUMER_IS_EXIST(false,11119,"用户已经存在！"),
    NOT_EXIST_RESUME(false,11120,"操作失败，没有上传简历！"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！");

    //    private static ImmutableMap<Integer, CommonCode> codes ;
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private CommonCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
