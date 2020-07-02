package com.zxw.modules.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxw.modules.domain.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zxw
 * @date 2020/5/29 14:38
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
