package com.baomidou.mybatisx.plugin.resultmap;

import org.jetbrains.annotations.NotNull;

/**
 * A column returned by a mapper select or declared by a resultMap.
 */
public final class SqlColumnModel {

  private String columnName;
  private String propertyName;
  private String javaType;
  private boolean javaTypeExplicit;
  private String jdbcType;
  private boolean id;
  private boolean ignored;
  private boolean autoIncrement;
  private String warning;

  public SqlColumnModel(@NotNull String columnName) {
    this.columnName = columnName;
    this.propertyName = NamingUtils.toPropertyName(columnName);
    this.javaType = "java.lang.String";
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getJavaType() {
    return javaType;
  }

  public void setJavaType(String javaType) {
    this.javaType = javaType;
    this.javaTypeExplicit = true;
  }

  public void setInferredJavaType(String javaType) {
    this.javaType = javaType;
    this.javaTypeExplicit = false;
  }

  public boolean isJavaTypeExplicit() {
    return javaTypeExplicit;
  }

  public String getJdbcType() {
    return jdbcType;
  }

  public void setJdbcType(String jdbcType) {
    this.jdbcType = jdbcType;
  }

  public boolean isId() {
    return id;
  }

  public void setId(boolean id) {
    this.id = id;
  }

  public boolean isIgnored() {
    return ignored;
  }

  public void setIgnored(boolean ignored) {
    this.ignored = ignored;
  }

  public boolean isAutoIncrement() {
    return autoIncrement;
  }

  public void setAutoIncrement(boolean autoIncrement) {
    this.autoIncrement = autoIncrement;
  }

  public String getWarning() {
    return warning;
  }

  public void setWarning(String warning) {
    this.warning = warning;
  }
}
