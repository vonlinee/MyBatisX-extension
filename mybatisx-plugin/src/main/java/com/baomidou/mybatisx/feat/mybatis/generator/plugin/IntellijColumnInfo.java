package com.baomidou.mybatisx.feat.mybatis.generator.plugin;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IntellijColumnInfo {
  private String name;
  private int dataType;
  private boolean generatedColumn;
  private boolean autoIncrement;
  private int size;
  private int decimalDigits;
  private String remarks;
  private String columnDefaultValue;
  private Boolean nullable;
  private short keySeq;

}
