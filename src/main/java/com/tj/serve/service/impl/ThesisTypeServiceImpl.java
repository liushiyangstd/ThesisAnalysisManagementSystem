package com.tj.serve.service.impl;

import com.tj.serve.domain.ThesisType;
import com.tj.serve.mapper.ThesisTypeMapper;
import com.tj.serve.service.ThesisTypeService;
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
public class ThesisTypeServiceImpl extends ServiceImpl<ThesisTypeMapper, ThesisType> implements ThesisTypeService {

    @Override
    public List<ThesisType> getAllThesisType() {
        List<ThesisType> typeList = baseMapper.selectList(null);
        return typeList;
    }
}
