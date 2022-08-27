package com.tj.serve.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ThesisQueryVo {
    @ApiModelProperty(value = "论文的学科")
    private String disciplineType;
    @ApiModelProperty(value = "文献类型标识")
    private String thesisType;
    @ApiModelProperty(value = "查询开始时间")
    private String start;
    @ApiModelProperty(value = "查询结束时间")
    private String end;
}
