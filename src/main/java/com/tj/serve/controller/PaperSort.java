package com.tj.serve.controller;

import com.tj.serve.service.PaperRankingAnsService;
import com.tj.serve.service.PaperRankingService;
import com.tj.serve.service.impl.PaperRankingServiceimpl;
import com.tj.serve.utils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "论文排序")
@RestController
@RequestMapping("/PaperSort")
public class PaperSort {

    @Autowired
    private PaperRankingService p;
    @Autowired
    private PaperRankingAnsService ansp;

    @GetMapping
    public R getTop(){

        /*如果数据库中一条数据没有，返回错误*/
        if (ansp.getTop(p.getPaperTopId()).size() == 0){
            return R.error();
        }else {
            return R.ok().data("Theis", ansp.getTop(p.getPaperTopId()));
        }
    }

}