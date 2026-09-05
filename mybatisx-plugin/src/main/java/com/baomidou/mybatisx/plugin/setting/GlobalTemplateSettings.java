package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateSettingDTO;
import com.baomidou.mybatisx.feat.mybatis.generator.setting.DefaultSettingsConfig;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.MyBatisXPlugin;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局设置
 */
@Service
@State(name = "TemplatesSettings", storages = {
  @Storage(value = MyBatisXPlugin.STORAGE_FILE_ROOT_NAME + "-templates" + MyBatisXPlugin.STORAGE_FILE_EXTENSION)
})
public final class GlobalTemplateSettings implements PersistentStateComponent<GlobalTemplateSettings.State> {

  private final State state = new State();

  public static GlobalTemplateSettings getInstance() {
    return IntellijSDK.getService(GlobalTemplateSettings.class);
  }

  public Map<String, List<TemplateSettingDTO>> getTemplates() {
    List<TemplateGroup> groups = state.getTemplates();
    Map<String, List<TemplateSettingDTO>> templatesMap = new LinkedHashMap<>();
    for (TemplateGroup group : groups) {
      templatesMap.put(group.getName(), group.getTemplates());
    }
    return templatesMap;
  }

  public List<TemplateGroup> getTemplateGroups() {
    return state.getTemplates();
  }

  @Override
  public GlobalTemplateSettings.State getState() {
    return state;
  }

  @Override
  public void loadState(@NotNull GlobalTemplateSettings.State state) {
    XmlSerializerUtil.copyBean(state, this.state);
  }

  @Override
  public void noStateLoaded() {
    Map<String, List<TemplateSettingDTO>> templates = DefaultSettingsConfig.defaultSettings();
    List<TemplateGroup> templateGroups = new ArrayList<>();
    for (Map.Entry<String, List<TemplateSettingDTO>> entry : templates.entrySet()) {
      TemplateGroup group = new TemplateGroup();
      group.setName(entry.getKey());
      group.addTemplates(entry.getValue());
      templateGroups.add(group);
    }
    this.state.templates = templateGroups;
  }

  @Override
  public void initializeComponent() {
  }

  @Data
  public static class State {

    private List<TemplateGroup> templates = new ArrayList<>();
  }
}
