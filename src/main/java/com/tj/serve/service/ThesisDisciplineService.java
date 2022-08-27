package com.tj.serve.service;

import com.tj.serve.domain.ThesisDiscipline;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lar
 * @since 2022-02-12
 */
public interface ThesisDisciplineService extends IService<ThesisDiscipline> {

    List<ThesisDiscipline> getAllThesisDiscipline();
}
