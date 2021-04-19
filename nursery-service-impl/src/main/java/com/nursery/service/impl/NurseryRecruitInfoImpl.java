package com.nursery.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.nursery.api.iservice.INurseryRecruitInfoSV;
import com.nursery.beans.DBDataParam;
import com.nursery.beans.RecruitmentDO;
import com.nursery.common.model.CommonAttrs;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.ResponseResult;
import com.nursery.dao.NurseryRecruitmentMapper;
import com.nursery.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * 招聘信息 业务类
 */
@Service
public class NurseryRecruitInfoImpl implements INurseryRecruitInfoSV {

    private static Logger logger = LoggerFactory.getLogger(NurseryRecruitInfoImpl.class);

    @Autowired
    @SuppressWarnings("all")
    private NurseryRecruitmentMapper mapper;

    @Override
    public List<RecruitmentDO> recruitList(DBDataParam dbDataParam) throws NullPointerException, SQLException {
        List<RecruitmentDO> list = null;
        if (dbDataParam != null) {
            try {
                list = mapper.selectByclassAndName(dbDataParam);
                if (list == null) {
                    System.out.println("抛出 null 对象错误");
                    throw new NullPointerException();
                }
            } catch (SQLException e) {
                throw new SQLException();
            }
        }
        return list;
    }

    @Override
    public List<RecruitmentDO> selectRecruitinfoByerid(String userId) throws SQLException {
        return mapper.selectRecruitmentDOsByRecruiterID(userId);
    }

    @Override
    public List<RecruitmentDO> selectRecruitinfoByerid() throws SQLException {
        return mapper.selectRecruitmentDOs();
    }

    @Override
    public List<RecruitmentDO> selectRecruitmentDOs() throws SQLException {
        return mapper.selectRecruitmentDOs();
    }

    @Override
    public RecruitmentDO selectRecruitInfoByrecruitid(String recruitid) throws SQLException {
        return mapper.selectRecruitInfoByrecruitid(recruitid);
    }

    @Override
    public int updateRecruitInfo(RecruitmentDO recruitmentDO) {
        int i = 0;
        try {
            i = mapper.updateRecruitInfo(recruitmentDO);
        } catch (SQLException throwables) {
            logger.error("NurseryRecruitInfoImpl ==> updateRecruitInfo ： mapper" +
                    " sql语句有问题");
        }
        return i;
    }

    @Override
    public List<RecruitmentDO> getRandomRecruit() throws SQLException {
        List<RecruitmentDO> recruitmentDOList = mapper.randomSelectRecruit();
        logger.info("职位推荐:" + JSONObject.toJSONString(recruitmentDOList));
        return mapper.randomSelectRecruit();
    }

    @Override
    public List<RecruitmentDO> selectRecruitinfoByName(String tableName) {
        return null;
    }

    @Override
    public List<RecruitmentDO> getRecruitByType(String type) throws SQLException {
        type = "%" + type + "%";
        return mapper.selectRecruitinfoByType(type);
    }

    @Override
    public List<RecruitmentDO> getRecruitByNewDate() throws SQLException {
        List<RecruitmentDO> newDate = mapper.selectRecruitByNewDateAndDesc();
        logger.info("最新职位:" + JSONObject.toJSONString(newDate));
        return newDate;
    }

    @Override
    public List<RecruitmentDO> getRecruitByTypeId(String typeId) throws SQLException {
        if (!StringUtils.isEmpty(typeId)) {
            typeId = "%" + typeId + "%";
        }
        List<RecruitmentDO> hotDate = mapper.selectRecruitinfoByType(typeId);
        logger.info("热门职位:" + JSONObject.toJSONString(hotDate));
        return hotDate;
    }

    @Transactional
    @Override
    public void insertRecruitInfo(RecruitmentDO recruitmentDO) throws SQLException {
        mapper.insertRecruitInfo(recruitmentDO);
        mapper.insertRecruitMeddleEr(recruitmentDO);
    }

    @Override
    public int deleteRecruitById(String erId) {
        int num = mapper.deleteRecruitById(erId);
        return 0;
    }

    /*@Override
    public List<RecruitmentDO> selectRecruitInfoByParams(DBDataParam signParam) throws SQLException {
        String search = "";
        String type = "";
        String placeId = "";
        if (signParam.contains("search:")) {
            String substring = signParam.substring(7);
            String[] split = substring.split("type:");
            search = split[0];
            String param = split[1];
            String[] split1 = param.split("placeId:");
            type = split1[0];
            try {
                placeId = split1[1];
            }catch (ArrayIndexOutOfBoundsException exception){
                placeId = "";
            }
        }
        DBDataParam dataParam = new DBDataParam();
        if (!StringUtils.isEmpty(search)) {
            search = "%" + search + "%";
            dataParam.setSearch(search);
        }
        if (!StringUtils.isEmpty(type) && !"0".equals(type)) {
            dataParam.setType(type);
        }
        if (!StringUtils.isEmpty(placeId)) {
            String place = CommonUtil.getPlace(placeId);
            if (place.equals("") || place.equals("不限")){
                place = "";
            }else {
                place = "%" + place + "%";
            }
            dataParam.setPlaceId(place);
        }
        return mapper.selectRecruitInfoByParams(dataParam);
    }*/
    public List<RecruitmentDO> selectRecruitInfoByParams(DBDataParam dataParam) throws SQLException {
        String search = dataParam.getSearch();
        String type = dataParam.getType();
        String placeId = dataParam.getPlaceId();
        String edcId = dataParam.getEdcId();
        String conditionId = dataParam.getConditionId();
        String putTimeId = dataParam.getPutTimeId();


        if (!StringUtils.isEmpty(search)) {
            search = "%" + search + "%";
            dataParam.setSearch(search);
        }
        if (!StringUtils.isEmpty(type) && !"0".equals(type)) {
            type = "%" + type + "%";
            dataParam.setType(type);
        }
        if (!StringUtils.isEmpty(placeId)) {
            String place = CommonUtil.getPlace(placeId);
            if (place.equals("") || place.equals("不限")){
                place = "";
            }else {
                place = "%" + place + "%";
            }
            dataParam.setPlaceId(place);
        }

        if (!StringUtils.isEmpty(edcId)) {
            String edc = CommonUtil.getEdc(edcId);
            dataParam.setEdcId(edc);
        }

        if (!StringUtils.isEmpty(conditionId)) {
            String condition = CommonUtil.getCondition(conditionId);
            dataParam.setConditionId(condition);
        }

        if (!StringUtils.isEmpty(putTimeId)) {
            String[] putTime = CommonUtil.getPutTime(putTimeId);
            if (putTime!=null){
                dataParam.setStateDate(putTime[0]);
                dataParam.setEndDate(putTime[1]);
            }else {
                dataParam.setStateDate("0000-00-00 00:00");
                dataParam.setEndDate("9999-00-00 00:00");
            }
        }
        return mapper.selectRecruitInfoByParams(dataParam);
    }
    @Override
    public List<RecruitmentDO> selectRecruitByIsActivate(String is) throws SQLException {
        //判断is
        if (!StringUtils.isEmpty(is)){
            return mapper.selectRecruitByIsActivate(is);
        }
        return null;
    }

    @Override
    public ResponseResult updateRecruitSetAudit(String param){
        String audit = "";
        String result = "";
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCommonCode(CommonCode.AUDIT_PASS_RESULT_NOT);
        if (!StringUtils.isEmpty(param)){
            RecruitmentDO recruitmentDO = new RecruitmentDO();
            String[] params = param.split("\\|");
            try {
                recruitmentDO.setId(params[0]);
                // 需求变更
                // 招聘审核后状态改为yes‘全部’
                audit = params[1];
                recruitmentDO.setAuditState(audit);
                recruitmentDO.setIsActivate("yes");
                result = params[2];
                recruitmentDO.setAuditResult(result);
            }catch (ArrayIndexOutOfBoundsException e){
                if (audit.equals("no")){
                    result = CommonAttrs.NOTAUDITSTR;
                }
                if (audit.equals("yes")){
                    result = CommonAttrs.YESAUDITSTR;
                }
                recruitmentDO.setAuditResult(result);
            }

            try {
                if (mapper.updateRecruitSetAudit(recruitmentDO)>0){
                    responseResult.setCommonCode(CommonCode.AUDIT_PASS_RESULT_YES);
                }
            }catch (SQLException throwables){
                responseResult.setCommonCode(CommonCode.SQL_YJ_ISNOT);
            }
        }
        return responseResult;
    }

    @Override
    public List<RecruitmentDO> selectRecruitDOs() throws SQLException{
        return mapper.selectRecruitByAuditStateAndCutoffDOs();
    }
}
