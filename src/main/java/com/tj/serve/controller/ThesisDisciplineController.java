package com.tj.serve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tj.serve.domain.ThesisDiscipline;
import com.tj.serve.domain.User;
import com.tj.serve.service.ThesisDisciplineService;
import com.tj.serve.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/serve/thesis-discipline")
@Api(description = "论文学科管理")
@CrossOrigin
public class ThesisDisciplineController {

    @Autowired
    private ThesisDisciplineService thesisDisciplineService;

    @ApiOperation("查询所有的论学科")
    @GetMapping("getAllThesisDiscipline")
    public R getAllThesisDiscipline(){
        List<ThesisDiscipline> disciplineList=thesisDisciplineService.getAllThesisDiscipline();
        return R.ok().data("disciplineList",disciplineList);
    }
    @ApiOperation(value = "获取全部的用户带有分页")
    @PostMapping("getPageDiscipline/{current}/{limit}")
    public R getPageDiscipline(@PathVariable Long current,
                         @PathVariable Long limit){
        Page<ThesisDiscipline> disciplinePage = new Page<>(current,limit);
        thesisDisciplineService.page(disciplinePage,null);
        long total = disciplinePage.getTotal();
        List<ThesisDiscipline> records = disciplinePage.getRecords();
        return R.ok().data("total",total).data("disciplineList",records);
    }
    @ApiOperation("根据id对用户进行删除")
    @DeleteMapping("{id}")
    public R removeDataById(@PathVariable Integer id){
        boolean byId = thesisDisciplineService.removeById(id);
        if (byId)
            return R.ok();
        else
            return R.error();
    }
    @ApiOperation("新增论文学科")
    @PostMapping("/addDiscipline")
    public R addDiscipline(@RequestBody ThesisDiscipline thesisDiscipline){
        thesisDisciplineService.save(thesisDiscipline);
        return R.ok();
    }
}

