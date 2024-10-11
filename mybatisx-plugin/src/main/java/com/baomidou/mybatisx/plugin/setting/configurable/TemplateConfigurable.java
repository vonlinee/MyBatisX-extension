package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.plugin.ui.components.TemplateSettingPane;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 模板配置
 */
public class TemplateConfigurable implements SearchableConfigurable {

    TemplateSettingPane rootPanel;

    public TemplateConfigurable() {
        rootPanel = new TemplateSettingPane();
    }

    @Override
    public @NotNull String getId() {
        return getClass().getName();
    }

    @Override
    public String getDisplayName() {
        return getId();
    }

    @Override
    public @Nullable JComponent createComponent() {
        return this.rootPanel;
    }

    @Override
    public boolean isModified() {

        boolean b = rootPanel.hasChanged();

        System.out.println(b);
        return b;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
