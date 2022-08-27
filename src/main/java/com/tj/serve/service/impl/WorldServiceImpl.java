package com.tj.serve.service.impl;

import com.tj.serve.domain.World;
import com.tj.serve.mapper.WorldMapper;
import com.tj.serve.service.WorldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-03-20
 */
@Service
public class WorldServiceImpl extends ServiceImpl<WorldMapper, World> implements WorldService {

}
