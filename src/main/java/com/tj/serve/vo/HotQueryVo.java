package com.tj.serve.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HotQueryVo {
    private Integer thesisId;
    private Integer search;
    private Integer TH_id; //论文分类id
}
