package com.baomidou.mybatisx.feat.mybatis;

public enum SqlStatementType {
  INSERT,
  DELETE,
  SELECT,
  UPDATE,
  /**
   * 删除表
   */
  DROP_TABLE,
  /**
   * 创建表
   */
  CREATE,
  /**
   * 修改表
   */
  ALTER;

  public static boolean isDml(SqlStatementType sst) {
    if (sst == null) {
      return false;
    }
    return sst == INSERT || sst == SELECT || sst == UPDATE || sst == DELETE;
  }

  public static boolean isDDL(SqlStatementType sst) {
    return sst == DROP_TABLE || sst == CREATE || sst == ALTER;
  }

  public static SqlStatementType findByName(String name) {
    if (name == null || name.isEmpty()) {
      return null;
    }
    for (SqlStatementType item : values()) {
      if (item.name().equalsIgnoreCase(name)) {
        return item;
      }
    }
    return null;
  }
}
