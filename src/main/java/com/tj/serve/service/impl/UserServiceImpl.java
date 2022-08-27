package com.tj.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tj.serve.domain.User;
import com.tj.serve.mapper.UserMapper;
import com.tj.serve.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cj
 * @since 2022-02-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<User> getAllUser() {
        List<User> users = baseMapper.selectList(null);
        return users;
    }

    @Override
    public User getByIdUser(Integer id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        User user = baseMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public Integer UserLogin(String name, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",name);
        User user = baseMapper.selectOne(wrapper);
        if (user == null){
            return -1;
        }else if(user.getPassword().equals(password)){
            return 1;
        }else if(!user.getPassword().equals(password)){
            return 0;
        }
        return -2;
    }

    @Override
    public Integer UserRegister(String username, String password) {
        /*查询用户*/
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = baseMapper.selectOne(wrapper);

        /*判断用户名是否重复*/
        if(user != null){
            return -1; /*用户存在返回-1*/
        }else if(user == null){
            User new_user = new User();
            new_user.setUsername(username);
            new_user.setPassword(password);
            new_user.setUserType(1);
            baseMapper.insert(new_user);
            return 1; /*注册成功返回1*/
        }else {
            return 0;
        }
    }
}
