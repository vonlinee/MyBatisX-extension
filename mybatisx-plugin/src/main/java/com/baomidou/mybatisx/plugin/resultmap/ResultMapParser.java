package com.baomidou.mybatisx.plugin.resultmap;

import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class ResultMapParser {

  private ResultMapParser() {
  }

  @NotNull
  static List<SqlColumnModel> parse(@NotNull XmlTag resultMap) {
    List<SqlColumnModel> columns = new ArrayList<>();
    for (XmlTag child : resultMap.getSubTags()) {
      String name = child.getName();
      if (!"id".equals(name) && !"result".equals(name)) {
        continue;
      }
      String column = child.getAttributeValue("column");
      String property = child.getAttributeValue("property");
      if (column == null || column.isBlank()) {
        continue;
      }
      SqlColumnModel model = new SqlColumnModel(column);
      if (property != null && !property.isBlank()) {
        model.setPropertyName(property);
      }
      String javaType = child.getAttributeValue("javaType");
      if (javaType != null && !javaType.isBlank()) {
        model.setJavaType(javaType);
      } else {
        model.setInferredJavaType("java.lang.String");
      }
      model.setJdbcType(child.getAttributeValue("jdbcType"));
      model.setId("id".equals(name));
      columns.add(model);
    }
    return columns;
  }
}
