package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.plugin.setting.OtherSetting;
import com.baomidou.mybatisx.plugin.ui.MyBatisGeneratorSettingPanel;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * MyBatis 生成器配置
 */
public class MyBatisGeneratorConfigurable extends SearchableConfigurableBase {

  MyBatisGeneratorSettingPanel panel;
  OtherSetting otherSetting;

  public MyBatisGeneratorConfigurable() {
    panel = new MyBatisGeneratorSettingPanel();
    this.otherSetting = OtherSetting.getInstance();
  }

  @Override
  public @Nullable JComponent createComponent() {
    return panel.getRootPanel();
  }

  @Override
  public boolean isModified() {
    OtherSetting.State myProperties = otherSetting.getProperties();
    if (!StringUtils.equals(myProperties.getIdAnnotation(), panel.getIdText().getText())) {
      return true;
    }
    if (!StringUtils.equals(myProperties.getTableAnnotation(), panel.getTableText().getText())) {
      return true;
    }
    if (!StringUtils.equals(myProperties.getTableAnnotationProperty(), panel.getTablePropertyText().getText())) {
      return true;
    }
    return !StringUtils.equals(myProperties.getCommentAnnotation(), panel.getCommendText().getText());
  }

  @Override
  public void apply() throws ConfigurationException {
    OtherSetting.State myProperties = otherSetting.myProperties;

    myProperties.setIdAnnotation(panel.getIdText().getText());
    myProperties.setTableAnnotation(panel.getTableText().getText());
    myProperties.setTableAnnotationProperty(panel.getTablePropertyText().getText());
    myProperties.setCommentAnnotation(panel.getCommendText().getText());
  }

  @Override
  public void reset() {
    OtherSetting.State myProperties = otherSetting.myProperties;
    panel.getIdText().setText(myProperties.getIdAnnotation());
    panel.getTableText().setText(myProperties.getTableAnnotation());
    panel.getTablePropertyText().setText(myProperties.getTableAnnotationProperty());
    panel.getCommendText().setText(myProperties.getCommentAnnotation());
  }
}
