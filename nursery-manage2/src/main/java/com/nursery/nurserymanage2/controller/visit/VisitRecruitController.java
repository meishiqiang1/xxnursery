package com.nursery.nurserymanage2.controller.visit;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nursery.api.iservice.INurseryRecruitInfoSV;
import com.nursery.api.iservice.INurseryRecruiterManagmentSV;
import com.nursery.api.iservice.IRecruitAndConsumerSV;
import com.nursery.api.iwebm.visit.VisitRecruitApi;
import com.nursery.beans.RecruitAndConsumerDO;
import com.nursery.beans.RecruiterManagmentDO;
import com.nursery.beans.RecruitmentDO;
import com.nursery.beans.code.RecruitCode;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.QueryResponseResult;
import com.nursery.common.model.response.QueryResult;
import com.nursery.common.web.BaseController;
import com.nursery.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

/**
 * author:MeiShiQiang
 * Date:2021/2/3 | Time:17:37
 * 访问招聘
 */
@Controller
public class VisitRecruitController extends BaseController implements VisitRecruitApi {
    private static final Logger logger = LoggerFactory.getLogger(VisitRecruitController.class);

    @Autowired
    private INurseryRecruitInfoSV nurseryRecruitInfoSV;

    @Autowired
    private INurseryRecruiterManagmentSV recruiterManagment;

    @Autowired
    private IRecruitAndConsumerSV recruitAndConsumerSV;

    @Autowired
    private INurseryRecruiterManagmentSV recruiterManagmentSV;

    //定义一个测试的id
    private String RECRUIT_ER_ID = "1";
    private String RECRUIT_AUDIT_ISACTIVATE = "no";


    // 获取所有的招聘信息
    @GetMapping("/manage/recruit/getRecruitManages")
    @ResponseBody
    @Override
    public ModelAndView getRecruitManages() {
        Map<String, String> returnMap = new HashMap<>();
        QueryResult<RecruitmentDO> queryResult = new QueryResult<RecruitmentDO>();
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL, queryResult);
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<RecruitmentDO> recruitmentDOList = null;
            recruitmentDOList = nurseryRecruitInfoSV.selectRecruitinfoByerid();
            if (recruitmentDOList != null && !recruitmentDOList.isEmpty()) {
                queryResult.setList(recruitmentDOList);
                queryResult.setTotal(recruitmentDOList.size());
            } else {

            }
        } catch (Exception e) {

        }
        queryResponseResult.setCommonCode(CommonCode.SUCCESS);
        modelAndView.addObject("data", queryResponseResult);
        modelAndView.setViewName("recruitManagePages");
        logger.info(JSON.toJSONString(queryResponseResult));
        return modelAndView;
    }

    /**
     * 获取当前用户发布的招聘信息
     *
     * @return
     */
    @GetMapping("/manage/recruit/getRecruitManage")
    @ResponseBody
    @Override
    public ModelAndView getRecruitManage() {
        QueryResult<RecruitmentDO> queryResult = new QueryResult<RecruitmentDO>();
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL, queryResult);
        ModelAndView modelAndView = new ModelAndView();
        try {
            String userId = getUserId();
            if (!StringUtils.isEmpty(userId)) {
                List<RecruitmentDO> recruitmentDOList = nurseryRecruitInfoSV.selectRecruitinfoByerid(userId);
                if (recruitmentDOList != null && !recruitmentDOList.isEmpty()) {
                    queryResult.setList(recruitmentDOList);
                    queryResult.setTotal(recruitmentDOList.size());
                } else {
                    queryResponseResult.setCommonCode(RecruitCode.RECRUIT_IS_NOT_EXISTS);
                }
            } else {
                logger.error(RecruitCode.SERVICE_USERID_NOT_EXISTX.message());
                queryResponseResult.setCommonCode(RecruitCode.SERVICE_USERID_NOT_EXISTX);
            }
        } catch (Exception e) {
            logger.error(RecruitCode.RECRUIT_SQL_FAIL.message());
            queryResponseResult.setCommonCode(RecruitCode.RECRUIT_SQL_FAIL);
        }
        queryResponseResult.setCommonCode(CommonCode.SUCCESS);
        modelAndView.addObject("data", queryResponseResult);
        modelAndView.setViewName("recruitManagePage");
        logger.info(JSON.toJSONString(queryResponseResult));
        return modelAndView;
    }

    /**
     * 详细信息
     *
     * @param recruitid 招聘内容id
     * @return 招聘详情页面、招聘更新页面
     */
    @GetMapping("/manage/recruit/getRecruitInfo/{recruitid}")
    @ResponseBody
    @Override
    public ModelAndView getRecruitInfoByrecruitid(@PathVariable("recruitid") String recruitid) {
        Map<String, Object> returnMap = new HashMap<>();
        String id = "";
        String degreeCompletion = "";//完成程度
        String classify = "";
        String author = "";
        String experienceCode = "";
        String requireEduDBCode = "";
        int recruitnumbers = 0; //招聘人数
        int applynum = 0;  // 报名人数
        List<RecruitAndConsumerDO> recruitAndConsumerDOS = new ArrayList<>();
        //定义返回值
        ModelAndView modelAndView = new ModelAndView();
        try {
            //sv层
            RecruitmentDO recruitmentDO = nurseryRecruitInfoSV.selectRecruitInfoByrecruitid(recruitid);
            returnMap.put("title", recruitmentDO.getRecruittablename());//标题
            returnMap.put("pay", recruitmentDO.getPay());//薪资
            returnMap.put("id", recruitmentDO.getId());//薪资
            RecruiterManagmentDO recruiterManagmentDO = new RecruiterManagmentDO();
            recruiterManagmentDO.setId(recruitmentDO.getAuthorId());
            try {
                author = recruiterManagment.getRealName(recruiterManagmentDO);
            } catch (SQLException e) {
                logger.error("查询出错");
            }
            returnMap.put("authorEr", author);//发布人  88
            classify = recruitmentDO.getClassify();
            if (!StringUtils.isEmpty(classify)) {
                classify = classify.trim();
                String[] types = classify.split(",");
                returnMap.put("types", types);//类型   88
            }
            returnMap.put("startDate", recruitmentDO.getStarttime());//创建时间
            returnMap.put("endDate", recruitmentDO.getStarttime());//结束时间
            returnMap.put("number", recruitmentDO.getManNumbers());//招聘人数
            recruitnumbers = recruitmentDO.getManNumbers();
            applynum = recruitmentDO.getApplynum();
            try {
                if (applynum != 0) {
                    // 创建一个数值格式化对象
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(2);
                    degreeCompletion = numberFormat.format((float) recruitnumbers / (float) applynum * 100);
                    returnMap.put("degreeCompletion", degreeCompletion);//完成程度
                } else {
                    returnMap.put("degreeCompletion", 0);//完成程度
                }
            } catch (ArithmeticException e) {
                returnMap.put("degreeCompletion", 0);//完成程度
            }
            experienceCode = recruitmentDO.getRequireExperience();
            String experience = CommonUtil.getExperience(experienceCode);
            returnMap.put("experience", experience);//工作经验
            returnMap.put("place", recruitmentDO.getPlace());//工作地址
            requireEduDBCode = recruitmentDO.getRequireEduDB();
            String requureEdu = CommonUtil.getRequureEdu(requireEduDBCode);
            returnMap.put("requireEduDB", requureEdu);//学历要求
            returnMap.put("responsibility", recruitmentDO.getResponsibility());//职责描述
            returnMap.put("treatment", recruitmentDO.getTreatment());//待遇
            returnMap.put("require", recruitmentDO.getJobrequirement());//职位要求
            id = recruitmentDO.getId();
            try {
                recruitAndConsumerDOS = recruitAndConsumerSV.getDOsByRecruitId(id);
                returnMap.put("recruitAndConsumerDOS", recruitAndConsumerDOS);
            } catch (SQLException throwables) {
                logger.error(throwables.getSQLState());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info(JSON.toJSONString(returnMap));
        modelAndView.addObject("data", returnMap);
        modelAndView.setViewName("recruitInfoPage");
        return modelAndView;
    }


    /**
     * 根据内容id 获取招聘的详细信息
     *
     * @param recruitid 招聘内容id
     * @return 招聘详情页面、招聘更新页面
     */
    @GetMapping("/manage/recruit/modify/page/{recruitid}")
    @ResponseBody
    @Override
    public ModelAndView getRecruitInfoByid(@PathVariable("recruitid") String recruitid) {
        QueryResult<RecruitmentDO> queryResult = new QueryResult<RecruitmentDO>();
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL, queryResult);
        ModelAndView modelAndView = new ModelAndView();
        try {
            RecruitmentDO recruitmentDO = nurseryRecruitInfoSV.selectRecruitInfoByrecruitid(recruitid);
            if (!ObjectUtils.isEmpty(recruitmentDO)) {
                queryResult.setObject(recruitmentDO);
                queryResponseResult.setCommonCode(CommonCode.SUCCESS);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        modelAndView.addObject("data", queryResponseResult);
        modelAndView.setViewName("recruitModifyPage");
        logger.info("getRecruitInfoByid" + JSON.toJSONString(queryResponseResult));
        return modelAndView;
    }


    @GetMapping("/manage/recruit/pull")
    @Override
    public ModelAndView visitPullRecruitPage(ModelAndView modelAndView) {
        modelAndView.setViewName("pullRecruitPage");
        return modelAndView;
    }

    /**
     * 访问招聘审核页面
     * 更具ISACTIVATE字段判断是否审核过。
     *
     * @param modelAndView 视图和数据
     * @return modelAndView
     */
    @RequestMapping(value = {"manage/recruit/audit"}, method = RequestMethod.GET)
    public ModelAndView visitAuditPage(ModelAndView modelAndView) {
        //定义返回值
        QueryResult<RecruitmentDO> queryResult = new QueryResult<RecruitmentDO>();
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL, queryResult);
        try {
            List<RecruitmentDO> recruits = nurseryRecruitInfoSV.selectRecruitByIsActivate(RECRUIT_AUDIT_ISACTIVATE);
            if (recruits != null && !recruits.isEmpty()) {
                queryResult.setList(recruits);
                queryResult.setTotal(recruits.size());
                queryResponseResult.setCommonCode(CommonCode.SUCCESS);
            } else {
                queryResponseResult.setCommonCode(RecruitCode.ISACTIVATE_IS_NULL);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getSQLState());
            queryResponseResult.setCommonCode(RecruitCode.RECRUIT_SQL_FAIL);
        }
        modelAndView.addObject("data", queryResponseResult);
        modelAndView.setViewName("recruitAuditPage");
        return modelAndView;
    }

    @RequestMapping(value = "/manage/recruit/resume/pass/{consumerId}/{recruitId}")
    public String resumePass(@PathVariable(name = "consumerId") String consumerId,
                             @PathVariable(name = "recruitId") String recruitId) {
        try {
            recruitAndConsumerSV.resumePass(consumerId,recruitId);
            return "redirect:/manage/recruit/getRecruitInfo/"+recruitId;
        } catch (SQLException throwables) {
            return "500";
        }
    }


    @RequestMapping(value = "/manage/recruit/resume/nopass/{consumerId}/{recruitId}")
    public String resumeNoPass(@PathVariable(name = "consumerId") String consumerId,
                               @PathVariable(name = "recruitId") String recruitId) {
        try {
            recruitAndConsumerSV.resumeNoPass(consumerId,recruitId);
            return "redirect:/manage/recruit/getRecruitInfo/"+recruitId;
        } catch (SQLException throwables) {
            return "500";
        }
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
