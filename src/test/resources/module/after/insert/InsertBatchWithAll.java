package template;

import org.apache.ibatis.annotations.Param;
import template.Blog;

import java.util.Collection;

public interface TipMapper {
    int insertBatchWithAll(@Param("blogCollection") Collection<Blog> blogCollection);
}
