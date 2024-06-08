package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.util.PluginUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 数据类型映射设置
 */
@State(name = "DataTypeMappingSettings", storages = {@Storage(PluginUtils.PLUGIN_NAME + "/datatype-mapping.xml")})
public class DataTypeMappingSettings implements PersistentStateComponent<DataTypeMappingSettings> {

    public static DataTypeMappingSettings getInstance() {
        return ApplicationManager.getApplication().getService(DataTypeMappingSettings.class);
    }

    @Override
    public @Nullable DataTypeMappingSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DataTypeMappingSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
