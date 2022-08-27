package com.tj.serve.controller;


import com.tj.serve.service.WorldAddressService;
import com.tj.serve.utils.R;
import com.tj.serve.vo.HotQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lar
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/serve/world-address")
@CrossOrigin
@Api(description = "热点词管理")
public class WorldAddressController {
    @Autowired
    private WorldAddressService worldAddressService;
    @ApiOperation("下载热点词")
    @PostMapping("/dowmload")
    public R dowmload(@RequestBody HotQueryVo hotQueryVo) throws MalformedURLException {
        String dowmload = worldAddressService.dowmload(hotQueryVo);
        if (dowmload.equals("error"))
            return R.error().data("status","error");
        return R.ok().data("status",dowmload);
    }
    @ApiOperation("下载热点词")
    @PostMapping("/dowmload1")
    public R dowmload1(@RequestBody HotQueryVo hotQueryVo, HttpServletResponse response) throws MalformedURLException {
        String dowmload = worldAddressService.dowmload(hotQueryVo,response);
        if (dowmload.equals("error"))
            return R.error().data("status","error");
        return R.ok().data("status",dowmload);
    }
    @ApiOperation("下载热点词")
    @GetMapping("/dowmload2")
    public void dowmload2(Integer search,Integer pid,HttpServletResponse response) throws MalformedURLException {
        System.out.println(search);
        worldAddressService.dowmload(search,pid,response);
    }
    @ApiOperation("根据论文的分类来下载热点词")
    @GetMapping("/cateDownload")
    public void cateDownload(Integer search,Integer pid,HttpServletResponse response){

    }
}

