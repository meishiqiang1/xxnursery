package com.nursery.cmsweb.controller;

import com.nursery.api.iservice.IDomesticConsumerSV;
import com.nursery.api.iservice.IUserCenterSV;
import com.nursery.api.iweb.UserCenterApi;
import com.nursery.beans.DomesticConsumerDO;
import com.nursery.beans.UserInfo;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.QueryResponseResult;
import com.nursery.common.model.response.ResponseResult;
import com.nursery.common.web.BaseController;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心业务
 * 查询个人信息   *
 * 更改密码操作   *
 * 更改用户名
 * 更改出生日期
 */
@Controller
@RequestMapping("/consumer")
public class UserCenterController extends BaseController implements UserCenterApi {

    private Logger logger = LoggerFactory.getLogger(UserCenterController.class);

    @Autowired
    private IDomesticConsumerSV domesticConsumerSV;
    @Autowired
    private IUserCenterSV userCenterSV;

    //用户中心
    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    @Override
    public ModelAndView visitUserInfoPage(ModelAndView modelAndView) {
        //初始化返回内容
        modelAndView.setViewName("500");
        modelAndView.addObject("message", CommonCode.SERVER_ERROR.message());
        //定义返回值内容
        QueryResponseResult data = new QueryResponseResult(CommonCode.FAIL, null);
        //获取用户名称
        String consumerName = getConsumerName();
        QueryResponseResult queryResponseResult = userCenterSV.getUserCenter(consumerName);
        if (queryResponseResult.getCode()!=10000){
            return modelAndView;
        }
        data = queryResponseResult;
        modelAndView.addObject("data", data);
        modelAndView.setViewName("userInfo1");
        return modelAndView;
    }

    private String getConsumerName(){
        String consumerName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            consumerName = authentication.getName();
        }
        return consumerName;
    }

    //用户编辑
    @RequestMapping(value = "/personalEdit", method = RequestMethod.GET)
    @Override
    public ModelAndView visitUserEditByID(ModelAndView modelAndView) {
        modelAndView.setViewName("500");
        modelAndView.addObject("message",CommonCode.SERVER_ERROR.message());
        QueryResponseResult data = new QueryResponseResult(CommonCode.FAIL, null);
        String consumerName = "";
        //获取用户信息 UserDetails
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            consumerName = authentication.getName();
        }
        QueryResponseResult queryResponseResult = userCenterSV.getUserCenter(consumerName);
        if (queryResponseResult.getCode()!=10000){
            return modelAndView;
        }
        data = queryResponseResult;
        data.setCommonCode(CommonCode.SUCCESS);
        modelAndView.addObject("data", data);
        modelAndView.setViewName("userEdit");
        return modelAndView;
    }

    //更新数据
    @RequestMapping(value = "/personal/pullUser",method = RequestMethod.POST)
    @Override
    public String pullUser(DomesticConsumerDO consumerDO) {
        //获取用户信息 UserDetails
        String consumerName = "";
        String consumerId = "";
        ResponseResult responseResult = ResponseResult.FAIL();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            consumerName = authentication.getName();
        }
        if (StringUtils.isNotBlank(consumerName)){
            consumerId = domesticConsumerSV.selectConsumerIdByConsumerNickName(consumerName);
            consumerDO.setConsumerID(consumerId);
            responseResult =  userCenterSV.pullUser(consumerDO);
            if (responseResult.getCode()==10000){
                return "redirect:/consumer/personalEdit/";
            }
        }
        request.setAttribute("message",responseResult.getMessage());
        return "500";
    }

    //更新头像
    @RequestMapping(value = "/personal/pullImage",method = RequestMethod.POST)
    @Override
    public void pullImage(@RequestParam(name = "base") String base64) {
        //获取用户信息 UserDetails
        String consumerName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            consumerName = authentication.getName();
        }
        userCenterSV.pullImage(request.getContextPath(),consumerName,base64);
    }



    /**
     * generalContent
     * personal/pullUser
     * @param consumerID
     * @return
     */
    @GetMapping("/generalContent")
    @Override
    public ResponseResult generalContent(String consumerID) {
        ResponseResult responseResult = ResponseResult.SUCCESS();
        Map<String, String> returnMap = new HashMap<>();
        String imageUrl = "";
        //1. 判断是否已经登录
        //2. 返回的参数有
        DomesticConsumerDO consumerDO = null;
        try {
            consumerDO = domesticConsumerSV.findByconsumerID(consumerID);
            if (consumerDO != null) {
                UserInfo userInfo = new UserInfo();
                userInfo.setAge(consumerDO.getConsumerAge());
                userInfo.setEducationBg(consumerDO.getConsumerEducationBg());
                userInfo.setStatus(consumerDO.getConsumerStatus());//身份
                returnMap.put("userName", consumerDO.getConsumerName());
                returnMap.put("consumerCellPhone", consumerDO.getConsumerCellPhone());
                returnMap.put("consumerEamil", consumerDO.getConsumerEmail());
                returnMap.put("consumerAddress", consumerDO.getConsumerAddress());
                returnMap.put("consumerNickname", consumerDO.getConsumerNickname());
                returnMap.put("consumerBirthday", consumerDO.getConsumerBirthday());
                returnMap.put("consumerJoinDay", consumerDO.getConsumerJoinDay());
                returnMap.put("userInfo", userInfo.toString());
                if (!imageUrl.equals(consumerDO.getConsumerURL())) {
                    imageUrl = consumerDO.getConsumerURL();
                } else {
                    imageUrl = "默认地址-头像-https://img.bosszhipin.com/boss/avatar/avatar_14.png";
                }
                Integer resumeflag = consumerDO.getResumeISNOT();
                if (consumerDO.getResumeISNOT() == 0) {//没有简历,,提示上传简历
                    //保存到jvm缓存中,   id + 0(RESUMERZORE),

                }
                returnMap.put("resumeflag", resumeflag.toString());//提示 用户 是否已经上传简历,判断后
                returnMap.put("imageUrl", imageUrl);
            } else {
                return new ResponseResult(CommonCode.UNAUTHORISE);
            }
        } catch (Exception e) {
            return new ResponseResult(CommonCode.SELECTISFAIL);
        }
        responseResult.setBean(returnMap);
        return responseResult;
    }

    /**
     * 比对密码正确
     *
     * @param consumerID 用户id
     * @param password   密码
     */
    @PostMapping("/consultingcode")
    @Override
    public ResponseResult selectPassword(String consumerID, String password) {
        if ((password != null && !"".equals(password)) && (consumerID != null && !"".equals(consumerID))) {
            if (domesticConsumerSV.selectPassword(consumerID, password)) {
                //存入标识  PASSWORDSUCCESS

                //返回
                return ResponseResult.SUCCESS();
            }
        }
        return ResponseResult.FAIL();
    }

    //更改用户密码
    @PostMapping("/changecode")
    @Override
    public ResponseResult updatePassword(String consumerID, String password) {
        ResponseResult responseResult = new ResponseResult(CommonCode.FAIL);
        //获取标识 PASSWORDSUCCESS从redis中获取

        //如果存在则进行密码修改
        if (domesticConsumerSV.updatePassword(consumerID, password)) {
            //1.更新redis和jvm缓存,

            //2.返回前端提示信息
            responseResult.setCode(CommonCode.SUCCESS.code());
            responseResult.setMessage(CommonCode.SUCCESS.message());
            responseResult.setSuccess(CommonCode.SUCCESS.success());
        }
        return responseResult;
    }

}
