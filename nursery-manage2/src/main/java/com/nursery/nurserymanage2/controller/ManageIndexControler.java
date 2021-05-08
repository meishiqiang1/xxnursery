package com.nursery.nurserymanage2.controller;

import com.github.pagehelper.PageHelper;
import com.nursery.api.iservice.INurseryRecruitInfoSV;
import com.nursery.beans.RecruitmentDO;
import com.nursery.common.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.List;

/**
 * author:MeiShiQiang
 * Date:2021/1/26
 * Time:17:31 首页
 */
@Controller
public class ManageIndexControler extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ManageIndexControler.class);
    @Autowired
    private INurseryRecruitInfoSV recruitInfoSV;

    @GetMapping(value = {"/index","/index.html","/"})
    public String index(){
        PageHelper.startPage(1, 4);
        List<RecruitmentDO> recruitmentDOList = null;
        try {
            recruitmentDOList = recruitInfoSV.getRecruitByDateDesc();
            request.setAttribute("recruits",recruitmentDOList);
//            PageInfo<RecruitmentDO> pageInfo = new PageInfo<>(recruitmentDOList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "index";
    }
}
