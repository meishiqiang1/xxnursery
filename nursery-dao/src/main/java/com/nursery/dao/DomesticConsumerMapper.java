package com.nursery.dao;

import com.nursery.beans.DomesticConsumerDO;
import com.nursery.beans.RoleDO;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;
import java.util.List;

/**
 * 用户表操作
 */
public interface DomesticConsumerMapper {

    //保存
    @Insert("insert into tb_consumer (consumer_id,consumer_address,consumer_cellphone,consumer_name,consumer_password," +
            "consumer_qianming,consumer_email,consumer_sex,consumer_nickname,consumer_age" +
            ",consumer_status,consumer_educationBg,consumer_URL,consumer_birthDay,consumer_joinDay) " +
            "values " +
            "(#{consumerID},#{consumerAddress},#{consumerCellPhone},#{consumerName},#{consumerPass}" +
            ",#{consumerSignature},#{consumerEmail},#{consumerSex},#{consumerNickname},#{consumerAge}" +
            ",#{consumerStatus},#{consumerEducationBg},#{consumerURL}" +
            ",#{consumerBirthday},#{consumerJoinDay})")
    public void insert(DomesticConsumerDO domesticConsumerDO)throws SQLException;

    //更新
    @Update("update")
    public void update()throws Exception;

    //删除
    @Delete("delete from tb_consumer where consumer_id = #{consumerID}")
    public  void delete(String consumerID)throws Exception;

    //根据姓名查询
    @Select("select * from tb_consumer where consumer_name = #{name}")
    @Results(
            id = "domesticConsumer",
            value = {
                    @Result(id = true,column = "consumer_id",property = "consumerID"),
                    @Result(column = "consumer_address",property = "consumerAddress"),
                    @Result(column = "consumer_cellphone",property = "consumerCellPhone"),
                    @Result(column = "consumer_email",property = "consumerEmail"),
                    @Result(column = "consumer_name",property = "consumerName"),
                    @Result(column = "consumer_password",property = "consumerPass"),
                    @Result(column = "consumer_qianming",property = "consumerSignature"),
                    @Result(column = "consumer_sex",property = "consumerSex"),
                    @Result(column = "consumer_nickname",property = "consumerNickname"),
                    @Result(column = "consumer_age",property = "consumerAge"),
                    @Result(column = "consumer_status",property = "consumerStatus"),
                    @Result(column = "consumer_educationBg",property = "consumerEducationBg"),
                    @Result(column = "consumer_URL",property = "consumerURL"),
                    @Result(column = "consumer_birthday",property = "consumerBirthday"),
                    @Result(column = "resumeISNOT",property = "resumeISNOT"),
                    @Result(column = "consumer_joinDay",property = "consumerJoinDay")
            })
    public  DomesticConsumerDO selectByName(String name)throws Exception;

    //根据手机号查询
    @Select("select * from tb_consumer where consumer_name = #{name}")
    @ResultMap("domesticConsumer")
    public  DomesticConsumerDO selectByCellphone(String consumerCellPhone)throws Exception;

    //查询个人信息
    @Select("select * from tb_consumer where consumer_id = #{consumerID}")
    @ResultMap("domesticConsumer")
    public  DomesticConsumerDO selectByID(String consumerID)throws Exception;

    //修改密码
    @Update("update tb_consumer set consumer_password = #{param2}  where consumer_id = #{param1}")
    public Integer updatePassword(String consumerID, String password) throws Exception;

    //校验用户是否存在 to 注册
    @Select("select * from tb_consumer " +
            "where consumer_email = #{consumerEmail} " +
            "or consumer_nickname = #{consumerNickname}")
    @ResultMap("domesticConsumer")
    public  List<DomesticConsumerDO> checkConsumerToRegister(DomesticConsumerDO checkDConsumerDo);

    //查询当前月份新增用户，
    @Select("select * from tb_consumer where consumer_joinDay like #{date} ")
    @ResultMap("domesticConsumer")
    public  List<DomesticConsumerDO> selectByMonth(String date);

    //查询当前季度份新增用户，
    @Select("select * from tb_consumer where consumer_joinDay between #{param1} AND #{param2}")
    @ResultMap("domesticConsumer")
    public  List<DomesticConsumerDO> selectByQuarter(String param1, String param2);

    //查询今年份新增用户，
    @Select("select * from tb_consumer where consumer_joinDay  like '#{date}' ")
    @ResultMap("domesticConsumer")
    public List<DomesticConsumerDO> selectByYear(String date);

    //查询所有用户
    @Select("select * from tb_consumer ")
    @ResultMap("domesticConsumer")
    public List<DomesticConsumerDO> selectConsumers();

    //查询个人资料
    @Select("select * from tb_consumer where consumer_id = #{value}")
    @Results(
            value = {
                    @Result(id = true,column = "consumer_id",property = "consumerID"),
                    @Result(column = "consumer_address",property = "consumerAddress"),
                    @Result(column = "consumer_cellphone",property = "consumerCellPhone"),
                    @Result(column = "consumer_email",property = "consumerEmail"),
                    @Result(column = "consumer_name",property = "consumerName"),
                    @Result(column = "consumer_password",property = "consumerPass"),
                    @Result(column = "consumer_qianming",property = "consumerSignature"),
                    @Result(column = "consumer_sex",property = "consumerSex"),
                    @Result(column = "consumer_nickname",property = "consumerNickname"),
                    @Result(column = "consumer_age",property = "consumerAge"),
                    @Result(column = "consumer_status",property = "consumerStatus"),
                    @Result(column = "consumer_educationBg",property = "consumerEducationBg"),
                    @Result(column = "consumer_birthday",property = "consumerBirthday"),
                    @Result(column = "resumeISNOT",property = "resumeISNOT"),
                    @Result(column = "consumer_joinDay",property = "consumerJoinDay"),
                    @Result(column = "consumer_URL",property = "consumerURL",one = @One(select = "com.nursery.dao.ConsumerImageMapper.selectImageUrlByImageId"))
            })
    public DomesticConsumerDO selectConsumerByConsumerID(String consumerID) throws SQLException;

    @Update("update tb_consumer " +
            "set consumer_address = #{consumerAddress}," +
            "consumer_cellphone = #{consumerCellPhone}," +
            "consumer_email = #{consumerEmail}," +
            "consumer_name = #{consumerName}," +
            "consumer_sex = #{consumerSex}," +
            "consumer_nickname = #{consumerNickname}," +
            "consumer_age = #{consumerAge}," +
            "consumer_educationBg = #{consumerEducationBg}," +
            "consumer_qianming = #{consumerSignature}," +
            "consumer_status = #{consumerStatus}," +
            "consumer_birthday = #{consumerBirthday} " +
            "where consumer_id = #{consumerID}")
    public int updateConsumer(DomesticConsumerDO consumerDO);

    //根据mail查询
    @Select("select * from tb_consumer where consumer_email = #{param1}")
    @ResultMap("domesticConsumer")
    public List<DomesticConsumerDO> findByMailAndPass(String mail, String pass)throws SQLException;

    //根据cellphone
    @Select("select * from tb_consumer where consumer_cellphone = #{param1}")
    @ResultMap("domesticConsumer")
    public  List<DomesticConsumerDO> findByCellAndPass(String cellPhone, String pass) throws SQLException;

    //更新resume简历相关字段
    @Update("update tb_consumer set resumeISNOT = #{resumeISNOT}," +
            "resume_waijian_id = #{resumeId} " +
            "where consumer_id = #{consumerID}")
    public int updateConsumerResume(DomesticConsumerDO consumerDO);

    //更新resume简历相关字段
    @Update("update tb_consumer set resumeISNOT = #{resumeISNOT} where consumer_nickname = #{consumerNickname}")
    public int updateResumeISNOT(DomesticConsumerDO consumerDO) throws SQLException;

    //查询resume简历相关字段
    @Select("select * from tb_consumer where consumer_id = #{consumerId}")
    @Results(
            value = {
                    @Result(column = "resumeISNOT",property = "resumeISNOT"),
                    @Result(column = "resume_waijian_id",property = "consumerResume",one = @One(select = "com.nursery.dao.DomesticConsumerResumeMapper.selectConsumerResumeByResumeId"))
            })
    public DomesticConsumerDO selectConsumerResumeByConsumerID(String consumerId);

    //根据用户id查询简历id
    @Select("select resume_waijian_id from tb_consumer where consumer_id = #{consumerId}")
    public String selectResumeIdByConsumerID(String consumerId);

    //1. 登录步骤1
    @Select("select * from tb_consumer where consumer_nickname = #{username}")
    @ResultMap("domesticConsumer")
    public DomesticConsumerDO selectByUsername(String username);

    //2. 登录步骤2
    @Select("SELECT * FROM `tb_role` WHERE ID IN (SELECT a.`ROLEID`  FROM  `tb_middle_consumer_role` a WHERE `CONSUMER` = #{id});")
    @Results({
            @Result(column = "ID",property = "id",id = true),
            @Result(column = "ROLENAME",property = "name"),
            @Result(column = "ROLEEN",property = "role")
    })
    public List<RoleDO> selectRolesByname(String id);

    @Select("select consumer_id from tb_consumer where consumer_nickname = #{name}")
    String selectConsumerIdByConsumerNickName(String name);

    @Insert("insert into tb_middle_consumer_role (ROLEID,CONSUMER,ROLEEN) value (#{id},#{consumerId},#{name})")
    void insertRole(RoleDO roleDO);

    @Update("update tb_consumer set consumer_URL = #{consumerURL} where consumer_id = #{consumerID}")
    int updateConsumerImage(DomesticConsumerDO domesticConsumerDO) throws SQLException;
}