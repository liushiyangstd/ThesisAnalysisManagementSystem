package com.tj.serve.utils;

import com.alibaba.excel.EasyExcel;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.tj.serve.exception.MyException;
import com.tj.serve.vo.HotVolVo;
import org.joda.time.DateTime;

import java.io.*;
import java.util.*;

public class DownLoadWorld {
    //用于下载热点词的
    public static String downloadWorld(List<Map.Entry<String,Integer>> sortMap, Integer pid, Integer cishu){
        //将热点词写入到文本文件中
        String url = "";
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        // 填写文件名。文件名包含路径，不包含Bucket名称。例如exampledir/exampleobject.txt。
        String filename = pid+""+cishu;
        //解决文件重复问题
        filename = UUID.randomUUID().toString()+filename;
        //创建上传日期文件夹
        String string = new DateTime().toString("yyyy/MM/dd");
        filename=string+"/"+filename+".xlsx";
        String excelFile = "write.xlsx";
        System.out.println(excelFile);
        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //将数据取出
            //String inputStream = "";
            //将数据放入列表中，然后将数据在插入到excel中
            List<HotVolVo> volVos = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : sortMap) {
                HotVolVo volVo = new HotVolVo();
                volVo.setVol(entry.getKey());
                volVo.setCount(entry.getValue());
                volVos.add(volVo);
            }
            //创建表格
            File file = new File(excelFile);
//            file.createNewFile();
            EasyExcel.write(excelFile,HotVolVo.class)
                    .sheet(cishu+"个单词组成的热点词频")
                    .doWrite(volVos);

            //InputStream in = new FileInputStream(file);
            PutObjectResult result = ossClient.putObject(bucketName, filename, file);
            file.delete();
           // System.out.println(result);
            //https://online-education-1-1.oss-cn-beijing.aliyuncs.com/2018.10.27.pdf
            url = "https://"+bucketName+"."+endpoint+"/"+filename;
            return url;
        } catch (OSSException e){
            throw new MyException(20000,"上传失败");
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }
}
