package com.nursery.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 招聘内容
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentDO {

    private String id;
    private String authorId;//招聘发布人员
    private String recruittablename;//招聘标题
    private String classify;//招聘分类信息
    private String pay;//招聘待遇--薪资
    private String starttime;//招聘开始时间
    private String endtime;//招聘结束时间
    private String place;//工作地点
    private String requireExperience;//招聘要求--工作经验*
    private String requireEduDB;//招聘要求--学历
    private int manNumbers;//招聘人数
    private String companyresume;//公司简历
    private String jobdesciption;//职位描述
    private String isNotStaleDated;//时间是否过期
    private String experience;//工作经验
    private String[] types;
    private String[] labels;
    private String responsibility;//职责描述
    private String jobrequirement;//职位要求
    private String treatment;//职位待遇
    private int applynum;//报名人数
    private String cutoff;//报名是否过期
    private String enrollFull;//报名人数已满
    private String isActivate;//是否审核    yes,no
    private String auditState;//审核状态    yes,no
    private String auditResult;//审核结果、反馈
}
