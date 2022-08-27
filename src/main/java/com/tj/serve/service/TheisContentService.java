package com.tj.serve.service;

import com.tj.serve.domain.TheisContent;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface TheisContentService extends IService<TheisContent> {

    //void addPdfContent(MultipartFile file);

    List<TheisContent> getPdfContent(Integer thesisId);

    void addPdfContent(String url);

    String getContent(Integer id);
}
