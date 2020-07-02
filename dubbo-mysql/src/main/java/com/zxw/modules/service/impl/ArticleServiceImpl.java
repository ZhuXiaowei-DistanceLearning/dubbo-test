package com.zxw.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.modules.domain.Article;
import com.zxw.modules.mapper.ArticleMapper;
import com.zxw.modules.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zxw
 * @date 2020/5/29 14:36
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;


    public void insert(Article article) {
        articleMapper.insert(article);
    }

    public void selectAll() {

    }
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Article uncommited() {
        Article article = articleMapper.selectById(1);
        System.out.println("****脏读****");
        Article article1 = articleMapper.selectById(1);
        List<Article> list = articleMapper.selectList(null);
        List<Article> list2 = articleMapper.selectList(null);
        return article1;
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Article commited() {
        Article article = articleMapper.selectById(1);
        System.out.println("****脏读****");
        Article article1 = articleMapper.selectById(1);
        List<Article> list = articleMapper.selectList(null);
        List<Article> list2 = articleMapper.selectList(null);
        return null;
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Article repeable() {
        Article article = articleMapper.selectById(1);
        System.out.println("****脏读****");
        Article article1 = articleMapper.selectById(1);
        List<Article> list = articleMapper.selectList(null);
        List<Article> list2 = articleMapper.selectList(null);
        return null;
    }

}
