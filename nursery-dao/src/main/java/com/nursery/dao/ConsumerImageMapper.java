package com.nursery.dao;

import com.nursery.beans.ConsumerImageDO;

import java.sql.SQLException;

/**
 * author:MeiShiQiang
 * Date:2021/2/7 | Time:17:33
 * tb_consumerimage
 */
public interface ConsumerImageMapper {

    //根据id查询地址
    String selectImageUrlByImageId(String imageId)throws Exception;

    //保存
    void insert(ConsumerImageDO consumerImageDO) throws SQLException;

}
