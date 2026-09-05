package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.plugin.ui.components.TemplateSettingPane;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 模板配置
 */
public class TemplateConfigurable implements SearchableConfigurable {

  private final TemplateSettingPane rootPanel;

  public TemplateConfigurable() {
    rootPanel = new TemplateSettingPane();
  }

  @Override
  public @NotNull String getId() {
    return getClass().getName();
  }

  @Override
  public String getDisplayName() {
    return "Template";
  }

  @Override
  public @Nullable JComponent createComponent() {
    return this.rootPanel;
  }

  @Override
  public boolean isModified() {
    return rootPanel.hasChanged();
  }

  @Override
  public void apply() throws ConfigurationException {
    try {
      rootPanel.apply();
    } catch (Exception exception) {
      throw new ConfigurationException(exception.getMessage(), "Unable to Save Templates");
    }
  }

  @Override
  public void reset() {
    rootPanel.reset();
  }
}
