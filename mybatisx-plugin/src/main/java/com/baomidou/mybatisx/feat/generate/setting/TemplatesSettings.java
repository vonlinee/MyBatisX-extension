package com.baomidou.mybatisx.feat.generate.setting;

import com.baomidou.mybatisx.feat.bean.TemplateInfo;
import com.baomidou.mybatisx.feat.generate.dto.TemplateContext;
import com.baomidou.mybatisx.feat.generate.dto.TemplateSettingDTO;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.MyBatisXPlugin;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板设置
 */
@State(name = "TemplatesSettings", storages = {@Storage(value = MyBatisXPlugin.PERSISTENT_STATE_FILE)})
public final class TemplatesSettings implements PersistentStateComponent<TemplatesSettings.State> {

    @Setter
    @Getter
    private TemplateContext templateConfigs;

    private final State state = new State();

    @NotNull
    public static TemplatesSettings getInstance(@NotNull Project project) {
        TemplatesSettings service = IntellijSDK.getService(TemplatesSettings.class, project);
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
    public TemplatesSettings.State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull TemplatesSettings.State state) {
        XmlSerializerUtil.copyBean(state, this.state);
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

    public static class State {
        /**
         * 模板信息列表
         * 序列化的对象需要有默认构造器，否则序列化失败
         */
        public List<TemplateInfo> templates = new ArrayList<>();
    }

    public List<TemplateInfo> getTemplates() {
        return state.templates;
    }
}
