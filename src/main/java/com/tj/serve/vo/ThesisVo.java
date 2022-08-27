package com.tj.serve.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author cj
 * @since 2022-02-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Thesis对象", description="")
public class ThesisVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "论文序号")
    private String thesisOrder;

    private String author;

    @ApiModelProperty(value = "论文题目")
    private String thesisTitle;

    @ApiModelProperty(value = "文献类型标识")
    private String thesisType;

    @ApiModelProperty(value = "出版地")
    private String publicationPlace;

    @ApiModelProperty(value = "出版社")
    private String publicationName;

    @ApiModelProperty(value = "出版年")
    private String publicationYear;

    @ApiModelProperty(value = "起止页码")
    private String pageNum;

    @ApiModelProperty(value = "第几期")
    private String stageOrder;

    @ApiModelProperty(value = "析出文献作者")
    private String precipitateAuthor;

    @ApiModelProperty(value = "析出文献作者")
    private String precipitateTitle;

    @ApiModelProperty(value = "出版次数")
    private String publicationNum;

    @ApiModelProperty(value = "论文的学科")
    private String disciplineType;

    @ApiModelProperty(value = "论文地址")
    private String url;
}
