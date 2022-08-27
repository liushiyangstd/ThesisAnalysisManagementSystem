package com.tj.serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.serve.domain.PaperRanking;
import com.tj.serve.domain.Thesis;
import com.tj.serve.mapper.ThesisMapper;
import com.tj.serve.service.PaperRankingAnsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaperRankingAnsServiceImpl extends ServiceImpl<ThesisMapper, Thesis> implements PaperRankingAnsService {
    @Override
    public List<Thesis> getTop(List<PaperRanking> p) {
        List<Thesis> ans = new ArrayList<>();


        //判断paper_count数据库中是否有10条数据
        if (p.size() > 10) {
            for (int i = 0; i < 10; i++) {
                ans.add(baseMapper.selectById(p.get(i).getPid()));
            }
        }else{
            for(int i=0;i<p.size();i++){
                ans.add(baseMapper.selectById(p.get(i).getPid()));
            }
        }
        return ans;
    }
}
