package com.tj.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tj.serve.domain.PaperRanking;

import java.util.List;

public interface PaperRankingService extends IService<PaperRanking> {

    List<PaperRanking> getPaperTopId();
}
