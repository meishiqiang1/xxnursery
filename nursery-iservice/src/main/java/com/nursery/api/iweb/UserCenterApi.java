package com.nursery.api.iweb;

import com.nursery.beans.DomesticConsumerDO;
import com.nursery.common.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

/**
 * 个人用户中心页面  personalCenterPage.jsp
 */
@Api(value = "/consumer",description = "个人用户中心信息")
public interface UserCenterApi {

    @ApiOperation("访问个人中心页面")
    public ModelAndView visitUserInfoPage(ModelAndView modelAndView);

    @ApiOperation("访问用户编辑页面")
    public ModelAndView visitUserEditByID(ModelAndView modelAndView);

    @ApiOperation("更新个人信息内容")
    public String pullUser(DomesticConsumerDO domesticConsumerDO);

    @ApiOperation("更新个人头像")
    public void pullImage(String base64);

    @ApiOperation("查询个人的大概信息,,,")
    ResponseResult generalContent(String consumerID);

}
