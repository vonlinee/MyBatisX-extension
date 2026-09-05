package com.baomidou.mybatisx.feat.mybatis.generator;

import com.baomidou.mybatisx.feat.mybatis.generator.dto.DefaultGenerateConfig;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.DomainInfo;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.GenerateConfig;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateContext;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateSettingDTO;
import com.baomidou.mybatisx.feat.mybatis.generator.setting.DefaultSettingsConfig;
import com.baomidou.mybatisx.plugin.components.BorderPane;
import com.baomidou.mybatisx.plugin.setting.TemplatesSettings;
import com.baomidou.mybatisx.plugin.ui.CodeGenerateUI;
import com.baomidou.mybatisx.plugin.ui.TablePreviewUI;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.MessageNotification;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器弹窗
 */
public class ClassGenerateDialogWrapper extends DialogWrapper {

  private final CodeGenerateUI codeGenerateUI = new CodeGenerateUI();

  private final TablePreviewUI tablePreviewUI = new TablePreviewUI();

  private final BorderPane rootPanel = new BorderPane();
  private final Action previousAction;
  private int page = 0;
  private int lastPage = 1;
  private Project project;
  private GenerateConfig generateConfig;

  public ClassGenerateDialogWrapper(@Nullable Project project) {
    super(project);
    this.setTitle("Generate Options");
    setOKButtonText("Next");
    setCancelButtonText("Cancel");

    previousAction = new DialogWrapperAction("Previous") {
      @Override
      protected void doAction(ActionEvent e) {
        switchPage(page = page - 1);
        previousAction.setEnabled(false);
        setOKButtonText("Next");
      }
    };
    // 默认禁用 上一个设置
    previousAction.setEnabled(false);
    // 初始化容器列表
    // 默认切换到第一页
    rootPanel.setCenter(tablePreviewUI.getRootPanel());
    super.init();
  }

  @Override
  protected void doOKAction() {
    if (page == lastPage) {
      super.doOKAction();
      return;
    }
    // 替换第二个panel的占位符
    DomainInfo domainInfo = tablePreviewUI.buildDomainInfo();
    if (StringUtils.isEmpty(domainInfo.getModulePath())) {
      MessageNotification.warn("Please select module to generate files", "Generate File");
      return;
    }
    page = page + 1;
    setOKButtonText("Finish");
    previousAction.setEnabled(true);

    TemplatesSettings templatesSettings = TemplatesSettings.getInstance(project);
    final TemplateContext templateContext = templatesSettings.getTemplateContext();
    Map<String, List<TemplateSettingDTO>> settingMap = templatesSettings.getTemplateSettingMap();
    if (settingMap.isEmpty()) {
      settingMap = DefaultSettingsConfig.defaultSettings();
    }
    codeGenerateUI.fillData(project,
      generateConfig,
      domainInfo,
      templateContext.getTemplateName(),
      settingMap);

    switchPage(page);

  }

  private void switchPage(int newPage) {
    if (newPage == 0) {
      rootPanel.remove(codeGenerateUI.getRootPanel());
      rootPanel.setCenter(tablePreviewUI.getRootPanel());
    } else if (newPage == 1) {
      rootPanel.remove(tablePreviewUI.getRootPanel());
      rootPanel.setCenter(codeGenerateUI.getRootPanel());
    }
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return rootPanel;
  }

  @Override
  protected Action[] createActions() {
    return new Action[]{previousAction, getOKAction(), getCancelAction()};
  }

  public void fillData(Project project, List<TableInfo> tableElements) {
    this.project = project;
    TemplatesSettings templatesSettings = TemplatesSettings.getInstance(project);
    TemplateContext templateContext = templatesSettings.getTemplateContext();
    generateConfig = templateContext.getGenerateConfig();
    if (generateConfig == null) {
      generateConfig = new DefaultGenerateConfig(templateContext);
    }

    if (CollectionUtils.isEmpty(templatesSettings.getTemplateSettingMap())) {
      templateContext.setTemplateSettingMap(DefaultSettingsConfig.defaultSettings());
    }

    tablePreviewUI.fillData(project, tableElements, generateConfig);
  }

  public GenerateConfig determineGenerateConfig() {
    GenerateConfig generateConfig = new GenerateConfig();
    codeGenerateUI.refreshGenerateConfig(generateConfig);
    tablePreviewUI.refreshGenerateConfig(generateConfig);
    return generateConfig;
  }
}
