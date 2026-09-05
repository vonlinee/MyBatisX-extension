package com.baomidou.mybatisx.feat.mybatis.generator;

import com.intellij.openapi.util.NlsSafe;

/**
 * @see com.intellij.database.model.RawConnectionConfig
 */
public interface ConnectionConfig {

  @NlsSafe
  String getName();

  String getDriverClass();

  String getUrl();
}
