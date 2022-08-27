package com.tj.serve.controller;


import com.tj.serve.domain.ThesisType;
import com.tj.serve.service.ThesisTypeService;
import com.tj.serve.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-02-12
 */
@RestController
@RequestMapping("/serve/thesis-type")
@CrossOrigin
public class ThesisTypeController {

    @Autowired
    private ThesisTypeService thesisTypeService;

    @ApiOperation("查询所有的发表论文文献的类型")
    @GetMapping("getAllThesisType")
    public R getAllThesisType(){
        List<ThesisType> typeList = thesisTypeService.getAllThesisType();
        return R.ok().data("typeList",typeList);
    }
}

