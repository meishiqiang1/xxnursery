package com.nursery.beans.code;

import com.google.common.collect.ImmutableMap;
import com.nursery.common.model.response.ResultCode;
import lombok.ToString;

/**
 * author:MeiShiQiang
 * Date:2021/2/3 | Time:16:45
 * 用户
 */
@ToString
public enum ConsumerCode implements ResultCode {

    CONSUMER_SQL_SUCCEED(true,13001,"用户信息更新成功！"),
    CONSUMER_SQL_FAIL(false,13002,"用户信息操作数据库错误！"),
    CONSUMER_PARAM_NONE(false,13003,"请传入正确的参数信息！"),
    CONSUMER_PASSWORD_NONE(false,13004,"请输入密码！"),
    CONSUMER_VERIFYCODE_NONE(false,13005,"请输入验证码！"),
    CONSUMER_ACCOUNT_NOTEXISTS(false,13006,"账号不存在！"),
    CONSUMER_CREDENTIAL_ERROR(false,13007,"账号或密码错误！"),
    CONSUMER_LOGIN_ERROR(false,13008,"登陆过程出现异常请尝试重新操作！"),
    CONSUMER_SELECT_LIST_ISNULL(true,13008,"查询为空！"),
    CONSUMER_SQL_SELECT_FAIL(true,13008,"查询失败！");

    boolean success;

    int code;

    String message;

    ConsumerCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private static final ImmutableMap<Integer, ConsumerCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, ConsumerCode> builder = ImmutableMap.builder();
        for (ConsumerCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
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
