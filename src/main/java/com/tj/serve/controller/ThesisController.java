package com.tj.serve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tj.serve.domain.Thesis;
import com.tj.serve.domain.ThesisDiscipline;
import com.tj.serve.domain.ThesisType;
import com.tj.serve.service.*;
import com.tj.serve.vo.*;
import com.tj.serve.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@RequestMapping("/serve/thesis")
@CrossOrigin
@Api(description = "上传论文管理")
public class ThesisController {
    @Autowired
    private ThesisService thesisService;
    @Autowired
    private ThesisAddressService thesisAddressService;
    @Autowired
    private ThesisTypeService thesisTypeService;
    @Autowired
    private ThesisDisciplineService thesisDisciplineService;
    @Autowired
    private TheisContentService contentService;
    @ApiOperation("对论文进行添加")
    @PostMapping("/addThesis")
    public R addThesis(@RequestBody ThesisVo thesis){
        thesisService.addThesis(thesis);
        thesisAddressService.addThesisAddress(thesis.getUrl());
        //插入论文内容
        contentService.addPdfContent(thesis.getUrl());
        return R.ok();
    }
    @ApiOperation("带有条件的分页")
    @PostMapping("getPageThesis/{current}/{limit}")
    public R getPageAddress(@PathVariable Long current,
                            @PathVariable Long limit,
                            @RequestBody ThesisQueryVo thesisQueryVo){
        System.out.println("带有条件的分页："+thesisQueryVo);

        Page<Thesis> thesisPageVos = thesisService.getPageThesis(current,limit,thesisQueryVo);
        long total = thesisPageVos.getTotal();
        List<Thesis> thesisList = thesisPageVos.getRecords();
        List<Thesis> thesisListVo = new ArrayList<>();
        List<ThesisType> thesisType = thesisTypeService.getAllThesisType();
        List<ThesisDiscipline> thesisDiscipline = thesisDisciplineService.getAllThesisDiscipline();
        for (Thesis thesis : thesisList) {
            for (ThesisDiscipline discipline : thesisDiscipline) {
                if (discipline.getId().toString().equals(thesis.getDisciplineType())){
                    thesis.setDisciplineType(discipline.getDisciplineName());
                    break;
                }
            }
            for (ThesisType type : thesisType) {
                if (type.getType().equals(thesis.getThesisType())){
                    thesis.setThesisType(type.getTypeName()+"("+type.getType()+")");
                    break;
                }
            }
            thesisListVo.add(thesis);
        }
        return R.ok().data("thesisList",thesisListVo).data("total",total);
    }
    @ApiOperation("根据论文的id来分析该论文的热点词")
    @PostMapping("/getHotVol")
    public R  getHotVol(@RequestBody HotQueryVo hotQueryVo){
        List<HotVolVo> hotVolVos= thesisService.getHotVol(hotQueryVo);
        return R.ok().data("hotVolVos",hotVolVos).data("size",hotVolVos.size());
    }
    @ApiOperation("查询所有的论文")
    @GetMapping("/getAllThesis")
    public R getAllThesis(){
        List<Thesis> thesisList = thesisService.getAllThesis();
        Integer id = thesisList.get(thesisList.size() - 1).getId();
        return R.ok().data("thesisList",thesisList).data("id",id);
    }
    @ApiOperation("根据论文分类来分析该论文的热点词")
    @PostMapping("/getCateHotVol")
    public R  getCateHotVol(@RequestBody HotQueryVo hotQueryVo){
        List<HotVolVo> hotVolVos= thesisService.getCateHotVol(hotQueryVo);
        return R.ok().data("hotVolVos",hotVolVos).data("size",hotVolVos.size());
    }
}

