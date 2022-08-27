package com.tj.serve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tj.serve.domain.User;
import com.tj.serve.service.UserService;
import com.tj.serve.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/serve/user")
@Api(description = "用户管理")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登陆")
    @CrossOrigin
    @GetMapping ("login")
    //{"code":20000,"data":{"token":"admin"}}
    public R login(@RequestParam("username") String username, @RequestParam("password") String password){
        System.out.println(username + password+" test");
        Integer status = userService.UserLogin(username,password);
        if(status == 1){
            return R.ok().message("登录成功").data("status",1);
        }else if(status == 0){
            return R.error().message("账号或密码错误").data("status",0);
        }else if(status == -1){
            return R.error().message("用户不存在").data("status",-1);
        }else if(status == -2){
            return R.error().message("登录失败").data("status",-2);
        }
        return R.error();
    }

    @ApiOperation(value = "注册")
    @PostMapping("register")
    public R register(@RequestParam("username") String username, @RequestParam("password") String password){
        Integer status = userService.UserRegister(username,password);
        if(status == 1){
            return R.ok().message("注册成功").data("status",1);
        }else if(status == 0){
            return R.error().message("注册失败").data("status",0);
        }else if(status == -1){
            return R.error().message("用户已存在").data("status",-1);
        }else{
            return R.error();
        }

    }

    @ApiOperation(value = "用户信息")
    @GetMapping("info")
    //{"code":20000,"data":{"roles":["admin"],"name":"admin","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}
    public R info(){
        return R.ok().data("roles","admin")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    @ApiOperation("查询所有的用户")
    @GetMapping("/getAllUser")
    public R getAllUser(){
        List<User> users = userService.getAllUser();
        return R.ok().data("users",users);
    }
    @ApiOperation(value = "获取全部的用户带有分页")
    @PostMapping("getPageUser/{current}/{limit}")
    public R getPageUser(@PathVariable Long current,
                        @PathVariable Long limit){
        Page<User> userPage = new Page<>(current,limit);
        userService.page(userPage,null);
        long total = userPage.getTotal();
        List<User> records = userPage.getRecords();
        return R.ok().data("total",total).data("userList",records);
    }
    @ApiOperation("根据id对用户进行删除")
    @DeleteMapping("{id}")
    public R removeDataById(@PathVariable Integer id){
        boolean byId = userService.removeById(id);
        if (byId)
            return R.ok();
        else
            return R.error();
    }
    @ApiOperation("根据id查询用户")
    @GetMapping("getByIdUser/{id}")
    public R getByIdUser(@PathVariable Integer id){
        User user=userService.getByIdUser(id);
        return R.ok().data("userVo",user);
    }
    @ApiOperation("根据用户id修改用户")
    @PostMapping("updateUser")
    public R updateUser(@RequestBody User user){
        boolean byId = userService.updateById(user);
        if(byId){
            return R.ok();
        }else{
            return R.error();
        }
    }
}

