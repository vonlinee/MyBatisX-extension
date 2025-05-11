package com.baomidou.mybatisx.feat.generate.plugin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IntellijTableInfo {
  private String tableName;
  private String databaseType;
  private String tableRemark;
  private String tableType;
  private List<IntellijColumnInfo> columnInfos;
  private List<IntellijColumnInfo> primaryKeyColumns;

  public IntellijTableInfo() {
  }

}
