package com.nursery.common.exception;


import com.nursery.common.model.response.ResultCode;

/**
 * 异常抛出类
 * Create with IDEA
 * author:MeiShiQiang
 */
public class ExceptionCast {

    public static void  cast(ResultCode resultCode){
        throw  new CustomException(resultCode);
    }
}
