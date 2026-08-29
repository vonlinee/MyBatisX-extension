package com.baomidou.mybatisx.plugin.resultmap;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ResultMapTextGeneratorTest {

  @Test
  public void shouldGenerateConfiguredResultMap() {
    ResultMapGenerationOptions options = new ResultMapGenerationOptions();
    options.setResultMapId("blogResultMap");
    options.setResultType("com.example.Blog");

    SqlColumnModel id = new SqlColumnModel("blog_id");
    id.setPropertyName("id");
    id.setJavaType("java.lang.Long");
    id.setJdbcType("BIGINT");
    id.setId(true);
    options.getColumns().add(id);

    String text = ResultMapTextGenerator.generate(options);
    assertTrue(text.contains("<resultMap id=\"blogResultMap\" type=\"com.example.Blog\">"));
    assertTrue(text.contains("<id column=\"blog_id\" property=\"id\" javaType=\"java.lang.Long\" jdbcType=\"BIGINT\"/>"));
  }

  @Test
  public void shouldGenerateLombokJpaClass() {
    ResultMapGenerationOptions options = new ResultMapGenerationOptions();
    options.setClassName("Blog");
    options.setPackageName("com.example.domain");
    options.setTableName("t_blog");
    options.setGenerateJavaClass(true);
    options.setUseLombok(true);
    options.setJpaEntity(true);
    options.setJpaPackage("javax.persistence");

    SqlColumnModel id = new SqlColumnModel("id");
    id.setPropertyName("id");
    id.setJavaType("java.lang.Long");
    id.setId(true);
    id.setAutoIncrement(true);
    options.getColumns().add(id);

    String text = JavaClassTextGenerator.generate(options);
    assertTrue(text.contains("import javax.persistence.Entity;"));
    assertTrue(text.contains("@Entity"));
    assertTrue(text.contains("@GeneratedValue(strategy = GenerationType.AUTO)"));
    assertTrue(text.contains("@Data"));
  }
}
