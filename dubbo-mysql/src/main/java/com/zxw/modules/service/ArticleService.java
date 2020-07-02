package com.zxw.modules.service;

import com.zxw.modules.domain.Article;

/**
 * @author zxw
 * @date 2020/5/29 14:36
 */
public interface ArticleService {
    void insert(Article article);

    void selectAll();

    Article uncommited();

    Article commited();

    Article repeable();

}
