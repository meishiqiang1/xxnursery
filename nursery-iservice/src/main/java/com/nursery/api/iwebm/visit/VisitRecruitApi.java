package com.nursery.api.iwebm.visit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

/**
 * author:MeiShiQiang
 * Date:2021/2/3 | Time:17:40
 */
@Api(value = "/manage/recruit",description = "招聘管理-页面跳转")
public interface VisitRecruitApi {

    @ApiOperation("获取当前用户发布的招聘信息")
    ModelAndView getRecruitManage();

    @ApiOperation("获取所有的招聘信息")
    ModelAndView getRecruitManages();

    @ApiOperation("获取单个详细招聘信息")
    ModelAndView getRecruitInfoByrecruitid(String recruitid);

    @ApiOperation("获取单个详细招聘信息")
    ModelAndView getRecruitInfoByid(String recruitid);

    @ApiOperation("访问-发布招聘")
    ModelAndView visitPullRecruitPage(ModelAndView modelAndView);

}
