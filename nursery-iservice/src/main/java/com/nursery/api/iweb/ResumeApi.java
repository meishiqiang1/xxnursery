package com.nursery.api.iweb;

import com.alibaba.fastjson.JSONObject;
import com.nursery.common.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * 简历上传 下载api
 */
@Api(value = "/resume",description = "专门提供简历上传 下载的api")
public interface ResumeApi {

    /**
     * 跳转到上传简历的页面
     * 判断是否存在已经上传简历
     *      1. 没有。则返回 is.. = 0
     *      1. 有。则返回 is.. = 1, 和简历的html页面内容
     * @param param 流水号
     */
    @ApiOperation("访问上传简历/在线观看")
    public String visitResume();

    /**
     * 上传和更新
     *      1. 获取简历信息（地址，大小，后缀名）
     *      2. 获取该项目所在地址的信息 ResourceUtils.getURL("xxnursery/").getPath() + "word/upload";使用spring提供的工具类
     *      3. 保存到项目外的文件夹中，并记录地址的详细路径
     *      4. 在webMvcConfig中设置虚拟映射路径
     *      5. 返回json信息
     * 更新
     *      1. 删除信息
     *      2. 同上
     * @param file 文件上传
     * @param base64 字节码上传
     * @return
     */
    @ApiOperation("上传/更新简历简历")
    public JSONObject uploadResume(MultipartFile file);

    /**
     * @param resumeId 简历id
     * @return 成功后重定向到 访问简历
     */
    @ApiOperation("删除简历")
    public String deleteResume(String resumeId);

    @ApiOperation("简历修改")
    QueryResponseResult resumeUpdateUpload();

    @ApiOperation("简历在线观看")
    QueryResponseResult resumeOnlineReading();
}
