package com.baomidou.mybatisx.feat.mybatis.generator.dto;

import lombok.Getter;
import lombok.Setter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * 字段信息
 */
@Setter
@Getter
public class FieldInfo {
  /**
   * 字段名称
   */
  private String fieldName;
  /**
   * 列名称
   */
  private String columnName;
  /**
   * 列实际限制的长度
   */
  private int columnLength;
  /**
   * 列的精度
   */
  private int columnScale;
  /**
   * java 字段类型是不是数组类型, 用于排除导入
   */
  private boolean columnIsArray;
  /**
   * java类型短名称
   */
  private String shortTypeName;
  /**
   * java类型的长名称, 用于导入
   */
  private String fullTypeName;
  /**
   * 字段注释
   */
  private String remark;
  /**
   * jdbcType 的值
   */
  private String jdbcType;

  /**
   * 是否允许为空
   */
  private boolean nullable;
  /**
   * 是否自增
   */
  private boolean autoIncrement;

  public static FieldInfo build(IntrospectedColumn introspectedColumn) {
    FieldInfo fieldInfo = new FieldInfo();
    fieldInfo.fieldName = introspectedColumn.getJavaProperty();
    fieldInfo.columnName = introspectedColumn.getActualColumnName();
    fieldInfo.jdbcType = introspectedColumn.getJdbcTypeName();
    fieldInfo.columnLength = introspectedColumn.getLength();
    fieldInfo.columnScale = introspectedColumn.getScale();
    FullyQualifiedJavaType fullyQualifiedJavaType = introspectedColumn.getFullyQualifiedJavaType();
    fieldInfo.shortTypeName = fullyQualifiedJavaType.getShortName();
    fieldInfo.fullTypeName = fullyQualifiedJavaType.getFullyQualifiedName();
    fieldInfo.columnIsArray = fullyQualifiedJavaType.isArray();
    fieldInfo.remark = introspectedColumn.getRemarks();
    fieldInfo.nullable = introspectedColumn.isNullable();
    fieldInfo.autoIncrement = introspectedColumn.isAutoIncrement();
    return fieldInfo;
  }

}
