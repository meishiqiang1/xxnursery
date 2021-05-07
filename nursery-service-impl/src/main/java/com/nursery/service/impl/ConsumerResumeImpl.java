package com.nursery.service.impl;

import com.nursery.api.iservice.IConsumerResumeSV;
import com.nursery.beans.DomesticConsumerResumeDO;
import com.nursery.beans.RecruitAndConsumerDO;
import com.nursery.common.model.response.CommonCode;
import com.nursery.dao.DomesticConsumerMapper;
import com.nursery.dao.DomesticConsumerResumeMapper;
import com.nursery.dao.RecruitAndConsumerMapper;
import com.nursery.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * author:MeiShiQiang
 * Date:2021/2/26 | Time:11:35
 */
@Service
public class ConsumerResumeImpl implements IConsumerResumeSV {

    @Autowired
    @SuppressWarnings("all")
    private DomesticConsumerResumeMapper resumeMapper;
    @Autowired
    @SuppressWarnings("all")
    private DomesticConsumerMapper domesticConsumerMapper;

    @Autowired
    @SuppressWarnings("all")
    private RecruitAndConsumerMapper recruitAndConsumerMapper;


    @Override
    public void insertResume(DomesticConsumerResumeDO consumerResumeDO) throws SQLException {
        resumeMapper.insertResume(consumerResumeDO);
    }

    @Override
    public void delectByid(String consumerResumeId) {
        resumeMapper.delectById(consumerResumeId);
    }

    @Override
    public DomesticConsumerResumeDO findResumeDOByConsuemrName(String name) throws NullPointerException, SQLException {
        String resumeId = domesticConsumerMapper.selectResumeIdByConsumerName(name);
        DomesticConsumerResumeDO resume = resumeMapper.selectConsumerResumeByResumeId(resumeId);
        String type = resume.getType();
        if (type.equals(".doc")) {
            try {
                String imgpath = ResourceUtils.getURL("xxnursery/").getPath() + "word/upload/image";
                String filepath = ResourceUtils.getURL("xxnursery/").getPath() + "word/upload";
                String imagePath = imgpath.replace('/', '\\').substring(1, imgpath.length());
                String filePath = filepath.replace('/', '\\').substring(1, filepath.length());
                filepath = filePath + "\\" + resume.getId() + resume.getType();
                String content = POIUtils.docHtmlContent(filepath, imagePath);
                resume.setHtmlContent(content);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        if (type.equals(".docx")) {
//            POIUtils.docxToHtml();
        }
        return resume;
    }

    @Override
    public DomesticConsumerResumeDO findResumeDOByConsuemrId(String consumerId)throws SQLException{
        String resumeId = domesticConsumerMapper.selectResumeIdByConsumerID(consumerId);
        DomesticConsumerResumeDO resume = resumeMapper.selectConsumerResumeByResumeId(resumeId);
        String type = resume.getType();
        if (type.equals(".doc")) {
            try {
                String imgpath = ResourceUtils.getURL("xxnursery/").getPath() + "word/upload/image";
                String filepath = ResourceUtils.getURL("xxnursery/").getPath() + "word/upload";
                String imagePath = imgpath.replace('/', '\\').substring(1, imgpath.length());
                String filePath = filepath.replace('/', '\\').substring(1, filepath.length());
                filepath = filePath + "\\" + resume.getId() + resume.getType();
                String content = POIUtils.docHtmlContent(filepath, imagePath);
                resume.setHtmlContent(content);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        return resume;
    }

    @Override
    public Map<String, String> findResumeURLByConsumerName(String name) throws SQLException {
        Map<String, String> result = new HashMap<>();
        result.put("code", String.valueOf(CommonCode.FAIL.code()));
        String resumeId = domesticConsumerMapper.selectResumeIdByConsumerName(name);
        String resumeURL = resumeMapper.selectURLById(resumeId);
        if (!StringUtils.isEmpty(resumeURL)) {
            result.put("code", String.valueOf(CommonCode.SUCCESS.code()));
            result.put("url", resumeURL);
        }
        return result;
    }

    @Override
    public boolean findResumeAndConsumerDO(String recruitId, String consumerId) throws SQLException {
        RecruitAndConsumerDO recruitAndConsumerDO = recruitAndConsumerMapper.findResumeAndConsumerDO(recruitId,consumerId);
        if (!ObjectUtils.isEmpty(recruitAndConsumerDO)){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, String> findResumeURLByConsumerId(String consumerID) throws SQLException {
        Map<String, String> result = new HashMap<>();
        result.put("code", String.valueOf(CommonCode.FAIL.code()));
        String resumeId = domesticConsumerMapper.selectResumeIdByConsumerID(consumerID);
        String resumeURL = resumeMapper.selectURLById(resumeId);
        if (!StringUtils.isEmpty(resumeURL)) {
            result.put("code", String.valueOf(CommonCode.SUCCESS.code()));
            result.put("url", resumeURL);
        }
        return result;
    }
}
