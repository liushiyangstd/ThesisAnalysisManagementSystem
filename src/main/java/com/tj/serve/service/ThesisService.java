package com.tj.serve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tj.serve.domain.Thesis;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.serve.vo.HotQueryVo;
import com.tj.serve.vo.HotVolVo;
import com.tj.serve.vo.ThesisQueryVo;
import com.tj.serve.vo.ThesisVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lar
 * @since 2022-02-12
 */
public interface ThesisService extends IService<Thesis> {

    void addThesis(ThesisVo thesis);

    Page<Thesis> getPageThesis(Long current, Long limit, ThesisQueryVo thesisQueryVo);

    List<HotVolVo> getHotVol(HotQueryVo hotQueryVo);

    List<Thesis> getAllThesis();

    List<HotVolVo> getCateHotVol(HotQueryVo hotQueryVo);
}
