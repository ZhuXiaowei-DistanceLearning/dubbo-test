package com.zxw;

import com.zxw.modules.domain.Article;
import com.zxw.modules.mapper.ArticleMapper;
import com.zxw.modules.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.UUID;

/**
 * @author zxw
 * @date 2020/5/29 14:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void test1() {
        for (int i = 1623999; i < 10000000; i++) {
            Article article = new Article();
            article.setRemark("备注"+i);
            article.setText("序号:" + UUID.randomUUID());
            article.setUuid(new Random().nextInt(Integer.MAX_VALUE));
            articleService.insert(article);
        }
    }

    @Test
    public void uncommited(){
        articleService.uncommited();
    }
    @Test
    public void commited(){
        articleService.commited();
    }
    @Test
    public void repetable(){
        articleService.repeable();
    }
}
