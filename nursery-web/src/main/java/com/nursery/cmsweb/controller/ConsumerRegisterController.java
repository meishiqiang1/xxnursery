package com.nursery.cmsweb.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nursery.api.iservice.IDomesticConsumerSV;
import com.nursery.api.iweb.ConsumerRegisterApi;
import com.nursery.beans.DomesticConsumerDO;
import com.nursery.beans.bo.RegisterBO;
import com.nursery.beans.code.ConsumerCode;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.ResponseResult;
import com.nursery.common.web.BaseController;
import com.nursery.utils.CommonUtil;
import com.nursery.utils.DateUtils;
import com.nursery.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 需要服务
 * 短信接口
 * 邮箱接口
 * 获取当前省份
 */
@Controller
public class ConsumerRegisterController extends BaseController implements ConsumerRegisterApi {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerRegisterController.class);
    @Autowired
    IDomesticConsumerSV domesticConsumerSV;

    /**
     * 注册
     * consumer/register
     */
    @RequestMapping(value = "/consumer/register2",method = RequestMethod.POST)
    @ResponseBody
    @Override
    public ResponseResult registerConsumer(RegisterBO registerBO) {
        ResponseResult responseResult = new ResponseResult();
        String consumerXing = "";
        String consumerAge = "";
        String consumerURL = "";
        DomesticConsumerDO consumerDO = new DomesticConsumerDO();
        consumerDO.setConsumerNickname(registerBO.getNickname());
        consumerDO.setConsumerSex(registerBO.getGender());
        consumerDO.setConsumerPass(registerBO.getPassword());
        consumerDO.setConsumerAddress(registerBO.getAddress());
        try {
            String uuid = CommonUtil.getUUID();
            if (uuid != null && uuid.length() == 32) {
                consumerDO.setConsumerID(uuid);
            } else {
                logger.warn("获取id错误");
                responseResult.setCommonCode(CommonCode.FAIL);
                return responseResult;
            }
            //校验邮箱；
            String email = registerBO.getEmail();
            if (!StringUtils.isEmpty(email) && EmailUtils.verify(email)){
                consumerDO.setConsumerEmail(email);
            }else {
                logger.warn("邮箱检验不正确");
                responseResult.setCommonCode(ConsumerCode.CONSUMER_VERIFY_EMAIL_NOT);
                return responseResult;
            }
            String realName = registerBO.getRealName();
            if (realName != "" && !StringUtils.isEmpty(realName)) {
                consumerXing = realName.substring(0, 1);
                consumerDO.setConsumerName(realName);
                consumerDO.setConsumerXing(consumerXing);
            }else {
                logger.warn("姓名格式不对");
                responseResult.setCommonCode(ConsumerCode.CONSUMER_REAL_NAME_WRONG);
                return responseResult;
            }
            //获取年龄
            String birthday = registerBO.getBirthday();
            if (!StringUtils.isEmpty(birthday)) {
                try {
                    consumerAge = DateUtils.getAge(birthday);
                }catch (Exception e){
                    logger.warn("日期格式不对");
                    responseResult.setCommonCode(ConsumerCode.CONSUMER_DATE_WRONG);
                    return responseResult;
                }
                consumerDO.setConsumerBirthday(birthday);
                consumerDO.setConsumerAge(consumerAge);
            }
            if (StringUtils.isEmpty(consumerURL)) {
                //  后期从数据库中获取   YLY_ZP_IMAGE_HEAR_URL
                consumerURL = "image1";
                consumerDO.setConsumerURL(consumerURL);
            }
            //获取当前时间
            String nowDay = DateUtils.getNowDate(DateUtils.YYYYMMDDHHMMSS);
            consumerDO.setConsumerJoinDay(nowDay);
            logger.info("consumerid: " + consumerDO.getConsumerID() + "register注册参数：" + JSON.toJSONString(consumerDO));
            responseResult = domesticConsumerSV.insertConsumer(consumerDO);
        } catch (Exception e) {
            responseResult.setCommonCode(ConsumerCode.CONSUMER_REAL_NAME_WRONG);
        }
        return responseResult;
    }

}
