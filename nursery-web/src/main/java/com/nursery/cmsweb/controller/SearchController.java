package com.nursery.cmsweb.controller;

import com.alibaba.fastjson.JSON;
import com.nursery.api.iservice.INurseryRecruitInfoSV;
import com.nursery.beans.DBDataParam;
import com.nursery.beans.RecruitmentDO;
import com.nursery.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

/**
 * author:MeiShiQiang
 * Date:2021/3/27 | Time:17:20
 */
@Controller
public class SearchController extends BaseController {

    @Autowired
    private INurseryRecruitInfoSV nurseryRecruitInfoSV;

    private Logger logger = LoggerFactory.getLogger(SearchController.class);

    /*@RequestMapping(value = {"/search"})
    public ModelAndView search(@RequestParam(value = "sign") String signParam, ModelAndView modelAndView) {
        try {
            List<RecruitmentDO> recruitmentDOList = nurseryRecruitInfoSV.selectRecruitInfoByParams(signParam);
            modelAndView.addObject("recommendData", recruitmentDOList);
            modelAndView.setViewName("job_detail");
            logger.info(JSON.toJSONString(recruitmentDOList));
        } catch (SQLException throwables) {
            modelAndView.setViewName("");
        }
        return modelAndView;
    }*/


    /**
     * 搜索
     * @param search
     * @param type
     * @param placeId
     * @param payId
     * @param edcId
     * @param conditionId
     * @param putTimeId
     * @param modelAndView
     */
    @RequestMapping(value = {"/search"})
    public ModelAndView search1(@RequestParam(value = "search",required = false) String search,
                                @RequestParam(value = "type",required = false) String type,
                                @RequestParam(value = "placeId",required = false) String placeId,
                                @RequestParam(value = "payId",required = false) String payId,
                                @RequestParam(value = "edcId",required = false) String edcId,
                                @RequestParam(value = "conditionId",required = false) String conditionId,
                                @RequestParam(value = "putTimeId",required = false) String putTimeId,
                                ModelAndView modelAndView) {
        DBDataParam dataParam = new DBDataParam();
        dataParam.setSearch(search);
        dataParam.setType(type);
        dataParam.setPlaceId(placeId);
        dataParam.setPayId(payId);
        dataParam.setEdcId(edcId);
        dataParam.setConditionId(conditionId);
        dataParam.setPutTimeId(putTimeId);

        try {
            List<RecruitmentDO> recruitmentDOList = nurseryRecruitInfoSV.selectRecruitInfoByParams(dataParam);
            modelAndView.addObject("recommendData", recruitmentDOList);
            modelAndView.setViewName("job_detail");
            logger.info(JSON.toJSONString(recruitmentDOList));
        } catch (SQLException throwables) {
            modelAndView.setViewName("");
        }
        return modelAndView;
    }


}
