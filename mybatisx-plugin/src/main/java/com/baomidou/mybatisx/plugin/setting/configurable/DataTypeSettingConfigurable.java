package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.plugin.setting.JavaBean2DDLSetting;
import com.baomidou.mybatisx.plugin.ui.components.DataTypeMappingTable;
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

    DataTypeMappingTable dataTypeMappingTable;

    public DataTypeSettingConfigurable() {
        dataTypeMappingTable = new DataTypeMappingTable();
    }

    @NotNull
    @Override
    public String getId() {
        return "top.breezes.javabean2ddl.java-bean-2-ddl-idea-plugin";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "DataType";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return dataTypeMappingTable;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {

    }
}
