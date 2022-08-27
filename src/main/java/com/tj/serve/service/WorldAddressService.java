package com.tj.serve.service;

import com.tj.serve.domain.WorldAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.serve.vo.HotQueryVo;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lar
 * @since 2022-02-14
 */
public interface WorldAddressService extends IService<WorldAddress> {

    void insertWorldAddress(Integer pid, Integer cishu, String url);

    String dowmload(HotQueryVo hotQueryVo) throws MalformedURLException;

    String dowmload(HotQueryVo hotQueryVo, HttpServletResponse response)throws MalformedURLException;

    String dowmload(Integer search, Integer pid, HttpServletResponse response) throws MalformedURLException;
}
