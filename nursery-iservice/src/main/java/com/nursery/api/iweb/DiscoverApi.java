package com.nursery.api.iweb;

import com.nursery.beans.DBDataParam;
import com.nursery.common.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * author:MeiShiQiang
 * Date:2021/2/25 | Time:16:49
 */
@Api(value = "/discover", description = "最新内容，发现")
public interface DiscoverApi {

    @ApiOperation("访问发现页面")
    ModelAndView visitDiscover(RedirectAttributes attr, String param);

    @ApiOperation("问题跳转")
    ModelAndView visitWenti(String tableid, String param, RedirectAttributes attr);

    @ApiOperation("发表言论")
    public ResponseResult publishDiscoverAnswer(DBDataParam dataParam);
}
