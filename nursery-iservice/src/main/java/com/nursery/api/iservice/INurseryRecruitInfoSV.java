package com.nursery.api.iservice;

import com.nursery.beans.DBDataParam;
import com.nursery.beans.RecruitmentDO;
import com.nursery.common.model.response.ResponseResult;

import java.sql.SQLException;
import java.util.List;

/**
 * 首页信息展示
 *      幼儿园 招聘信息
 */
public interface INurseryRecruitInfoSV {
    /**
     * 查询招聘信息
     * @param dbDataParam  dbDataParam.param1 dbDataParam.param2
     * @return
     * @throws NullPointerException
     * @throws SQLException
     */
    List<RecruitmentDO> recruitList(DBDataParam dbDataParam) throws NullPointerException,SQLException;

    /**
     * 根据 id获取招聘信息
     * @param userId 招聘部门人员id
     * @return
     * @throws SQLException
     */
    List<RecruitmentDO> selectRecruitinfoByerid(String userId) throws SQLException;

    /**
     * 获取招聘信息
     * @return
     * @throws SQLException
     */
    List<RecruitmentDO> selectRecruitinfoByerid() throws SQLException;

    //查询所有招聘信息
    List<RecruitmentDO> selectRecruitmentDOs() throws SQLException;

    /**
     * 根究id 查询招聘信息
     * @param recruitid
     * @return
     * @throws SQLException
     */
    RecruitmentDO selectRecruitInfoByrecruitid(String recruitid) throws SQLException;

    /**
     * 更新招聘信息
     * @param recruitmentDO
     * @return 返回受影响的行数
     * @throws SQLException
     */
    int updateRecruitInfo(RecruitmentDO recruitmentDO) throws SQLException;

    //随机获取招聘信息
    List<RecruitmentDO> getRandomRecruit() throws SQLException;

    //根据名称
    List<RecruitmentDO> selectRecruitinfoByName(String tableName);

    //根据类型
    List<RecruitmentDO> getRecruitByType(String type) throws SQLException;

    //获取最新时间的职位
    List<RecruitmentDO> getRecruitByNewDate() throws SQLException;

    //根据类型名称获取
    List<RecruitmentDO> getRecruitByTypeId(String typeId) throws SQLException;

    //插入一行招聘信息
    void insertRecruitInfo(RecruitmentDO recruitmentDO) throws SQLException;

    //根据erid删除招牌信息
    int deleteRecruitById(String erId);

    //搜索框
    List<RecruitmentDO> selectRecruitInfoByParams(DBDataParam dataParam) throws SQLException;
//    List<RecruitmentDO> selectRecruitInfoByParams(string dataParam) throws SQLException;

    /**
     * 更具审核结果查询
     * @param is 是否审核通过 'yes' , 'no'
     */
    List<RecruitmentDO> selectRecruitByIsActivate(String is) throws SQLException;

    /**
     * 改变简历的审核状态  yes,no
     * @param param  id|yes,no
     */
    ResponseResult updateRecruitSetAudit(String param);

    List<RecruitmentDO> selectRecruitDOs() throws SQLException;

}
