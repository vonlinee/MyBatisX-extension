package com.baomidou.mybatisx.feat.jpa.component;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Tx field.
 */
@Setter
@Getter
public class TxField {
  /**
   * 是不是主键
   */
  private Boolean primaryKey = false;
  /**
   * 定义字段的类全路径名称
   */
  private String className;
  /**
   * 提示名称
   */
  private String tipName;
  /**
   * 字段名称
   */
  private String fieldName;

  /**
   * 表的列名
   */
  private String columnName;
  /**
   * 字段类型
   */
  private String fieldType;
  /**
   * 字段jdbc类型
   */
  private String jdbcType;
}
