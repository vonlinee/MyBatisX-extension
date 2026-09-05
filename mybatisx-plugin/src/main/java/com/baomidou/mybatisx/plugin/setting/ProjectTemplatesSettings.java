package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateContext;
import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateSettingDTO;
import com.baomidou.mybatisx.feat.mybatis.generator.setting.DefaultSettingsConfig;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.MyBatisXPlugin;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目级别的模板设置
 */
@Service
@State(name = "TemplatesSettings", storages = {
  // 存放在.idea/mybatisx.xml
  @Storage(value = MyBatisXPlugin.STORAGE_FILE_ROOT_NAME + "-templates" + MyBatisXPlugin.STORAGE_FILE_EXTENSION)
})
public final class ProjectTemplatesSettings implements PersistentStateComponent<ProjectTemplatesSettings.State> {

  private final State state = new State();
  @Setter
  @Getter
  private TemplateContext templateContext;

  public static ProjectTemplatesSettings getInstance() {
    return IntellijSDK.getService(ProjectTemplatesSettings.class);
  }

  public static Map<String, List<TemplateSettingDTO>> getAllTemplates() {
    Map<String, List<TemplateSettingDTO>> templates = getInstance().getTemplates();
    if (templates == null) {
      templates = Collections.emptyMap();
    }
    return templates;
  }

  /**
   * @param project 为null，则获取全局的配置，不为null，则获取当前项目的模板配置
   * @return 模板配置
   */
  @NotNull
  public static ProjectTemplatesSettings getInstance(@Nullable Project project) {
    if (project == null) {
      return IntellijSDK.getService(ProjectTemplatesSettings.class);
    }
    ProjectTemplatesSettings service = IntellijSDK.getService(ProjectTemplatesSettings.class, project);
    // 配置的默认值
    if (service.getTemplateContext() == null) {
      // 默认配置
      TemplateContext templateContext = new TemplateContext();
      templateContext.setTemplateSettingMap(new HashMap<>());
      templateContext.setProjectPath(project.getBasePath());
      service.setTemplateContext(templateContext);
    }
    return service;
  }

  @Override
  public ProjectTemplatesSettings.State getState() {
    return state;
  }

  @Override
  public void loadState(@NotNull ProjectTemplatesSettings.State state) {
    XmlSerializerUtil.copyBean(state, this.state);
  }

  @Override
  public void noStateLoaded() {
    // 加载全局模板配置
    this.state.templates = IntellijSDK.getService(GlobalTemplateSettings.class).getTemplates();
  }

  @Override
  public void initializeComponent() {

  }

  /**
   * 默认的配置更改是无效的
   *
   * @return 模板设置
   */
  public Map<String, List<TemplateSettingDTO>> getTemplateSettingMap() {
    return getTemplateSettingMap(this.templateContext);
  }

  /**
   * 默认的配置更改是无效的
   *
   * @return 模板设置
   */
  public static Map<String, List<TemplateSettingDTO>> getTemplateSettingMap(TemplateContext templateContext) {
    final Map<String, List<TemplateSettingDTO>> templateSettingMap = new HashMap<>();
    final Map<String, List<TemplateSettingDTO>> settingMap = templateContext.getTemplateSettingMap();
    Map<String, List<TemplateSettingDTO>> setTemplateSettingMap = DefaultSettingsConfig.defaultSettings();
    templateSettingMap.putAll(settingMap);
    templateSettingMap.putAll(setTemplateSettingMap);
    return templateSettingMap;
  }

  public Map<String, List<TemplateSettingDTO>> getTemplates() {
    return state.templates;
  }

  @Data
  public static class State {

    private Map<String, List<TemplateSettingDTO>> templates;
  }
}
