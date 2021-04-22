package com.nursery.service.impl;

import com.nursery.api.iservice.IUserCenterSV;
import com.nursery.beans.ConsumerImageDO;
import com.nursery.beans.DomesticConsumerDO;
import com.nursery.common.model.response.CommonCode;
import com.nursery.common.model.response.QueryResponseResult;
import com.nursery.common.model.response.QueryResult;
import com.nursery.common.model.response.ResponseResult;
import com.nursery.dao.ConsumerImageMapper;
import com.nursery.dao.DomesticConsumerMapper;
import com.nursery.utils.Base64Utils;
import com.nursery.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * author:MeiShiQiang
 * Date:2021/4/22 | Time:11:02
 */
@Service
public class UserCenterImpl implements IUserCenterSV {
    private static final Logger logger = LoggerFactory.getLogger(DomesticConsumerImpl.class);

    @Autowired
    @SuppressWarnings("all")
    private DomesticConsumerMapper mapper;

    @Autowired
    @SuppressWarnings("all")
    private ConsumerImageMapper consumerImageMapper;

    @Transactional
    @Override
    public QueryResponseResult getUserCenter(String consumerName) {
        //定义返回值内容
        QueryResponseResult data = new QueryResponseResult(CommonCode.FAIL, null);
        String consumerId = "";
        try {
            consumerId = mapper.selectConsumerIdByConsumerNickName(consumerName);
        }catch (Exception e){
            logger.warn(this.getClass().getName()+ "错误信息" + e.getLocalizedMessage());
        }
        try {
            DomesticConsumerDO consumerDO = mapper.selectConsumerByConsumerID(consumerId);
            if (consumerDO != null) {
                QueryResult<DomesticConsumerDO> queryResult = new QueryResult<>();
                queryResult.setObject(consumerDO);
                data.setQueryResult(queryResult);
                data.setCommonCode(CommonCode.SUCCESS);
                return data;
            }
        } catch (Exception e) {
            logger.warn(this.getClass().getName()+ "错误信息" + e.getLocalizedMessage());
        }
        return data;
    }

    @Override
    public ResponseResult pullUser(DomesticConsumerDO consumerDO) {
        ResponseResult responseResult = new ResponseResult();
        int i = 0;
        try {
            i = mapper.updateConsumer(consumerDO);
        }catch (Exception e){
            System.out.println(e.getMessage());
            responseResult.setCommonCode(CommonCode.SERVER_ERROR);
        }
        if (i>0){
            responseResult.setCommonCode(CommonCode.SUCCESS);
        }else {
            responseResult.setCommonCode(CommonCode.FAIL);
        }
        return responseResult;
    }

    @Transactional
    @Override
    //data:image/jpeg;base64,
    public void pullImage(String contextPath, String consumerName, String base64) {
        String path = "";
        String realPath = "";
        String imageId = "";
        String[] split = base64.split(";");
        String suffix = split[0].substring(11);
        String fileName = "";
        String sqlFilePath = "";//想数据库中储存路径
        File targetFile = null;
        try {
            suffix = CommonUtil.getImgSuffix(suffix);
            path = ResourceUtils.getURL("xxnursery/").getPath() + "img/user/image";
            realPath = path.replace('/', '\\').substring(1, path.length());
            imageId = CommonUtil.getUUID();
            fileName = imageId + "." + suffix;
            targetFile = new File(realPath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            path = realPath +"\\"+ fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Base64Utils.decodeToFile(path,base64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqlFilePath = contextPath + "/upload/user/image/" + fileName;
        ConsumerImageDO consumerImageDO = new ConsumerImageDO();
        consumerImageDO.setImageId(imageId);
        consumerImageDO.setImageType(suffix);
        consumerImageDO.setImageSize(base64.length());
        consumerImageDO.setImageUrl(sqlFilePath);
        consumerImageDO.setImage64Base(base64);
        try {
            consumerImageMapper.insert(consumerImageDO);
            String consumerId = mapper.selectConsumerIdByConsumerNickName(consumerName);
            DomesticConsumerDO domesticConsumerDO =new DomesticConsumerDO();
            domesticConsumerDO.setConsumerID(consumerId);
            domesticConsumerDO.setConsumerURL(imageId);
            mapper.updateConsumerImage(domesticConsumerDO);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throwables.getSQLState();
        }
    }
}
