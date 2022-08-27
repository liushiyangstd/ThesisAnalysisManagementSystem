package com.tj.serve.service;

import com.tj.serve.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.serve.utils.R;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lar
 * @since 2022-02-11
 *
 */
public interface UserService extends IService<User> {

    List<User> getAllUser();

    Integer UserLogin(String name, String password);
    User getByIdUser(Integer id);

//    注册模块
    Integer UserRegister(String username,String password);
}
