package com.nursery.service.impl;

import com.nursery.api.iservice.IRecruitAndConsumerSV;
import com.nursery.beans.RecruitAndConsumerDO;
import com.nursery.dao.RecruitAndConsumerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * author:MeiShiQiang
 * Date:2021/3/15 | Time:14:12
 */
@Service
public class RecruitAndConsumerImpl implements IRecruitAndConsumerSV {

    @Autowired
    @SuppressWarnings("all")
    private RecruitAndConsumerMapper recruitAndConsumerMapper;

    @Override
    public List<RecruitAndConsumerDO> getDOsByRecruitId(String id) throws SQLException {
        return recruitAndConsumerMapper.selectDomeByRecruitId(id);
    }

    @Override
    public int resumePass(String consumerId, String recruitId) throws SQLException {
        RecruitAndConsumerDO recruitAndConsumerDO = recruitAndConsumerMapper.findResumeAndConsumerDO(consumerId,recruitId);
        if (!ObjectUtils.isEmpty(recruitAndConsumerDO)){
            return recruitAndConsumerMapper.updatePassByConsumerIdAndRecruitId(consumerId, recruitId);
        }
        return 0;
    }

    @Override
    public int resumeNoPass(String consumerId, String recruitId) throws SQLException {
        RecruitAndConsumerDO recruitAndConsumerDO = recruitAndConsumerMapper.findResumeAndConsumerDO(consumerId,recruitId);
        if (!ObjectUtils.isEmpty(recruitAndConsumerDO)){
            return recruitAndConsumerMapper.updateNoPassByConsumerIdAndRecruitId(consumerId, recruitId);
        }
        return 0;
    }

    @Override
    public void interviewRecruit(String consumerId, String recruitId) throws SQLException {
        RecruitAndConsumerDO recruitAndConsumerDO = recruitAndConsumerMapper.findResumeAndConsumerDO(consumerId,recruitId);
        if (!ObjectUtils.isEmpty(recruitAndConsumerDO)) {
            recruitAndConsumerMapper.updateResult(consumerId, recruitId);
        }
    }
}
