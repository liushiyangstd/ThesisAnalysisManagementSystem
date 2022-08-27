package com.tj.serve.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tj.serve.domain.Thesis;
import com.tj.serve.domain.ThesisAddress;
import com.tj.serve.domain.ThesisDiscipline;
import com.tj.serve.domain.ThesisType;
import com.tj.serve.exception.MyException;
import com.tj.serve.mapper.ThesisAddressMapper;
import com.tj.serve.service.ThesisAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.serve.service.ThesisDisciplineService;
import com.tj.serve.service.ThesisService;
import com.tj.serve.service.ThesisTypeService;
import com.tj.serve.utils.ConstantPropertiesUtil;
import com.tj.serve.vo.ThesisAddressPageVo;
import com.tj.serve.vo.ThesisAddressVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cj
 * @since 2022-02-12
 */
@Service
public class ThesisAddressServiceImpl extends ServiceImpl<ThesisAddressMapper, ThesisAddress> implements ThesisAddressService {

    @Autowired
    private ThesisService thesisService;
    @Autowired
    private ThesisDisciplineService thesisDisciplineService;
    @Autowired
    private ThesisTypeService thesisTypeService;
    @Override
    public String uploadFileOSS(MultipartFile file) {
        String url = "";
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        // 填写文件名。文件名包含路径，不包含Bucket名称。例如exampledir/exampleobject.txt。
        String filename = file.getOriginalFilename();
        //解决文件重复问题
        filename = UUID.randomUUID().toString()+filename;
        //创建上传日期文件夹
        String string = new DateTime().toString("yyyy/MM/dd");
        filename=string+"/"+filename;
        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            try {
                InputStream inputStream = file.getInputStream();
                PutObjectResult result = ossClient.putObject(bucketName, filename, inputStream);
                System.out.println(result);
                //https://online-education-1-1.oss-cn-beijing.aliyuncs.com/2018.10.27.pdf
                url = "https://"+bucketName+"."+endpoint+"/"+filename;
                return url;
            } catch (IOException e) {
                throw new MyException(20000,"上传失败");
            }

        } catch (OSSException e){
            throw new MyException(20000,"上传失败");
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    @Override
    public void addThesisAddress(String url) {
        //首先查询论文引用
        QueryWrapper<Thesis> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 1");
        Thesis thesis = thesisService.getOne(wrapper);
        //获取论文id
        Integer thesisId = thesis.getId();
        ThesisAddress thesisAddress = new ThesisAddress();
        thesisAddress.setId(null);
        thesisAddress.setPid(thesisId);
        thesisAddress.setServerAdderss(url);
        //对地址进行插入
        baseMapper.insert(thesisAddress);
    }

    @Override
    public ThesisAddressPageVo getPageAddress(Long current, Long limit, ThesisAddressVo thesisAddressVo) {
        QueryWrapper<Thesis> thesisQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(thesisAddressVo.getAuthor()))
            thesisQueryWrapper.like("author",thesisAddressVo.getAuthor());
        if (!StringUtils.isEmpty(thesisAddressVo.getThesisTitle()))
            thesisQueryWrapper.like("thesis_title",thesisAddressVo.getThesisTitle());
        if (!StringUtils.isEmpty(thesisAddressVo.getDisciplineType()))
            thesisQueryWrapper.like("discipline_type",thesisAddressVo.getDisciplineType());
        if (!StringUtils.isEmpty(thesisAddressVo.getThesisType()))
            thesisQueryWrapper.like("thesis_type",thesisAddressVo.getThesisType());
        //首先是对论文进行查询
        Page<Thesis> thesisPage = new Page<>(current,limit);
        thesisService.page(thesisPage,thesisQueryWrapper);
        List<Thesis> records = thesisPage.getRecords();
        ThesisAddressPageVo thesisAddressPageVo = new ThesisAddressPageVo();
        thesisAddressPageVo.setTotal(thesisPage.getTotal());
        thesisAddressPageVo.setThesisAddressVos(new ArrayList<>());
        //List<ThesisAddressVo> thesisAddressVos =  List<ThesisAddressVo> thesisAddressVos;
        //1.查询所有的论文学科
        List<ThesisDiscipline> thesisDiscipline = thesisDisciplineService.getAllThesisDiscipline();
        List<ThesisType> thesisType = thesisTypeService.getAllThesisType();
        for (Thesis record : records) {
            ThesisAddressVo thesisAddressVo1 = new ThesisAddressVo();
            thesisAddressVo1.setAuthor(record.getAuthor());
            for (ThesisDiscipline discipline : thesisDiscipline) {
                if (discipline.getId().toString().equals(record.getDisciplineType())){
                    thesisAddressVo1.setDisciplineType(discipline.getDisciplineName().toString());
                    break;
                }
            }
            thesisAddressVo1.setThesisTitle(record.getThesisTitle());
            for (ThesisType type : thesisType) {
                if (type.getType().equals(record.getThesisType())){
                    thesisAddressVo1.setThesisType(type.getTypeName()+"("+type.getType()+")");
                    break;
                }
            }
            //查询论文的引用地址
            QueryWrapper<ThesisAddress> thesisAddressQueryWrapper = new QueryWrapper<>();
            thesisAddressQueryWrapper.eq("pid",record.getId());
            ThesisAddress thesisAddress = baseMapper.selectOne(thesisAddressQueryWrapper);
            if (thesisAddress!=null){
                thesisAddressVo1.setServerAdderss(thesisAddress.getServerAdderss());
                thesisAddressPageVo.getThesisAddressVos().add(thesisAddressVo1);
            }
            thesisAddressVo1=null;
        }

        return thesisAddressPageVo;
    }

    @Override
    public void removeDataById(String thesisTitle) {
       //根据论文的标题查询论文
        QueryWrapper<Thesis> thesisQueryWrapper = new QueryWrapper<>();
        thesisQueryWrapper.eq("thesis_title",thesisTitle);
        Thesis thesis = thesisService.getOne(thesisQueryWrapper);
        //删除论文
        thesisService.removeById(thesis.getId());
        //删除论文引用地址
        if (!StringUtils.isEmpty(thesis.getId())){
            QueryWrapper<ThesisAddress> wrapper = new QueryWrapper<>();
            wrapper.eq("pid",thesis.getId());
            baseMapper.delete(wrapper);
        }

    }
}
