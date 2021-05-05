package com.nursery.cmsweb.controller;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nursery.api.iservice.INurseryAnnounceSV;
import com.nursery.api.iweb.InformationApi;
import com.nursery.beans.NurseryAnnounceDO;
import com.nursery.beans.NurseryAnnounceDetailDO;
import com.nursery.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

/**
 * author:MeiShiQiang
 * Date:2021/2/25 | Time:14:11
 * 对应  information.html页面
 */
@RestController
@RequestMapping("/information")
public class InformationController extends BaseController implements InformationApi {
    private Logger logger = LoggerFactory.getLogger(InformationController.class);
    private int SQL_SIZE_LEN_MIN = 4;
    @Autowired
    private INurseryAnnounceSV announceSV;

    @RequestMapping(method = RequestMethod.GET)
    @Override
    public ModelAndView visitInformation(ModelAndView modelAndView) {
        /*获取公告 最新/最热/推荐*/
        List<NurseryAnnounceDO> nhrData = announceSV.getNewAndHotAndRecommendAnnounceDO();
        if (nhrData == null || nhrData.size() < 3) {
            logger.info("长度应为3，提示管理员进行测试");
        }
        List<NurseryAnnounceDO> announces = announceSV.getRandomAnnounceeList();
        if (announces == null) {
            logger.info("announces 返回值为空");
        }
        modelAndView.addObject("nhrData", nhrData);
        modelAndView.addObject("announces", announces);
        modelAndView.setViewName("information");
        return modelAndView;
    }

    /*获取公告 最新*/
    @RequestMapping(value = "/zuixin/{page}.html", method = RequestMethod.GET)
    @Override
    public ModelAndView visitNewInformation(@PathVariable(value = "page", required = true) String page,
                                            ModelAndView modelAndView) {
        int ipage = 0;
        if (!StringUtils.isEmpty(page)) ipage = Integer.parseInt(page);
        int sqlSize = ipage*SQL_SIZE_LEN_MIN;
        PageHelper.startPage(1, sqlSize);
        List<NurseryAnnounceDO> announceDOList = null;
        try {
            announceDOList = announceSV.getNewAnnounceDO();
            PageInfo<NurseryAnnounceDO> pageInfo = new PageInfo<>(announceDOList);
            modelAndView.addObject("pageInfo", pageInfo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        modelAndView.setViewName("information");
        return modelAndView;
    }

    @RequestMapping(value = "/zuixinMore/{page}.html", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public PageInfo<NurseryAnnounceDO> visitNewInformation(@PathVariable("page") String page) {
        int ipage = 0;
        if (!StringUtils.isEmpty(page)) ipage = Integer.parseInt(page);
        PageHelper.startPage(ipage, SQL_SIZE_LEN_MIN);
        List<NurseryAnnounceDO> announceDOList = null;
        PageInfo<NurseryAnnounceDO> pageInfo = null;
        try {
            announceDOList = announceSV.getNewAnnounceDO();
            pageInfo = new PageInfo<>(announceDOList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageInfo;
    }

    /*获取公告 最热*/
    @RequestMapping(value = "/remen/{page}.html", method = RequestMethod.GET)
    @Override
    public ModelAndView visitHotInformation(@PathVariable(value = "page", required = true) String page, ModelAndView modelAndView) {
        int ipage = 1;
        if (!StringUtils.isEmpty(page)) ipage = Integer.parseInt(page);
        int sqlSize = ipage*SQL_SIZE_LEN_MIN;
        PageHelper.startPage(1, sqlSize);
        List<NurseryAnnounceDO> announceDOList = null;
        try {
            announceDOList = announceSV.getHotAnnounceDO();
            PageInfo<NurseryAnnounceDO> pageInfo = new PageInfo<>(announceDOList);
            modelAndView.addObject("pageInfo", pageInfo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        modelAndView.setViewName("information");
        return modelAndView;
    }


    @RequestMapping(value = "/remenMore/{page}.html", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public PageInfo<NurseryAnnounceDO> visitHotInformation(@PathVariable("page") String page) {
        int ipage = 0;
        if (!StringUtils.isEmpty(page)) ipage = Integer.parseInt(page);
        PageHelper.startPage(ipage, SQL_SIZE_LEN_MIN);
        List<NurseryAnnounceDO> announceDOList = null;
        PageInfo<NurseryAnnounceDO> pageInfo = null;
        try {
            announceDOList = announceSV.getHotAnnounceDO();
            pageInfo = new PageInfo<>(announceDOList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pageInfo;
    }

    @RequestMapping(value = "/tuijian.html", method = RequestMethod.GET)
    @Override
    public ModelAndView visitRecommendInformation(ModelAndView modelAndView) {
        /*获取公告 推荐*/
        List<NurseryAnnounceDO> announceDOList = null;
        try {
            announceDOList = announceSV.getRecommendAnnounceDO();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        modelAndView.addObject("announces", announceDOList);
        modelAndView.setViewName("information");
        return modelAndView;
    }

    //http://localhost:32226/information/djxq/09513254fcf5460b92b2f3ea4ce07fdf.html
    @RequestMapping(value = "/djxq/{id}.html", method = RequestMethod.GET)
    @Override
    public ModelAndView visitOneInformation(@PathVariable(value = "id", required = true) String id, ModelAndView modelAndView) {
        NurseryAnnounceDetailDO announceDetail = null;
        try {
            announceDetail = announceSV.getannounceDetailById(id);
        } catch (SQLException throwables) {
            String sqlState = throwables.getSQLState();
            logger.error("错误sql: " + sqlState);
        }
        if (announceDetail==null){
            String referer = request.getHeader("Referer");
            modelAndView.addObject("referer_URL",referer);
            modelAndView.setViewName("404");
            return modelAndView;
        }
        modelAndView.addObject("announceDetail", announceDetail);
        modelAndView.setViewName("informationDetail");
        return modelAndView;
    }

}
