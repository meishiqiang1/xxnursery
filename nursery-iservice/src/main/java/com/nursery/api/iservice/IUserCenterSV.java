package com.nursery.api.iservice;

import com.nursery.beans.DomesticConsumerDO;
import com.nursery.common.model.response.QueryResponseResult;
import com.nursery.common.model.response.ResponseResult;

/**
 * author:MeiShiQiang
 * Date:2021/4/22 | Time:11:02
 */
public interface IUserCenterSV {
    /**
     * 更具用户名获取用户个人信息
     * @param consumerName
     * @return
     */
    QueryResponseResult getUserCenter(String consumerName);

    /**
     * 更新数据
     * @return
     * @param consumerDO
     */
    ResponseResult pullUser(DomesticConsumerDO consumerDO);

    /**
     * 更新头像
     * @param contextPath 项目路径
     * @param consumerName 用户名
     * @param base64 字节码
     */
    void pullImage(String contextPath, String consumerName, String base64);

}
