package template;

import org.apache.ibatis.annotations.Param;
import template.Blog;

import java.util.List;

public interface TipMapper {
    List<Blog> selectByAgeGreaterThanEqual(@Param("age") Integer age);
}
