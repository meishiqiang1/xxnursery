package com.nursery.nurserymanage2.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nursery.api.iservice.IConsumerResumeSV;
import com.nursery.api.iservice.INurseryRecruitInfoSV;
import com.nursery.api.iservice.INurseryRecruiterManagmentSV;
import com.nursery.api.iservice.IRecruitAndConsumerSV;
import com.nursery.api.iwebm.ManageRecruitApi;
import com.nursery.beans.DomesticConsumerResumeDO;
import com.nursery.beans.RecruitmentDO;
import com.nursery.beans.bo.RecruitBO;
import com.nursery.beans.code.RecruitCode;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.ResponseResult;
import com.nursery.common.web.BaseController;
import com.nursery.nurserymanage2.controller.async.LogAsyncComponent;
import com.nursery.utils.CommonUtil;
import com.nursery.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * author:MeiShiQiang
 * Date:2021/1/27 | Time:11:38
 * 一级目录 recruit manage | 二级目录 招聘管理
 */
@Controller
@RequestMapping("/manage")
public class ManageRecruitController extends BaseController implements ManageRecruitApi {
    private static final Logger logger = LoggerFactory.getLogger(ManageRecruitController.class);

    @Autowired
    private INurseryRecruitInfoSV nurseryRecruitInfoSV;

    @Autowired
    private LogAsyncComponent logAsyncComponent;

    @Autowired
    private INurseryRecruiterManagmentSV recruiterManagmentSV;

    @Autowired
    private IRecruitAndConsumerSV recruitAndConsumerSV;

    @Autowired
    private IConsumerResumeSV consumerResumeSV;

    private String NOWDATE_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    private String LOGO_CAOZUOTYPE = "audit";
    private String DOTHING_UPDATE = "2";
    private String DOTHING_DELETE = "1";

    /**
     * 更新招聘内容
     *
     * @param recruitBO 招聘内容
     * @return 提示信息
     */
    @RequestMapping(value = "/recruit/update", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public ResponseResult putRecruitInfo(RecruitBO recruitBO) {
        //初始化返回值
        ResponseResult responseResult = ResponseResult.FAIL();
        String employment = recruitBO.getEmployment();//职位名称
        String type = recruitBO.getType();//类型
        String workPlace = recruitBO.getWorkPlace();//地点
        Integer needNumber = Integer.parseInt(recruitBO.getNeedNumber());//人数
        String endTime = recruitBO.getEndTime();//结束时间
        String pay = recruitBO.getPay();//薪资
        String requireEduDB = recruitBO.getRequireEduDB();//学历要求
        String requireExperience = recruitBO.getRequireExperience();//工作经验
        String responsibility = recruitBO.getResponsibility();//职责描述
        String require = recruitBO.getRequire();//职位要求
        String treatment = recruitBO.getTreatment();//福利待遇
        logger.info("putRecruitInfo：RecruitBO==> " + recruitBO);
        //判断是否参数有误
        if (StringUtils.isEmpty(recruitBO.getId())) {
            logger.warn("参数不对，没有id值");
            responseResult.setCommonCode(RecruitCode.RECRUIT_PARAM_NONE);
            return responseResult;
        }
        try {
            RecruitmentDO recruitmentDO = new RecruitmentDO();
            recruitmentDO.setId(recruitBO.getId());
            recruitmentDO.setRecruittablename(employment);
            recruitmentDO.setClassify(type);
            recruitmentDO.setPlace(workPlace);
            recruitmentDO.setManNumbers(needNumber);
            recruitmentDO.setEndtime(endTime);
            recruitmentDO.setPay(pay);
            recruitmentDO.setRequireEduDB(requireEduDB);
            recruitmentDO.setRequireExperience(requireExperience);
            recruitmentDO.setResponsibility(responsibility);
            recruitmentDO.setJobrequirement(require);
            recruitmentDO.setTreatment(treatment);
            //4.2 更新后重新提交审核
            recruitmentDO.setIsActivate("no");
            logger.warn("putRecruitInfo: RecruitmentDO==>" + JSON.toJSONString(recruitmentDO));
            int i = nurseryRecruitInfoSV.updateRecruitInfo(recruitmentDO);
            //判断更新是否成功
            if (i > 0) {
                logger.error("putRecruitInfo： 更新成功,影响行数" + i);
                responseResult.setCommonCode(RecruitCode.RECRUIT_SQL_SUCCEED);
                return responseResult;
            }
        } catch (Exception e) {
            logger.info("putRecruitInfo： 更新失败");
            logger.error(e.getMessage());
            responseResult.setCommonCode(RecruitCode.RECRUIT_SQL_FAIL);
            return responseResult;
        }
        return responseResult;
    }

    /**
     * 发布招聘
     *
     * @param recruitmentDO
     * @return
     */
    @RequestMapping(value = "/postRecruitment", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public ResponseResult postRecruitInfo(RecruitmentDO recruitmentDO) {
        ResponseResult responseResult = ResponseResult.SUCCESS();
        String id = CommonUtil.getUUID();
        String authorId = recruitmentDO.getAuthorId();  //发布者id
        String startTime = recruitmentDO.getStarttime();
        String endTime = recruitmentDO.getEndtime();
        //封面内容
        String recruittablename = recruitmentDO.getRecruittablename();//标题
        String place = recruitmentDO.getPlace();//地点
        String pay = recruitmentDO.getPay();//薪资情况
        String requireEduDB = recruitmentDO.getRequireEduDB();//学历要求
        try {
            /*判断参数问题*/
            if (StringUtils.isEmpty(recruittablename) || StringUtils.isEmpty(place)
                    || StringUtils.isEmpty(pay) || StringUtils.isEmpty(requireEduDB)) {
                logger.warn("请输入正确的参数！！");
                responseResult.setCommonCode(RecruitCode.RECRUIT_PARAM_NONE);
                return responseResult;
            }
            if (StringUtils.isEmpty(id)) {
                logger.warn("获取id为空，服务器异常！！");
                responseResult.setCommonCode(RecruitCode.RECRUIT_GET_ID_ISNULL);
                return responseResult;
            }
            recruitmentDO.setId(id);
            /*判断是否过期 传过来的日期格式 yyyy-MM-dd HH:ss*/
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)
                    || !DateUtils.verifyOverDue(startTime, endTime)) {
                logger.warn("日期格式不正确，or开始时间和结束时间前后顺序颠倒！");
                responseResult.setCommonCode(RecruitCode.RECRUIT_Date_IS_WRONG);
                return responseResult;
            }
            recruitmentDO.setCutoff("no");//报名是否过期
            recruitmentDO.setEnrollFull("no");//报名人数已满
            if (StringUtils.isEmpty(authorId)) {
                authorId = getUserId();
                recruitmentDO.setAuthorId(authorId);
            }
            nurseryRecruitInfoSV.insertRecruitInfo(recruitmentDO);
            logger.info("招聘信息recruitmentDO " + JSON.toJSONString(recruitmentDO));
        } catch (Exception e) {
            logger.error("数据插入失败=>错误信息 : " + e.getMessage());
            responseResult.setCommonCode(RecruitCode.RECRUIT_SQL_Fail);
        }
        return responseResult;
    }

    /**
     * 下载简历
     * http://localhost:32227/manage/recruit/resume/download/1c3b9294497447d786d4cff019bbdc9b
     *
     * @param consumerId
     * @return
     */
    @RequestMapping(value = {"/recruit/resume/download/{consumerId}"})
    @ResponseBody
    @Override
    public JSONObject downloadResume(@PathVariable(name = "consumerId") String consumerId) {
        JSONObject responseResult = new JSONObject();
        responseResult.put("message", CommonCode.FAIL.message());
        try {
            Map<String, String> resultMap = consumerResumeSV.findResumeURLByConsumerId(consumerId);
            responseResult.put("code", resultMap.get("code"));
            if (resultMap.get("code").equals("10000")) {
                responseResult.put("resumeUrl", resultMap.get("url"));
                responseResult.put("message", CommonCode.SUCCESS.message());
            } else {
                responseResult.put("message", "你还没上传，先上传简历！");
            }
        } catch (SQLException throwables) {
            logger.error("数据库错误" + throwables.getSQLState());
        }
        return responseResult;
    }

    /**
     * 查询简历
     * /manage/recruit/resume/look/1c3b9294497447d786d4cff019bbdc9b
     *
     * @param consumerId
     * @return
     */
    @RequestMapping(value = {"/recruit/resume/look/{consumerId}"})
    @ResponseBody
    @Override
    public ResponseResult lookResume(@PathVariable(name = "consumerId") String consumerId) {
        ResponseResult responseResult = ResponseResult.FAIL();
        DomesticConsumerResumeDO consumerResume = null;
        try {
            consumerResume = consumerResumeSV.findResumeDOByConsuemrId(consumerId);
        } catch (SQLException throwables) {
            return responseResult;
        }
        if (!ObjectUtils.isEmpty(consumerResume)) {
            Map map = JSONObject.parseObject(JSONObject.toJSONString(consumerResume), Map.class);
            responseResult.setBean(map);
            responseResult.setCommonCode(CommonCode.SUCCESS);
        }
        return responseResult;
    }


    /**
     * 删除招聘信息
     *
     * @param recruitId 招聘id
     * @return
     */
    @RequestMapping(value = "/recruit/delete/{recruitId}", method = RequestMethod.GET)
    public String deleteRecruit(@PathVariable(value = "recruitId") String recruitId) {
        String userId = "";
        try {
            userId = getUserId();
            int i = nurseryRecruitInfoSV.deleteRecruitById(userId, recruitId);
            if (i > 0) {
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("classify", ManageRecruitController.class.getName() + ".deleteRecruit");
                resultMap.put("type", LOGO_CAOZUOTYPE);
                resultMap.put("date", DateUtils.getNowDate(NOWDATE_YYYYMMDDHHMMSS));
                resultMap.put("id", recruitId);
                resultMap.put("erId", userId);
                resultMap.put("dothing", DOTHING_DELETE);
                logAsyncComponent.logAnnounce(resultMap);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage() + throwables.getSQLState());
        }
        return "redirect:/manage/recruit/getRecruitManage";
    }

    /**
     * 管理员-审核反馈
     *
     * @param param id|yes,no|result
     */
    @RequestMapping(value = "/recruit/audit/result", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult auditRecruit(String param) {
        ResponseResult responseResult = ResponseResult.FAIL();
        String[] params = param.split("\\|");
        String authorID = null;
        try {
            authorID = getUserId();
        } catch (SQLException throwables) {
            return responseResult;
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("classify", ManageRecruitController.class.getName() + ".auditRecruit");
        resultMap.put("type", LOGO_CAOZUOTYPE);
        resultMap.put("date", DateUtils.getNowDate(NOWDATE_YYYYMMDDHHMMSS));
        resultMap.put("id", params[0]);
        resultMap.put("erId", authorID);
        resultMap.put("dothing", DOTHING_UPDATE);
        responseResult = nurseryRecruitInfoSV.updateRecruitSetAudit(param);
        logAsyncComponent.logAnnounce(resultMap);
        return responseResult;
    }

    //recruit/resume/interview/
    @RequestMapping(value = "/recruit/resume/interview/{consumerId}/{recruitId}", method = RequestMethod.GET)
    public String interviewRecruit(@PathVariable(value = "consumerId") String consumerId,
                                   @PathVariable(value = "recruitId") String recruitId) {
        try {
            recruitAndConsumerSV.interviewRecruit(consumerId, recruitId);
            boolean b = nurseryRecruitInfoSV.updateNum(recruitId);
            if (b){
                return "redirect:/manage/recruit/getRecruitInfo/"+recruitId;
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage() + throwables.getSQLState());
        }
        return "redirect:/manage/recruit/getRecruitInfo/"+recruitId;
    }

    private String getUserId() throws SQLException {
        String authorId = "";
        String authorName = "";
        //获取用户信息 UserDetails
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            authorName = authentication.getName();
            authorId = recruiterManagmentSV.getIdByName(authorName);
        }
        return authorId;
    }
}
