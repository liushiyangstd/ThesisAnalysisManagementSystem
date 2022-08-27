package com.tj.serve.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tj.serve.domain.Thesis;
import com.tj.serve.domain.WorldAddress;
import com.tj.serve.mapper.WorldAddressMapper;
import com.tj.serve.service.ThesisService;
import com.tj.serve.service.WorldAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.serve.utils.ConstantPropertiesUtil;
import com.tj.serve.vo.HotQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cj
 * @since 2022-02-14
 */
@Service
public class WorldAddressServiceImpl extends ServiceImpl<WorldAddressMapper, WorldAddress> implements WorldAddressService {

    @Autowired
    private ThesisService thesisService;
    @Override
    public void insertWorldAddress(Integer pid, Integer cishu, String url) {
        WorldAddress worldAddress = new WorldAddress();
        worldAddress.setPid(pid);
        worldAddress.setServerAdderss(url);
        worldAddress.setCishu(cishu);
        baseMapper.insert(worldAddress);
    }

    @Override
    public String dowmload(HotQueryVo hotQueryVo) throws MalformedURLException {
        Integer search = hotQueryVo.getSearch();
        Integer pid = hotQueryVo.getThesisId();
        System.out.println("search====="+search);
        if (pid==null)
        {
            Thesis thesis = thesisService.getOne(new QueryWrapper<Thesis>().orderByDesc("id").last("limit 1"));
            pid=thesis.getId();
            System.out.println("thesisId:"+pid.toString());
        }
        if (search==null)
            search = 1;
        QueryWrapper<WorldAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("cishu",search);
        wrapper.eq("pid",pid);
        WorldAddress worldAddress = baseMapper.selectOne(wrapper);
        if (worldAddress==null)
            return "error";
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        URL url = new URL(worldAddress.getServerAdderss());

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();

            FileOutputStream fs = new FileOutputStream("D:/"+worldAddress.getPid()+"_"+worldAddress.getCishu()+".txt");
            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";

    }

    @Override
    public String dowmload(HotQueryVo hotQueryVo, HttpServletResponse response) throws MalformedURLException {
        Integer search = hotQueryVo.getSearch();
        Integer pid = hotQueryVo.getThesisId();
        System.out.println("search====="+search);
        if (pid==null)
        {
            Thesis thesis = thesisService.getOne(new QueryWrapper<Thesis>().orderByDesc("id").last("limit 1"));
            pid=thesis.getId();
            System.out.println("thesisId:"+pid.toString());
        }
        if (search==null)
            search = 1;
        QueryWrapper<WorldAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("cishu",search);
        wrapper.eq("pid",pid);
        WorldAddress worldAddress = baseMapper.selectOne(wrapper);
        if (worldAddress==null)
            return "error";
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        URL url = new URL(worldAddress.getServerAdderss());

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();

           // response.setContentType("application/multipart/form-data");
            /* 设置文件头：最后一个参数是设置下载文件名(假如我们叫a.ini)   */
            response.setHeader("Content-Disposition", "attachment;filename="+worldAddress.getPid()+"_"+worldAddress.getCishu()+".txt");
            ServletOutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @Override
    public String dowmload(Integer search, Integer pid,HttpServletResponse response) throws MalformedURLException {
        if (pid==null)
        {
            Thesis thesis = thesisService.getOne(new QueryWrapper<Thesis>().orderByDesc("id").last("limit 1"));
            pid=thesis.getId();
            System.out.println("thesisId:"+pid.toString());
        }
        if (search==null)
            search = 1;
        QueryWrapper<WorldAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("cishu",search);
        wrapper.eq("pid",pid);
        WorldAddress worldAddress = baseMapper.selectOne(wrapper);
        if (worldAddress==null)
            return "error";
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        URL url = new URL(worldAddress.getServerAdderss());

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();

            // response.setContentType("application/multipart/form-data");
            /* 设置文件头：最后一个参数是设置下载文件名(假如我们叫a.ini)   */
            response.setHeader("Content-Disposition", "attachment;filename="+worldAddress.getPid()+"_"+worldAddress.getCishu()+".xlsx");
            ServletOutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
