package com.nursery.cmsweb.controller;

import com.alibaba.fastjson.JSONObject;
import com.nursery.api.iservice.IConsumerResumeSV;
import com.nursery.api.iservice.IDomesticConsumerSV;
import com.nursery.api.iweb.ResumeApi;
import com.nursery.beans.DomesticConsumerDO;
import com.nursery.beans.DomesticConsumerResumeDO;
import com.nursery.common.model.CommonAttrs;
import com.nursery.common.model.response.QueryResponseResult;
import com.nursery.common.web.BaseController;
import com.nursery.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;

/**
 * 简历上传下载
 */

@Controller
public class ConsumerResumeController extends BaseController implements ResumeApi {

    private Logger logger = LoggerFactory.getLogger(ConsumerResumeController.class);
    @Autowired
    private IConsumerResumeSV consumerResumeSV;
    @Autowired
    private IDomesticConsumerSV consumerSV;

    //访问简历页面
    @RequestMapping(value = "/consumer/resume/", method = RequestMethod.GET)
    @Override
    public String visitResume() {
        String name = "";//用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            name = authentication.getName();
        }
        String consumerId = "";
        if (!StringUtils.isEmpty(name)) {
            try {
                consumerId = consumerSV.selectConsumerIdByConsumerNickName(name);
            } catch (NullPointerException e) {
                logger.error("没有该用户名");
                return "500";
            }
            try {
                DomesticConsumerDO consumerDO = consumerSV.selectConsumerResumeByConsumerID(consumerId);
                request.setAttribute("consumer", consumerDO);
            } catch (SQLException e) {
                logger.error("数据库查询异常/语句出错");
                return "500";
            }
        }else {
            logger.error("未登录，请先登录后进行操作");
            return "login";
        }
        return "resume";
    }

    /**
     * 上传简历
     * 1. 比对简历信息,是否符合要求
     * 2. 判断文件夹是否存在,下载到本地,不存在就创建mkdirs
     * 3. 下载成功,则返回url信息,并将简历信息存入到数据库中.
     *
     * @param file 简历信息
     * @return 固定返回参数信息
     */
    @RequestMapping(value = {"/consumer/resume/upload"})
    @ResponseBody
    @Override
    public JSONObject uploadResume(@RequestParam(value = "resumeFile", required = true) MultipartFile file) {
        JSONObject responseResult = new JSONObject();
        String consumerId = "";     //用户id
        String consumerName = "";
        String path = "";           //文件的真实路径
        String realFileName = "";   //简历名称
        String suffix = "";         //简历后缀/类型
        String sqlFileUrl = "";     //简历url地址
        String resumeId = "";       //简历id
        long size = 0;              //文件大小
        String fileName = "";       //随机生成的文件名称
        File targetFile = null;
        //获取存储的用户信息 UserDetails
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            consumerName = authentication.getName();
        }
        if (!StringUtils.isEmpty(consumerName )) {
            try {
                consumerId = consumerSV.selectConsumerIdByConsumerNickName(consumerName);
            }catch (Exception e){
                //定义返回 500 服务器出错
            }
        }
        try {
            realFileName = file.getOriginalFilename();//获取文件名称
            suffix = realFileName.substring(realFileName.lastIndexOf("."));//获取后缀
            size = file.getSize();
            if (!CommonAttrs.WORD_TYPE.contains(suffix)) {
                responseResult.put("success", 0);//格式不正确
                responseResult.put("message", "简历格式不正确：{doc, docx}");
                return responseResult;
            }
            if (size > CommonAttrs.IMG_MAX_SIZE) {
                responseResult.put("success", 0);//文件过大
                responseResult.put("message", "最大长度为!2MB");
                return responseResult;
            }
            resumeId = CommonUtil.getUUID() + CommonUtil.getRandomNum(100, 200);
            fileName = resumeId + suffix;
            //获取真实路径
            path = ResourceUtils.getURL("xxnursery/").getPath() + "word/upload";
            String realPath = path.replace('/', '\\').substring(1, path.length());
            targetFile = new File(realPath, fileName);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            try {
                file.transferTo(targetFile);//保存
            } catch (Exception e) {
                responseResult.put("success", 0);
                responseResult.put("message", "上传失败 !");
                return responseResult;
            }
        } catch (Exception e) {
            responseResult.put("success", 0);
            responseResult.put("message", "上传失败 !");
            return responseResult;
        }
        //如果上传成功,则将该地址返回,并存入数据库中
        sqlFileUrl = request.getContextPath() + "/upload/word/" + fileName;
        //数据库操作
        try {
            //更新操作，判断是否
            String consumerResumeId = consumerSV.selectResumeIdByConsumerID(consumerId);
            if (!StringUtils.isEmpty(consumerResumeId)) {
                //如果存在就删除简历
                consumerResumeSV.delectByid(consumerResumeId);
            }
            DomesticConsumerResumeDO consumerResumeDO = new DomesticConsumerResumeDO();
            consumerResumeDO.setId(resumeId);
            consumerResumeDO.setName(realFileName);
            consumerResumeDO.setType(suffix);
            consumerResumeDO.setUrl(sqlFileUrl);
            consumerResumeDO.setSize(size + "");
            //保存信息
            consumerResumeSV.insertResume(consumerResumeDO);
            DomesticConsumerDO consumerDO = new DomesticConsumerDO();
            consumerDO.setConsumerID(consumerId);
            consumerDO.setResumeISNOT(1);
            consumerDO.setResumeId(resumeId);
            consumerSV.updateConsumerResume(consumerDO);
        } catch (Exception e) {
            responseResult.put("success", 0);
            responseResult.put("message", "上传失败!");
            //删除文件;
            targetFile.delete();
            return responseResult;
        }
        responseResult.put("url", sqlFileUrl);
        responseResult.put("success", 1);
        responseResult.put("message", "上传成功!");
        return responseResult;
    }

    /**
     * 删除简历
     * @return
     */
    @RequestMapping(value = "/consumer/resume/delete/{resumeId}",method = RequestMethod.GET)
    @Override
    public String deleteResume(@PathVariable String resumeId) {
        //1.删除管理信息
        //如果存在就删除简历
        consumerResumeSV.delectByid(resumeId);
        //2. 更改简历上传状态
        String consumerName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            consumerName = authentication.getName();
        }
        if (!StringUtils.isEmpty(consumerName )) {
            try {
                DomesticConsumerDO consumerDO = new DomesticConsumerDO();
                consumerDO.setConsumerNickname(consumerName);
                consumerDO.setResumeISNOT(0);
                consumerSV.updateResumeISNOT(consumerDO);
            }catch (SQLException e){
                logger.warn("更新失败"+e.getSQLState());
            }
        }
        return "redirect:/consumer/resume/";
    }

    @Override
    public QueryResponseResult resumeUpdateUpload() {
        return null;
    }

    @Override
    public QueryResponseResult resumeOnlineReading() {
        return null;
    }


}
