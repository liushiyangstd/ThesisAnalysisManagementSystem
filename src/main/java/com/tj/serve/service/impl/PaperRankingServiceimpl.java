package com.tj.serve.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tj.serve.domain.PaperRanking;
import com.tj.serve.mapper.PaperRankingMapper;
import com.tj.serve.service.PaperRankingService;
import com.tj.serve.utils.R;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class PaperRankingServiceimpl extends ServiceImpl<PaperRankingMapper, PaperRanking> implements PaperRankingService {
    @Override
    public List<PaperRanking> getPaperTopId() {
        List<PaperRanking> Paper_list = baseMapper.selectList(null);

        PaperRanking temp = null;

        int length = Paper_list.size();
        /*冒泡排序*/
        for (int i = 0; i < length - 1; i++) {

            for (int j = 0; j < length- 1 - i; j++)
            {
                if (Paper_list.get(j+1).getCount() > Paper_list.get(j).getCount()) {
                    Collections.swap(Paper_list,j,j+1);;
//                    temp = Paper_list.get(j+1);
//                    Paper_list.get(j+1) = Paper_list.get(j);
//                    Paper_list.get(j) = temp;

                }
            }
        }
        List<PaperRanking> ans_paper_list = new ArrayList<>();
        if (Paper_list.size() > 10) {
            for (int i = 0; i < 10; i++) {
                ans_paper_list.add(Paper_list.get(i));
            }
        }else{
            ans_paper_list = Paper_list;
        }

        return ans_paper_list;
    }
}
