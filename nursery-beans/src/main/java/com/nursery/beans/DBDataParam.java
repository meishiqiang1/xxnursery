package com.nursery.beans;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 通用参数
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DBDataParam {
    private String table_flag;//表名
    @ApiModelProperty("搜索关键字")
    private String search;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("地点关键字")
    private String placeId;
    @ApiModelProperty("薪资关键字")
    private String payId;

    @ApiModelProperty("学历关键字")
    private String edcId;
    @ApiModelProperty("工作关键字")
    private String conditionId;
    @ApiModelProperty("时间关键字")
    private String putTimeId;
    private String stateDate;
    private String endDate;

    private String param1;
    private String param2;
    private String param3;
}
