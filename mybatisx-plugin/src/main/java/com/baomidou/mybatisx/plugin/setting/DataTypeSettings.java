package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.model.DataTypeSystem;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.PluginUtils;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 数据类型设置
 */
@Service
@State(name = "DataTypeSettings", storages = {@Storage(PluginUtils.PLUGIN_NAME + "/datatype-system.xml")})
public final class DataTypeSettings implements PersistentStateComponent<DataTypeSystem> {

    private final DataTypeSystem typeSystem = new DataTypeSystem();

    public static DataTypeSettings getInstance() {
        return IntellijSDK.getService(DataTypeSettings.class);
    }

    @Override
    @NotNull
    public DataTypeSystem getState() {
        return typeSystem;
    }

    @Override
    public void loadState(@NotNull DataTypeSystem state) {
        XmlSerializerUtil.copyBean(state, this.typeSystem);
    }

    @Override
    public void noStateLoaded() {
        if (this.typeSystem.isEmpty()) {
            this.typeSystem.initBuiltinTypeSystem();
        }
    }
}
