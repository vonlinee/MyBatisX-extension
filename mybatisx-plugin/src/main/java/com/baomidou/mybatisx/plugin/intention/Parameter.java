package com.baomidou.mybatisx.plugin.intention;

import lombok.Builder;
import lombok.Data;

/**
 * @author Clinton Begin
 */
@Data
@Builder
public class Parameter {

  private static final Object UNSET = new Object();

  private String property;
  private String mode;
  private String javaType;
  private String jdbcType;
  private String typeHandlerAlias;
  private Integer numericScale;
  private String resultMapId;
  private String jdbcTypeName;
  private String expression;
}
