package com.nursery.api.iservice;

import com.nursery.beans.DomesticConsumerResumeDO;

import java.sql.SQLException;
import java.util.Map;

/**
 * author:MeiShiQiang
 * Date:2021/2/26 | Time:11:34
 * 个人简历业务
 */
public interface IConsumerResumeSV {
    //保存简历信息
    void insertResume(DomesticConsumerResumeDO consumerResumeDO) throws SQLException;

    void delectByid(String consumerResumeId);

    DomesticConsumerResumeDO findResumeDOByConsuemrName(String name) throws SQLException;

    Map<String,String> findResumeURLByConsumerName(String name) throws SQLException;

    boolean findResumeAndConsumerDO(String recruitId, String consumerId) throws SQLException;
}
