package com.tj.serve.service;

import com.tj.serve.domain.PaperRanking;
import com.tj.serve.domain.Thesis;

import java.util.List;

public interface PaperRankingAnsService {
    List<Thesis> getTop(List<PaperRanking> p);
}
