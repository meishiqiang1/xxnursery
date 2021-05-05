package com.nursery.cmsweb.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nursery.api.iservice.IDomesticConsumerSV;
import com.nursery.api.iservice.IHotTopicSV;
import com.nursery.api.iweb.DiscoverApi;
import com.nursery.beans.DBDataParam;
import com.nursery.beans.HotTopicDO;
import com.nursery.beans.TopicCommentDO;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.ResponseResult;
import com.nursery.common.web.BaseController;
import com.nursery.utils.CommonUtil;
import com.nursery.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:MeiShiQiang
 * Date:2021/2/25 | Time:16:52
 * 发现- 热门话题
 */
@Controller
public class DiscoverController extends BaseController implements DiscoverApi {
    private Logger logger = LoggerFactory.getLogger(DiscoverController.class);
    public static final String NOW_DATE_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private IHotTopicSV hotTopicSV;
    @Autowired
    private IDomesticConsumerSV domesticConsumerSV;

    // http://localhost:32226/discover/wenti/2?number=jj
    @RequestMapping(value = "/discover/wenti/{tableId}", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public ModelAndView visitWenti(
            @PathVariable(value = "tableId", required = true) String tableId) {
        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("tableid", tableId);
        HotTopicDO topic = null;
        modelAndView.setViewName("discover_wenti");
        try {
            topic = hotTopicSV.getTopic(tableId);
            returnMap.put("tag", topic.getTag());
            returnMap.put("introduce", topic.getIntroduce());
            returnMap.put("startdate", topic.getStartDate());
            returnMap.put("content", topic.getContent());
            returnMap.put("tablename", topic.getTableName());
            returnMap.put("author", topic.getAuthor());
            returnMap.put("authorId", topic.getAuthorId());
            returnMap.put("imgPath", topic.getImgPath());
            //2021、5、4
            returnMap.put("topicId",topic.getId());
            List<TopicCommentDO> commentDOS = topic.getCommentDOS();
            modelAndView.addObject("commentDOS", commentDOS);
        } catch (NullPointerException nullPointerException) {
            System.out.println("null");
            modelAndView.setViewName("404");
        } catch (SQLException throwables) {
            System.out.println("sql");
        }
        logger.info(JSON.toJSONString(returnMap));
        modelAndView.addObject("data", returnMap);
        return modelAndView;
    }

    @RequestMapping(value = {"/discover"}, method = RequestMethod.GET)
    @ResponseBody
    @Override
    public ModelAndView visitDiscover() {
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<HotTopicDO> topicRandom = hotTopicSV.getTopicRandom();
            modelAndView.addObject("data", topicRandom);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        modelAndView.setViewName("discover");
        return modelAndView;
    }


    /**
     * 发表言论
     * @param dataParam 参数
     */
    @RequestMapping(value = {"/discover/answer"})
    @ResponseBody
    @Override
    public ResponseResult publishDiscoverAnswer(DBDataParam dataParam){
        //参数
        ResponseResult responseResult = ResponseResult.FAIL();
        HashMap<String, String> returnMap = new HashMap<>();
        String id = CommonUtil.getUUID();
        //String liushui = dataParam.getParam1();
        String tableId = dataParam.getParam2();
        String answer = dataParam.getParam3();//回答的话
        String consumerName = "";
        String consumerId = "";
        String introduce = "";
        //获取用户信息 UserDetails
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            consumerName = authentication.getName();
            consumerId = domesticConsumerSV.selectConsumerIdByConsumerNickName(consumerName);
        }
        if (!StringUtils.isEmpty(consumerName)){
            Map<String, String> map = new HashMap<>();
            map.put("id",id);
            map.put("content",answer);
            map.put("tableId",tableId);
            map.put("consumer_id",consumerId);
            map.put("consumer_name",consumerName);
            map.put("date", DateUtils.getNowDate(NOW_DATE_YMDHMS));
            if(StringUtils.isEmpty(introduce)){
                introduce = CommonUtil.getIntroduce(consumerId);
            }
            map.put("consumer_introduce",introduce);
            try {
                hotTopicSV.insertTopicComment(map);
            } catch (SQLException throwables) {
                logger.error("错误原因: "+throwables.getMessage()+"错误代码: "+throwables.getSQLState()+"");
                return responseResult;
            }
        }else {
            responseResult.setCommonCode(CommonCode.UNAUTHENTICATED);
            return responseResult;
        }
        responseResult.setCommonCode(CommonCode.SUCCESS);
        returnMap.put("yhu",consumerName);
        responseResult.setBean(returnMap);
        return responseResult;
    }

}
