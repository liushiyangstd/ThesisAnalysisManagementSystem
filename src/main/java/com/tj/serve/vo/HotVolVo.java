package com.tj.serve.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class HotVolVo {
    @ExcelProperty(value = "热点词",index = 0)
    private String vol;
    @ExcelProperty(value = "该热点词的个数",index =1)
    private Integer count;
}
