package com.tj.serve.controller;


import com.tj.serve.service.TheisContentService;
import com.tj.serve.service.ThesisAddressService;
import com.tj.serve.utils.R;
import com.tj.serve.vo.ThesisAddressPageVo;
import com.tj.serve.vo.ThesisAddressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/serve/thesis-address")
@Api(description = "论文上传地址管理")
@CrossOrigin
public class ThesisAddressController {

    @Autowired
    private ThesisAddressService thesisAddressService;
    @Autowired
    private TheisContentService theisContentService;
    @ApiOperation("论文上传")
    @PostMapping("uploadFile")
    public R uploadFile(MultipartFile file){
        //1获取文件
        //2调用接口上传文件，获取Url
        System.out.println(file.getOriginalFilename());
        String url = thesisAddressService.uploadFileOSS(file);
        //此处有一处bug
        //theisContentService.addPdfContent(file);
        return R.ok().data("url",url);
    }
    @ApiOperation("带有条件的分页")
    @PostMapping("getPageAddress/{current}/{limit}")
    public R getPageAddress(@PathVariable Long current,
                            @PathVariable Long limit,
                            @RequestBody ThesisAddressVo thesisAddressVo){
        ThesisAddressPageVo thesisAddressPageVo = thesisAddressService.getPageAddress(current,limit,thesisAddressVo);

        return R.ok().data("thesisAddressVos",thesisAddressPageVo.getThesisAddressVos()).data("total",thesisAddressPageVo.getTotal());
    }
    @ApiOperation("根据论文的地址进行删除论文")
    @PostMapping("/removeDataById/{thesisTitle}")
    public R removeDataById(@PathVariable String thesisTitle){
        System.out.println(thesisTitle);
        thesisAddressService.removeDataById(thesisTitle);
        return R.ok();
    }
}

