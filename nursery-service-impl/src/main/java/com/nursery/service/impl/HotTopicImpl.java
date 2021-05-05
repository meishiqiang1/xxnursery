package com.nursery.service.impl;

import com.alibaba.fastjson.JSON;
import com.nursery.api.iservice.IHotTopicSV;
import com.nursery.beans.HotTopicDO;
import com.nursery.beans.TopicCommentDO;
import com.nursery.dao.CommentMapper;
import com.nursery.dao.DomesticConsumerMapper;
import com.nursery.dao.HotTopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * author:MeiShiQiang
 * Date:2021/3/1 | Time:9:37
 */
@Service
public class HotTopicImpl implements IHotTopicSV {
    private Logger logger = LoggerFactory.getLogger(IHotTopicSV.class);

    @Autowired
    @SuppressWarnings("all")
    private HotTopicMapper hotTopicMapper;

    @Autowired
    @SuppressWarnings("all")
    private CommentMapper commentMapper;

    @Autowired
    @SuppressWarnings("all")
    //用户表信息
    private DomesticConsumerMapper consumerMapper;


    @Override
    public List<HotTopicDO> getTopicRandom() throws SQLException {
        List<HotTopicDO> topicDOList = hotTopicMapper.selectTopicRandom();
        logger.info(JSON.toJSONString(topicDOList));
        return topicDOList;
    }



    @Override
    public HotTopicDO getTopic(String tableid) throws SQLException {
        return hotTopicMapper.selectTopic(tableid);
    }

    @Override
    public void insertTopicComment(Map<String, String> map) throws SQLException {
        commentMapper.insertTopicComment(map);
    }

    @Override
    public List<TopicCommentDO> selectCommentDOByConsumerId(String consumerName) {
        String consumerId = "";
        try {
            consumerId = consumerMapper.selectConsumerIdByConsumerNickName(consumerName);
        }catch (Exception e){
            logger.warn(this.getClass().getName()+ "错误信息" + e.getLocalizedMessage());
        }
        //2021、5、2  增加评论类信息
        return commentMapper.selectCommentDOByConsumerId(consumerId);
    }
}
