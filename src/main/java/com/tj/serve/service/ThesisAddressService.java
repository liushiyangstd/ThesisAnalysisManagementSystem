package com.tj.serve.service;

import com.tj.serve.domain.ThesisAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.serve.vo.ThesisAddressPageVo;
import com.tj.serve.vo.ThesisAddressVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lar
 * @since 2022-02-12
 */
public interface ThesisAddressService extends IService<ThesisAddress> {

    String uploadFileOSS(MultipartFile file);

    void addThesisAddress(String url);

    ThesisAddressPageVo getPageAddress(Long current, Long limit, ThesisAddressVo thesisAddressVo);

    void removeDataById(String thesisTitle);
}
