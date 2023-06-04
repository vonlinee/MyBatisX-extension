package template;

import org.apache.ibatis.annotations.Param;
import template.Blog;

import java.util.Collection;
import java.util.List;

public interface TipMapper {
    List<Blog> selectByIdIn(@Param("idList") Collection<Long> idList);
}
