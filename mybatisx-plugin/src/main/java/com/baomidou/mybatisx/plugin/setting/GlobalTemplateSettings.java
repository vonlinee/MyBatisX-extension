package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.feat.generate.setting.TemplatesSettings;
import com.baomidou.mybatisx.feat.bean.TemplateInfo;
import com.baomidou.mybatisx.util.MyBatisXPlugin;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @see TemplatesSettings
 */
@State(name = "GlobalTemplateSettings", storages = {@Storage(value = MyBatisXPlugin.PERSISTENT_STATE_FILE)})
public class GlobalTemplateSettings implements PersistentStateComponent<GlobalTemplateSettings.State> {

    private final State state = new State();

    public static GlobalTemplateSettings getInstance() {
        return ApplicationManager.getApplication().getService(GlobalTemplateSettings.class);
    }

    @Override
    public @Nullable GlobalTemplateSettings.State getState() {
        return this.state;
    }

    @Override
    public void loadState(@NotNull State state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }

    public static class State {
        /**
         * 模板信息列表
         * 序列化的对象需要有默认构造器，否则序列化失败
         */
        public List<TemplateInfo> templates = new ArrayList<>();
    }

    public void setTemplates(List<TemplateInfo> templates) {
        state.templates.addAll(templates);
    }

    public List<TemplateInfo> getTemplates() {
        return state.templates;
    }
}
