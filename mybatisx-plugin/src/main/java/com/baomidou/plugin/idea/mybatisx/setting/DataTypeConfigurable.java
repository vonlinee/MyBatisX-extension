package com.baomidou.plugin.idea.mybatisx.setting;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DataTypeConfigurable implements SearchableConfigurable {
    @Override
    public @NotNull String getId() {
        return getClass().getName();
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "DataType Setting";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
