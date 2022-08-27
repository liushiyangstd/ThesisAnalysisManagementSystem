package com.tj.serve.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThesisAddressPageVo {
    private List<ThesisAddressVo> thesisAddressVos;
    private long total;
}
