package com.nursery.api.iwebm;

import com.alibaba.fastjson.JSONObject;
import com.nursery.beans.RecruitmentDO;
import com.nursery.beans.bo.RecruitBO;
import com.nursery.common.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author:MeiShiQiang
 * Date:2021/1/27 | Time:11:42
 */
@Api(value = "/manage/recruit",description = "招聘管理")
public interface ManageRecruitApi {

    @ApiOperation(value = "更新招聘信息")
    public ResponseResult putRecruitInfo(RecruitBO recruitBO);

    @ApiOperation(value = "发布招聘信息")
    public ResponseResult postRecruitInfo(RecruitmentDO recruitmentDO);

    @ApiOperation(value = "下载简历表")
    public JSONObject downloadResume(String consumerId);

    @ApiOperation(value = "查询简历表")
    public ResponseResult lookResume(String consumerId);

    @ApiOperation(value = "审核招聘")
    public ResponseResult auditRecruit(String param);

}
