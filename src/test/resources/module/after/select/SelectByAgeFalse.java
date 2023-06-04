package template;

import template.Blog;

import java.util.List;

public interface TipMapper {
    List<Blog> selectByAgeFalse();
}
