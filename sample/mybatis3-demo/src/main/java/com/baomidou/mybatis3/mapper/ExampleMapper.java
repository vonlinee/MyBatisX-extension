package com.baomidou.mybatis3.mapper;

import com.baomidou.mybatis3.domain.BlogTitleContentDTO;
import com.baomidou.mybatis3.domain.BlogAgeContentDTO;
import com.baomidou.mybatis3.domain.BlogIdTitleDTO;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatis3.domain.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;


public interface ExampleMapper extends BaseMapper<Blog> {
    // 测试默认检测不爆红
    default void checkDefaultMethod() {
    }

    int updateIdAndContentByAllFields(@Param("id") Long id, @Param("content") String content);


    List<BlogIdTitleDTO> selectIdAndTitleById(@Param("id") Long id);

    Blog selectOneById(@Param("id") Long id);

    List<BlogAgeContentDTO> selectAgeAndContentById(@Param("id") Long id);

    Blog insertAll(Blog blog);


    List<BlogTitleContentDTO> selectTitleAndContentByTitle(@Param("title") String title);

    //    insertAll
//        updateAgeAndContentByMoneyAndCreateTime
    List<BlogTitleContentDTO> selectTitleAndContentById(@Param("id") Long id);


    IPage<Blog> selectByTitle(Page<Blog> page, @Param("title") String title);

    @MapKey("id")
    Map<String, Blog> selectCreateTimeByAge(@Param("blog") Blog xxxx, @Param("age") int age);

}
