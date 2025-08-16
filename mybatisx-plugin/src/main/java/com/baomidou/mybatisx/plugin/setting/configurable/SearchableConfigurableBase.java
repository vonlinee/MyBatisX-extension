package com.baomidou.mybatisx.plugin.setting.configurable;

import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class SearchableConfigurableBase implements SearchableConfigurable {

  @Override
  public @NotNull
  @NonNls String getId() {
    return getClass().getName();
  }

  @Override
  public String getDisplayName() {
    return getClass().getSimpleName();
  }
}
