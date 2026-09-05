package com.baomidou.mybatisx.feat.mybatis.generator;

import java.util.Iterator;

public interface TableInfo {

  String getTableName();

  ConnectionConfig getConnectionConfig();

  String getComment();

  String getTableType();

  ColumnInfo[] getColumns();

  Iterator<String> getPrimaryKeyColumnNames();
}
