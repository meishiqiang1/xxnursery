package com.nursery.service.impl;

import com.nursery.api.iservice.IDomesticConsumerSV;
import com.nursery.beans.DomesticConsumerDO;
import com.nursery.beans.DomesticConsumerResumeDO;
import com.nursery.beans.RoleDO;
import com.nursery.beans.bo.ConsumerBO;
import com.nursery.beans.vo.MailVo;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.ResponseResult;
import com.nursery.dao.DomesticConsumerMapper;
import com.nursery.utils.CommonUtil;
import com.nursery.utils.EmailUtils;
import com.nursery.utils.POIUtils;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Create with IDEA
 * author:MeiShiQiang
 * Date:2021/1/2
 * Time:12:36
 */
@Service
public class DomesticConsumerImpl implements IDomesticConsumerSV {
    private static final Logger logger = LoggerFactory.getLogger(DomesticConsumerImpl.class);

    @Autowired
    @SuppressWarnings("all")
    private DomesticConsumerMapper mapper;

    @Override
    public DomesticConsumerDO findByconsumerID(String consumerID) throws Exception {
        return mapper.selectByID(consumerID);
    }

    @Override
    public boolean selectPassword(String consumerID, String password) {
        try {
            DomesticConsumerDO byconsumerID = findByconsumerID(consumerID);
            String consumerPass = byconsumerID.getConsumerPass();
            if (consumerPass.equals(password)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean updatePassword(String consumerID, String password) {
        try {
            DomesticConsumerDO byconsumerID = findByconsumerID(consumerID);
            String consumerPass = byconsumerID.getConsumerPass();
            if (!consumerPass.equals(password)) {
                Integer integer = mapper.updatePassword(consumerID, password);
                if (integer == 1) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ResponseResult insertConsumer(DomesticConsumerDO consumerDO) throws Exception {
        //注册前判断
        String checkEmail = consumerDO.getConsumerEmail();
        String checkCellPhone = consumerDO.getConsumerCellPhone();
        String consumerName = consumerDO.getConsumerName();
        String nickname = consumerDO.getConsumerNickname();
        DomesticConsumerDO checkDConsumerDo = new DomesticConsumerDO();
        ResponseResult success = ResponseResult.SUCCESS();
        if (StringUtils.isNotBlank(checkEmail)){
            checkDConsumerDo.setConsumerEmail(checkEmail);
        }
        if (StringUtils.isNotBlank(checkCellPhone)){
            checkDConsumerDo.setConsumerCellPhone(checkCellPhone);
        }
        if (StringUtils.isNotBlank(nickname)){
            checkDConsumerDo.setConsumerNickname(nickname);
        }
        List<DomesticConsumerDO> resultList = mapper.checkConsumerToRegister(checkDConsumerDo);
        if (!resultList.isEmpty()){
            logger.warn("注册用户已经存在，sql错误");
            success.setCommonCode(CommonCode.CONSUMER_IS_EXIST);
            return success;
        }
        try {
            String passwordEncode = CommonUtil.passwordEncode(consumerDO.getConsumerPass());
            consumerDO.setConsumerPass(passwordEncode);
            mapper.insert(consumerDO);
            RoleDO roleDO = new RoleDO();
            roleDO.setId("5f69554ec3bc11e999ff1cb72c0beab7");
            roleDO.setName("USER");
            roleDO.setConsumerId(consumerDO.getConsumerID());
            mapper.insertRole(roleDO);
            EmailUtils sendEmailUtils = null;
            String consumerEmail = consumerDO.getConsumerEmail();
            String consumerCellPhone = consumerDO.getConsumerCellPhone();

            // 后期邮件功能
            if (StringUtils.isNotBlank(consumerEmail) && EmailUtils.checkEmail(consumerEmail)) {
                logger.info("发送邮件consumerEmail：" + consumerEmail);
                sendEmailUtils = new EmailUtils();
                MailVo mailVo = new MailVo();
                mailVo.setTo(consumerEmail);
                mailVo.setSubject("发送邮件标题");
                mailVo.setText("发送邮件的正文内容。。。。。。。");
                try {
                    sendEmailUtils.sendSimpleEmail(mailVo);
                } catch (Exception e) {
                    logger.debug("注册用户：" + consumerDO.getConsumerID() + "发送邮件错误");
                }
            }
            // 手机号短信
            if (StringUtils.isNotBlank(consumerCellPhone)&& checkCellphone(consumerCellPhone)) {}
        }catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            logger.error("注册错误");
            success.setCommonCode(CommonCode.FAIL);
        }
        return success;
    }

    //2021-01-21 16:32:53
    @Override
    public List<DomesticConsumerDO> selectByMonth(String date) {
        String dateMonth = date.substring(0,7);
        return mapper.selectByMonth(dateMonth+"%");
    }

    @Override
    public List<DomesticConsumerDO> selectByQuarter(String date) {
        String month = date.substring(5,7);
        String year = date.substring(0,5);
        int num = Integer.parseInt(month);
        String startMonth = "";
        String endMonth = "";
        if (1<=num && num<4){
            startMonth = "01";
            endMonth="04";
        }else if (4<=num && num<7){
            startMonth = "04";
            endMonth="07";
        }else if (7<=num && num<10){
            startMonth = "07";
            endMonth="10";
        }else {
            startMonth = "10";
            endMonth="01";
        }
        startMonth = year+startMonth+"%";
        endMonth = year+endMonth+"%";
        return mapper.selectByQuarter(startMonth,endMonth);
    }

    @Override
    public List<DomesticConsumerDO> selectByYear(String date) {
        return mapper.selectByYear(date.substring(0,4)+"%");
    }

    @Override
    public List<DomesticConsumerDO> selectConsumers() {
        return mapper.selectConsumers();
    }

    @Override
    public DomesticConsumerDO selectConsumerByConsumerID(String consumerID) throws SQLException {
        return mapper.selectConsumerByConsumerID(consumerID);
    }

    @Override
    public int updateConsumer(DomesticConsumerDO consumerDO) {
        return mapper.updateConsumer(consumerDO);
    }

    @Override
    public void addPassword(String id,String password) throws Exception {
        mapper.updatePassword(id,password);
    }

    @Override
    public ConsumerBO findByMailAndPass(String mail, String pass) throws SQLException {
        ConsumerBO consumerBO = new ConsumerBO();
        List<DomesticConsumerDO> consumerDOList = mapper.findByMailAndPass(mail,pass);
        if (consumerDOList!=null && consumerDOList.size()>0){
            DomesticConsumerDO consumerDO = consumerDOList.get(0);
            if (consumerDO.getConsumerPass().equals(pass)){
                consumerBO.setId(consumerDO.getConsumerID());
                consumerBO.setYhu(consumerDO.getConsumerNickname());
                consumerBO.setIntroduce("其他技术职业.1年");
                return consumerBO;
            }
        }
        return null;
    }

    @Override
    public ConsumerBO findByCellAndPass(String cellPhone, String pass) throws SQLException {
        ConsumerBO consumerBO = new ConsumerBO();
        List<DomesticConsumerDO> consumerDOList = mapper.findByCellAndPass(cellPhone,pass);
        if (consumerDOList!=null && consumerDOList.size()>0){
            DomesticConsumerDO consumerDO = consumerDOList.get(0);
            if (consumerDO.getConsumerPass().equals(pass)){
                consumerBO.setId(consumerDO.getConsumerID());
                consumerBO.setYhu(consumerDO.getConsumerNickname());
                return consumerBO;
            }
        }
        return null;
    }

    @Override
    public int updateConsumerResume(DomesticConsumerDO consumerDO) {
        return mapper.updateConsumerResume(consumerDO);
    }


    @Override
    public int updateResumeISNOT(DomesticConsumerDO consumerDO) throws SQLException {
        return mapper.updateResumeISNOT(consumerDO);
    }


    @Override
    public DomesticConsumerDO selectConsumerResumeByConsumerID(String consumerId) throws SQLException{
        DomesticConsumerDO consumerDO = mapper.selectConsumerResumeByConsumerID(consumerId);
        if(consumerDO.getResumeISNOT()==0){
            return consumerDO;
        }
        DomesticConsumerResumeDO resume = consumerDO.getConsumerResume();
        String url = resume.getUrl();
        String type = resume.getType();
        if (type.equals(".doc")){
            try {
                String imgpath = ResourceUtils.getURL("xxnursery/").getPath() + "word/upload/image";
                String filepath = ResourceUtils.getURL("xxnursery/").getPath() + "word/upload";
                String imagePath = imgpath.replace('/', '\\').substring(1, imgpath.length());
                String filePath = filepath.replace('/', '\\').substring(1, filepath.length());
                filepath = filePath+"\\"+resume.getId()+resume.getType();
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
        return consumerDO;
    }

    @Override
    public String selectResumeIdByConsumerID(String consumerId){
        return mapper.selectResumeIdByConsumerID(consumerId);
    }

    @Override
    public DomesticConsumerDO findByUsername(String username) {
        return mapper.selectByUsername(username);
    }

    @Override
    public List<RoleDO> findRolesByUsername(String id) {
        return mapper.selectRolesByname(id);
    }

    @Override
    public String selectConsumerIdByConsumerNickName(String name) {
        return mapper.selectConsumerIdByConsumerNickName(name);
    }

    //校验手机号
    private boolean checkCellphone(String consumerCellPhone){
        boolean flag = false;
        return true;
    }
}