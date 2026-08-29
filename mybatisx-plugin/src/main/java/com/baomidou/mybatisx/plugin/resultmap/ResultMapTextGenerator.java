package com.baomidou.mybatisx.plugin.resultmap;

public final class ResultMapTextGenerator {

  private ResultMapTextGenerator() {
  }

  public static String generate(ResultMapGenerationOptions options) {
    StringBuilder text = new StringBuilder();
    text.append("<resultMap id=\"")
      .append(xml(options.getResultMapId()))
      .append("\"");
    String type = options.isGenerateJavaClass() ? qualifiedClassName(options) : options.getResultType();
    if (type != null && !type.isBlank()) {
      text.append(" type=\"").append(xml(type)).append("\"");
    }
    text.append(">\n");
    for (SqlColumnModel column : options.getColumns()) {
      if (column.isIgnored() || column.getColumnName() == null || column.getPropertyName() == null) {
        continue;
      }
      text.append("  <").append(column.isId() ? "id" : "result")
        .append(" column=\"").append(xml(column.getColumnName()))
        .append("\" property=\"").append(xml(column.getPropertyName())).append("\"");
      if (column.isJavaTypeExplicit() && column.getJavaType() != null && !column.getJavaType().isBlank()) {
        text.append(" javaType=\"").append(xml(column.getJavaType())).append("\"");
      }
      if (column.getJdbcType() != null && !column.getJdbcType().isBlank()) {
        text.append(" jdbcType=\"").append(xml(column.getJdbcType())).append("\"");
      }
      text.append("/>\n");
    }
    text.append("</resultMap>");
    return text.toString();
  }

  static String qualifiedClassName(ResultMapGenerationOptions options) {
    String packageName = options.getPackageName();
    if (packageName == null || packageName.isBlank()) {
      return options.getClassName();
    }
    return packageName + "." + options.getClassName();
  }

  private static String xml(String value) {
    return value == null ? "" : value.replace("&", "&amp;")
      .replace("\"", "&quot;")
      .replace("<", "&lt;")
      .replace(">", "&gt;");
  }
}
