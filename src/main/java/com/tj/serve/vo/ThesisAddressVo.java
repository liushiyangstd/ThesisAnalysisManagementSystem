package com.tj.serve.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ThesisAddress对象")
public class ThesisAddressVo {
    @ApiModelProperty(value = "论文作者")
    private String author;
    @ApiModelProperty(value = "论文题目")
    private String thesisTitle;
    @ApiModelProperty(value = "论文的学科")
    private String disciplineType;
    @ApiModelProperty(value = "文献类型标识")
    private String thesisType;
    @ApiModelProperty(value = "论文地址")
    private String serverAdderss;
}
