package com.baomidou.mybatisx.plugin.provider;

import com.intellij.serviceContainer.BaseKeyedLazyInstance;
import com.intellij.spring.toolWindow.SpringToolWindowContentProvider;
import org.jetbrains.annotations.Nullable;

public class MyBatisToolWindowContent extends BaseKeyedLazyInstance<SpringToolWindowContentProvider> {
  @Override
  protected @Nullable String getImplementationClassName() {
    return null;
  }
}
