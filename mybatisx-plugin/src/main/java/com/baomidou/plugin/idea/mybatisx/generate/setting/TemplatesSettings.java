package com.baomidou.plugin.idea.mybatisx.generate.setting;

import com.baomidou.plugin.idea.mybatisx.generate.dto.TemplateContext;
import com.baomidou.plugin.idea.mybatisx.generate.dto.TemplateSettingDTO;
import com.baomidou.plugin.idea.mybatisx.model.TemplateInfo;
import com.baomidou.plugin.idea.mybatisx.util.MyBatisXPlugin;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板设置
 */
@State(name = "TemplatesSettings", storages = {@Storage(value = MyBatisXPlugin.PERSISTENT_STATE_FILE)})
public class TemplatesSettings implements PersistentStateComponent<TemplatesSettings> {

    /**
     * 模板信息列表
     * 序列化的对象需要有默认构造器，否则序列化失败
     */
    public List<TemplateInfo> templates = new ArrayList<>();
    private TemplateContext templateConfigs;

    @NotNull
    public static TemplatesSettings getInstance(@NotNull Project project) {
        TemplatesSettings service = ServiceManager.getService(project, TemplatesSettings.class);
        // 配置的默认值
        if (service.templateConfigs == null) {
            // 默认配置
            TemplateContext templateContext = new TemplateContext();
            templateContext.setTemplateSettingMap(new HashMap<>());
            templateContext.setProjectPath(project.getBasePath());
            service.templateConfigs = templateContext;
        }
        return service;
    }

    @Override
    public @Nullable TemplatesSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TemplatesSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /**
     * 默认的配置更改是无效的
     *
     * @return 模板设置
     */
    public Map<String, List<TemplateSettingDTO>> getTemplateSettingMap() {
        final Map<String, List<TemplateSettingDTO>> templateSettingMap = new HashMap<>();
        final Map<String, List<TemplateSettingDTO>> settingMap = templateConfigs.getTemplateSettingMap();
        Map<String, List<TemplateSettingDTO>> setTemplateSettingMap = DefaultSettingsConfig.defaultSettings();
        templateSettingMap.putAll(settingMap);
        templateSettingMap.putAll(setTemplateSettingMap);
        return templateSettingMap;
    }

    public TemplateContext getTemplateConfigs() {
        return templateConfigs;
    }

    public void setTemplateConfigs(TemplateContext templateConfigs) {
        this.templateConfigs = templateConfigs;
    }
}
