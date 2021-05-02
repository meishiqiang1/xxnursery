package com.nursery.dao;

import com.nursery.beans.RecruitAndConsumerDO;

import java.sql.SQLException;
import java.util.List;

/**
 * author:MeiShiQiang
 * Date:2021/3/15 | Time:14:10
 *
 */
public interface RecruitAndConsumerMapper {

    /**
     * 根据招聘id，从中间表中获取简历信息
     * @param id 招聘id
     * @throws SQLException
     */
    List<RecruitAndConsumerDO> selectDomeByRecruitId(String id) throws SQLException;

    /**
     * 根据y用户id，从中间表中获取招聘信息
     * @param id 用户id
     * @throws SQLException
     */
    List<RecruitAndConsumerDO> selectDomeByConsumerId(String consumerId)throws SQLException;

    void insertRecruitAndConsumer(RecruitAndConsumerDO recruitAndConsumerDO);

    RecruitAndConsumerDO findResumeAndConsumerDO(String recruitId, String consumerId) throws SQLException;
}
