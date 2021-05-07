package com.nursery.api.iweb;

import com.nursery.beans.bo.RegisterBO;
import com.nursery.common.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户注册api
 */
@Api(value = "/consumer",description = "用户注册")
public interface ConsumerRegisterApi {


    @ApiOperation("创建用户")
    ResponseResult  registerConsumer(RegisterBO registerBO);
}
