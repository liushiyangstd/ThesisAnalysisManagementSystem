package com.tj.serve.service.impl;

import com.tj.serve.domain.ThesisDiscipline;
import com.tj.serve.mapper.ThesisDisciplineMapper;
import com.tj.serve.service.ThesisDisciplineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cj
 * @since 2022-02-12
 */
@Service
public class ThesisDisciplineServiceImpl extends ServiceImpl<ThesisDisciplineMapper, ThesisDiscipline> implements ThesisDisciplineService {

    @Override
    public List<ThesisDiscipline> getAllThesisDiscipline() {
        List<ThesisDiscipline> disciplineList = baseMapper.selectList(null);
        return disciplineList;
    }
}
