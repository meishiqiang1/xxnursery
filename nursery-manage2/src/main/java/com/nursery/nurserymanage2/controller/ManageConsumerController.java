package com.nursery.nurserymanage2.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nursery.api.iservice.IDomesticConsumerSV;
import com.nursery.api.iservice.IUserCenterSV;
import com.nursery.api.iwebm.ManageConsumerApi;
import com.nursery.beans.DomesticConsumerDO;
import com.nursery.beans.code.ConsumerCode;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.ResponseResult;
import com.nursery.common.web.BaseController;
import com.nursery.utils.CellUtils;
import com.nursery.utils.DateUtils;
import com.nursery.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


/**
 * @author: MeiShiQiang
 * Date:2021/2/8 | Time:14:19
 * 个人中心 controller
 */
@Controller
@RequestMapping("/manage/consumer")
public class ManageConsumerController extends BaseController implements ManageConsumerApi {

    private Logger logger = LoggerFactory.getLogger(ManageConsumerController.class);

    @Autowired
    private IDomesticConsumerSV domesticConsumerSV;

    @Autowired
    private IUserCenterSV userCenterSV;

    /**
     * 更新用户
     *
     * @param consumerDO 参数
     */
    @RequestMapping(value = "/putConsumer", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public ResponseResult putConsumer(DomesticConsumerDO consumerDO) {
        //初始化返回值
        ResponseResult responseResult = ResponseResult.FAIL();
        String name = consumerDO.getConsumerName();
        String educationBg = consumerDO.getConsumerEducationBg();
        String consumerEmail = consumerDO.getConsumerEmail();
        String cellPhone = consumerDO.getConsumerCellPhone();
        String birthday = consumerDO.getConsumerBirthday();
        if (educationBg != null) {
            String[] bg = educationBg.split("/");
            String college = bg[0];//大学
            String department = bg[1];//院系
            String specialty = bg[2];//专业
            String degree = bg[3];//学位
            if (!college.equals("null")) consumerDO.setConsumerEduBgCollege(college);
            if (!department.equals("null")) consumerDO.setConsumerEduBgDepartment(department);
            if (!specialty.equals("null")) consumerDO.setConsumerEduBgSpecialty(specialty);
            if (!degree.equals("null")) consumerDO.setConsumerEduBgDegree(degree);
        }
        //检验邮箱和手机号
        if (consumerEmail != null) {
            if (!EmailUtils.verify(consumerEmail)) {
                responseResult.setCommonCode(ConsumerCode.CONSUMER_VERIFY_EMAIL_NOT);
                return responseResult;
            }
        }
        if (cellPhone != null) {
            if (!CellUtils.verify(cellPhone)) {
                responseResult.setCommonCode(ConsumerCode.CONSUMER_VERIFY_CELL_NOT);
                return responseResult;
            }
        }
        //计算年龄
        String age = "";
        try {
            age = DateUtils.getAge(birthday);
            consumerDO.setConsumerAge(age);
        } catch (ParseException e) {
            logger.error("error:计算年龄错误");
        }
        //姓名校验
        if (!validateName(name)) {
            logger.info("姓名校验不合格");
        }
        //判断是否参数有误
        if (StringUtils.isEmpty(consumerDO.getConsumerID())) {
            logger.warn("参数不对，没有id值");
            responseResult.setCommonCode(ConsumerCode.CONSUMER_PARAM_ID_ISNOT);
            return responseResult;
        }
        try {
            logger.warn("putConsumer: consumerDO==>" + JSON.toJSONString(consumerDO));
            int i = domesticConsumerSV.updateConsumer(consumerDO);
            //判断更新是否成功
            if (i > 0) {
                logger.error("putConsumer： 更新成功,影响行数" + i);
                responseResult.setCommonCode(ConsumerCode.CONSUMER_SQL_SUCCEED);
                return responseResult;
            }
        } catch (Exception e) {
            logger.error("putConsumer： 更新失败");
            responseResult.setCommonCode(ConsumerCode.CONSUMER_SQL_FAIL);
            return responseResult;
        }
        return responseResult;
    }

    //更新数据
    @RequestMapping(value = "/pullUser", method = RequestMethod.POST)
    public String pullUser(DomesticConsumerDO consumerDO) {
        String consumerId = consumerDO.getConsumerID();
        ResponseResult responseResult = ResponseResult.FAIL();
        consumerDO.setConsumerID(consumerId);
        responseResult = userCenterSV.pullUser(consumerDO);
        if (responseResult.getCode() == 10000) {
            return "redirect:/manage/consumer/visitConsumerEdit/" + consumerId;
        }
        return "500";
    }

    //更新头像
    @RequestMapping(value = "/pullImage",method = RequestMethod.POST)
    public void pullImage(@RequestParam(name = "base") String base64) {
        //获取用户信息 UserDetails
        String consumerName = "";
        userCenterSV.pullImage(request.getContextPath(),consumerName,base64);
    }

    @RequestMapping(value = "/delete/{consumerId}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult deleteConsumer(@PathVariable(name = "consumerId")String consumerId){
        ResponseResult fail = ResponseResult.FAIL();
        int i = 0;
        try {
            i = domesticConsumerSV.deleteConsumer(consumerId);
        } catch (Exception e) {
            return fail;
        }
        if (i>0){
            fail.setCommonCode(CommonCode.SUCCESS);
        }
        return fail;
    }


    public Boolean validateName(String name) {
        return name.matches("^([\\u4e00-\\u9fa5]{1,20}|[a-zA-Z\\.\\s]{1,20})$");
    }
}
