package com.baomidou.mybatisx.plugin.setting.configurable;

import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * MyBatis 生成器配置
 */
public class MyBatisGeneratorConfigurable extends SearchableConfigurableBase {

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
