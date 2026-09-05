package com.baomidou.mybatisx.feat.mybatis.generator;

import com.baomidou.mybatisx.feat.mybatis.generator.dto.DomainInfo;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.GenerateConfig;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateContext;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateSettingDTO;
import com.baomidou.mybatisx.feat.mybatis.generator.setting.DefaultSettingsConfig;
import com.baomidou.mybatisx.plugin.setting.ProjectTemplatesSettings;
import com.baomidou.mybatisx.util.MessageNotification;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器弹窗
 */
public class MyBatisGeneratorDialog extends DialogWrapper {

  MyBatisGeneratorPane myBatisGeneratorPane = new MyBatisGeneratorPane();

  private final Action previousAction;

  private Project project;

  public MyBatisGeneratorDialog(@Nullable Project project) {
    super(project);
    this.setTitle("Generate Options");
    setOKButtonText("Next");
    setCancelButtonText("Cancel");

    previousAction = new DialogWrapperAction("Previous") {
      @Override
      protected void doAction(ActionEvent e) {
        myBatisGeneratorPane.switchToPreviousPage();
        previousAction.setEnabled(false);
        setOKButtonText("Next");
      }
    };
    // 默认禁用 上一个设置
    previousAction.setEnabled(false);
    // 初始化容器列表
    // 默认切换到第一页
    myBatisGeneratorPane.switchToFirstPage();
    super.init();
  }

  @Override
  protected void doOKAction() {
    if (myBatisGeneratorPane.isLastPage()) {
      super.doOKAction();
      return;
    }
    // 替换第二个 panel 的占位符
    DomainInfo domainInfo = myBatisGeneratorPane.buildDomainInfo();
    if (StringUtils.isEmpty(domainInfo.getModulePath())) {
      MessageNotification.warn("Please select module to generate files", "Generate File");
      return;
    }

    myBatisGeneratorPane.incrementPageNum();
    setOKButtonText("Finish");
    previousAction.setEnabled(true);

    ProjectTemplatesSettings templatesSettings = ProjectTemplatesSettings.getInstance(project);
    final TemplateContext templateContext = templatesSettings.getTemplateContext();
    Map<String, List<TemplateSettingDTO>> settingMap = templatesSettings.getTemplateSettingMap();
    if (settingMap.isEmpty()) {
      settingMap = DefaultSettingsConfig.defaultSettings();
    }
    myBatisGeneratorPane.fillData(project,
      domainInfo,
      templateContext.getTemplateName(),
      settingMap);

    myBatisGeneratorPane.nextPage();
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return myBatisGeneratorPane;
  }

  @NotNull
  @Override
  protected Action[] createActions() {
    return new Action[]{previousAction, getOKAction(), getCancelAction()};
  }

  public void fillData(Project project, List<TableInfo> tableElements) {
    this.project = project;
    myBatisGeneratorPane.fillData(project, tableElements);
  }

  public GenerateConfig determineGenerateConfig() {
    return myBatisGeneratorPane.determineGenerateConfig();
  }

  public void generateOnExist(List<TableInfo> tablesToGenerate) {
    // 模态窗口选择 OK, 生成相关代码
    if (this.getExitCode() == Messages.YES) {
      // 生成代码
      GenerateConfig generateConfig = this.determineGenerateConfig();
      if (!generateConfig.checkGenerate()) {
        return;
      }
      myBatisGeneratorPane.generateCode(project, tablesToGenerate, generateConfig);
    }
  }

}
