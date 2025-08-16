package com.baomidou.mybatisx.feat.mybatis.generator;

import com.baomidou.mybatisx.feat.mybatis.generator.dto.DefaultGenerateConfig;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.DomainInfo;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.GenerateConfig;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateContext;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateSettingDTO;
import com.baomidou.mybatisx.plugin.setting.TemplatesSettings;
import com.baomidou.mybatisx.plugin.ui.CodeGenerateUI;
import com.baomidou.mybatisx.plugin.ui.TablePreviewUI;
import com.baomidou.mybatisx.util.MessageNotification;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器配置
 */
@Slf4j
public class ClassGenerateDialogWrapper extends DialogWrapper {

  private final CodeGenerateUI codeGenerateUI = new CodeGenerateUI();

  private final TablePreviewUI tablePreviewUI = new TablePreviewUI();

  private final JPanel rootPanel = new JPanel();
  private final Action previousAction;
  private List<JPanel> containerPanelList;
  private int page = 0;
  private int lastPage = 1;
  private Project project;
  private List<DbTable> tableElements;
  private GenerateConfig generateConfig;

  public ClassGenerateDialogWrapper(@Nullable Project project) {
    super(project);
    this.setTitle("Generate Options");
    setOKButtonText("Next");
    setCancelButtonText("Cancel");

    previousAction = new DialogWrapperAction("Previous") {
      @Override
      protected void doAction(ActionEvent e) {
        page = page - 1;
        switchPage(page);
        previousAction.setEnabled(false);
        setOKButtonText("Next");
      }
    };
    // 默认禁用 上一个设置
    previousAction.setEnabled(false);
    // 初始化容器列表
    containerPanelList = new ArrayList<>();
    containerPanelList.add(tablePreviewUI.getRootPanel());
    containerPanelList.add(codeGenerateUI.getRootPanel());
    // 默认切换到第一页
    switchPage(0);
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
      MessageNotification.showMessageDialog("Please select module to generate files", "Generate File", Messages.getWarningIcon());
      return;
    }
    page = page + 1;
    setOKButtonText("Finish");
    previousAction.setEnabled(true);

    TemplatesSettings templatesSettings = TemplatesSettings.getInstance(project);
    final TemplateContext templateContext = templatesSettings.getTemplateContext();
    final Map<String, List<TemplateSettingDTO>> settingMap = templatesSettings.getTemplateSettingMap();
    if (settingMap.isEmpty()) {
      throw new RuntimeException("无法获取模板");
    }
    codeGenerateUI.fillData(project,
      generateConfig,
      domainInfo,
      templateContext.getTemplateName(),
      settingMap);
    switchPage(page);
  }

  private void switchPage(int newPage) {
    rootPanel.removeAll();
    JPanel comp = containerPanelList.get(newPage);
    rootPanel.add(comp);
    rootPanel.repaint();//刷新页面，重绘面板
    rootPanel.validate();//使重绘的面板确认生效
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return rootPanel;
  }

  @Override
  protected Action @NotNull [] createActions() {
    return new Action[]{previousAction, getOKAction(), getCancelAction()};
  }

  public void fillData(Project project, List<DbTable> tableElements) {
    this.project = project;
    this.tableElements = tableElements;
    TemplatesSettings templatesSettings = TemplatesSettings.getInstance(project);
    TemplateContext templateContext = templatesSettings.getTemplateContext();
    generateConfig = templateContext.getGenerateConfig();
    if (generateConfig == null) {
      generateConfig = new DefaultGenerateConfig(templateContext);
    }

    Map<String, List<TemplateSettingDTO>> settingMap = templatesSettings.getTemplateSettingMap();
    if (settingMap.isEmpty()) {
      throw new RuntimeException("无法获取模板");
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
