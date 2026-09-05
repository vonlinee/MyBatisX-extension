package com.baomidou.mybatisx.feat.mybatis.generator;

import com.intellij.database.model.RawConnectionConfig;

public class PsiConnectionConfig implements ConnectionConfig {

  private final RawConnectionConfig rawConnectionConfig;

  public PsiConnectionConfig(RawConnectionConfig rawConnectionConfig) {
    this.rawConnectionConfig = rawConnectionConfig;
  }

  @Override
  public String getName() {
    return rawConnectionConfig.getName();
  }

  @Override
  public String getDriverClass() {
    return rawConnectionConfig.getDriverClass();
  }

  @Override
  public String getUrl() {
    return rawConnectionConfig.getUrl();
  }
}
