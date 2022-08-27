package com.tj.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tj.serve.domain.TheisContent;
import com.tj.serve.domain.Thesis;
import com.tj.serve.mapper.TheisContentMapper;
import com.tj.serve.service.TheisContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.serve.service.ThesisService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cj
 * @since 2022-02-13
 */
@Service
public class TheisContentServiceImpl extends ServiceImpl<TheisContentMapper, TheisContent> implements TheisContentService {
    @Autowired
    private ThesisService thesisService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<TheisContent> getPdfContent(Integer thesisId) {
        QueryWrapper<TheisContent> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",thesisId);
        List<TheisContent> contents = baseMapper.selectList(wrapper);
        return contents;
    }

    @Override
    public void addPdfContent(String url) {
        //获取论文的最后一个id
        //获取论文的最后一个id
        QueryWrapper<Thesis> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Thesis thesis = thesisService.getOne(wrapper);
        Integer pid =1;
        pid = thesis.getId();
        //使用地址远程连接到OSS对象中
        try {
            URL ossUrl = new URL(url);
            //读取
            URLConnection conn = ossUrl.openConnection();
            InputStream inputStream =  conn.getInputStream();
            PDDocument pdDocument = PDDocument.load(inputStream);
            if (!pdDocument.isEncrypted()){
                for (int i=1;i<pdDocument.getNumberOfPages()-1;i++){
                    PDFTextStripper stripper = new PDFTextStripper();
                    stripper.setSortByPosition(true);
                    stripper.setStartPage(i);
                    stripper.setEndPage(i);
                    String text = stripper.getText(pdDocument);
                    TheisContent content = new TheisContent();
                    content.setContent(text);
                    content.setPageNum(i);
                    content.setPid(pid);
                    int insertContent = baseMapper.insert(content);
                    if (insertContent<1){
                        System.out.println("插入失败");
                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getContent(Integer id) {
        //首先先获取redis中是否有该数据，如果有该数据直接取出
        String value =(String) redisTemplate.opsForValue().get(id);
        if (!StringUtils.isEmpty(value)){
            return value;
        }
        //根据id查找论文
        QueryWrapper<TheisContent> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        wrapper.select("content");
        List<TheisContent> contentList = baseMapper.selectList(wrapper);
        StringBuilder sb = new StringBuilder();
        for (TheisContent theisContent : contentList) {
            //去除空白和空格
            sb.append(StringUtils.trimWhitespace(theisContent.getContent()));
        }
        String pdfContent = StringUtils.trimAllWhitespace(sb.toString());
        pdfContent = pdfContent.replaceAll("\\["," ")
                .replaceAll("]"," ").replaceAll("\\("," ")
                .replaceAll("\\)"," ").replaceAll(","," ")
                .replaceAll("\\'"," ").replaceAll("\\-"," ")
                .replaceAll("[0-9]+"," ").
                        replaceAll("\\."," ").replaceAll("\\%"," ")
                .replaceAll("\\:"," ").replaceAll("https"," ").
                        replaceAll("www"," ").replaceAll("\\“"," ")
                .replaceAll("\\/","").replaceAll("\\;"," ")
                .replaceAll("\\”"," ").replaceAll("[\"]"," ")
                .replaceAll("\\@"," ").replaceAll("\\{"," ").
                        replaceAll("\\}"," ").replaceAll("–"," ").replaceAll("\\’"," ")
                .replaceAll("IEEE"," ").replaceAll("\\+"," ")
        .replaceAll("="," ")
        .replaceAll("©"," ");
        //将内容放到redis中
        redisTemplate.opsForValue().set(id,pdfContent);

        return pdfContent;
    }
}
