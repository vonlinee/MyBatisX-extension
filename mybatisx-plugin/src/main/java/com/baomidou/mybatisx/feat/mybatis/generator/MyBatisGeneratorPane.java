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
import com.intellij.openapi.project.Project;

import java.util.List;
import java.util.Map;

public class MyBatisGeneratorPane extends BorderPane {

  private int page = 0;
  private GenerateConfig generateConfig;

  private final CodeGenerateUI codeGenerateUI = new CodeGenerateUI();

  private final TablePreviewUI tablePreviewUI = new TablePreviewUI();

  public MyBatisGeneratorPane() {
    switchPage(0);
  }

  public void nextPage() {
    this.switchPage(page);
  }

  public void switchPage(int newPage) {
    if (newPage == 0) {
      this.remove(codeGenerateUI.getRootPanel());
      this.setCenter(tablePreviewUI.getRootPanel());
    } else if (newPage == 1) {
      this.remove(tablePreviewUI.getRootPanel());
      this.setCenter(codeGenerateUI.getRootPanel());
    }
  }

  public boolean isLastPage() {
    return page == 1;
  }

  public void switchToFirstPage() {
    this.setCenter(tablePreviewUI.getRootPanel());
  }

  public DomainInfo buildDomainInfo() {
    return tablePreviewUI.buildDomainInfo();
  }

  public void fillData(Project project, List<TableInfo> dbTables) {
    TemplatesSettings templatesSettings = TemplatesSettings.getInstance(project);
    TemplateContext templateContext = templatesSettings.getTemplateContext();
    generateConfig = templateContext.getGenerateConfig();
    if (generateConfig == null) {
      generateConfig = new DefaultGenerateConfig(templateContext);
    }

    if (CollectionUtils.isEmpty(templatesSettings.getTemplateSettingMap())) {
      templateContext.setTemplateSettingMap(DefaultSettingsConfig.defaultSettings());
    }

    tablePreviewUI.fillData(project, dbTables, generateConfig);
  }

  public void fillData(Project project, DomainInfo domainInfo, String templateName, Map<String, List<TemplateSettingDTO>> settingMap) {
    codeGenerateUI.fillData(project, generateConfig, domainInfo, templateName, settingMap);
  }

  public GenerateConfig determineGenerateConfig() {
    GenerateConfig generateConfig = new GenerateConfig();
    codeGenerateUI.refreshGenerateConfig(generateConfig);
    tablePreviewUI.refreshGenerateConfig(generateConfig);
    return generateConfig;
  }

  public void switchToPreviousPage() {
    this.switchPage(page = page - 1);
  }

  public void incrementPageNum() {
    page = page + 1;
  }
}
