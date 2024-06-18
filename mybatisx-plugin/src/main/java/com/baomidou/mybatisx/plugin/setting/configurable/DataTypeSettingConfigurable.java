package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.plugin.ui.components.DataTypeSettingPanel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 数据类型设置面板
 */
public final class DataTypeSettingConfigurable implements SearchableConfigurable {

    DataTypeSettingPanel dataTypeSettingPanel;

    public DataTypeSettingConfigurable() {
        dataTypeSettingPanel = new DataTypeSettingPanel();
    }

    @NotNull
    @Override
    public String getId() {
        return getClass().getName();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "DataType";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return dataTypeSettingPanel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
