package com.nursery.nurserymanage2.controller.visit;

import com.nursery.api.iservice.INurseryAnnounceSV;
import com.nursery.api.iservice.INurseryRecruiterManagmentSV;
import com.nursery.api.iwebm.visit.VisitAnnunceApi;
import com.nursery.beans.NurseryAnnounceDO;
import com.nursery.common.web.BaseController;
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
import java.util.List;

/**
 * author:MeiShiQiang
 * 通告管理中心
 */
@Controller
public class VisitAnnunceController extends BaseController implements VisitAnnunceApi {

    private Logger logger = LoggerFactory.getLogger(VisitAnnunceController.class);

    @Autowired
    private INurseryAnnounceSV annunceManageSV;

    @Autowired
    private INurseryRecruiterManagmentSV recruiterManagmentSV;

    @RequestMapping(value = {"/manage/announcement.html"},method = RequestMethod.GET)
    @Override
    public ModelAndView visitAnnuncePage(ModelAndView modelAndView) {
        modelAndView.setViewName("annuncePage");
        try {
            List<NurseryAnnounceDO> announceDOS = annunceManageSV.selectAnnunces();
            modelAndView.addObject("announcedata",announceDOS);
        } catch (SQLException throwables) {
            logger.error("报错信息："+throwables.getMessage());
        }
        return modelAndView;
    }

    /**
     * @param modelAndView 返回参数
     * @return ModelandView
     */
    @RequestMapping(value = {"/manage/pullAnnouncement.html"},method = RequestMethod.GET)
    @Override
    public ModelAndView pullAnnuncePage(ModelAndView modelAndView) {
        modelAndView.setViewName("annuncePullPage");
        return modelAndView;
    }

    @RequestMapping(value = {"/manage/announce/detail/{erId}/{announceId}.html"},method = RequestMethod.GET)
    @ResponseBody
    @Override
    public ModelAndView getAnnuncePage( @PathVariable(value = "erId") String id,@PathVariable(value = "announceId") String announceId, ModelAndView modelAndView) {
        modelAndView.setViewName("annunceDetailPage");
        try {
            NurseryAnnounceDO announceById = annunceManageSV.getAnnounceById(announceId);
            modelAndView.addObject("announce",announceById);
        } catch (SQLException throwables) {
            logger.error("错误sql : "+throwables.getSQLState()+" 错误信息: "+throwables.getMessage());
        }
        return modelAndView;
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
