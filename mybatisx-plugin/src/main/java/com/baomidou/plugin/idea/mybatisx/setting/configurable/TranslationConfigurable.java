package com.baomidou.plugin.idea.mybatisx.setting.configurable;

import com.baomidou.plugin.idea.mybatisx.setting.JavaBean2DDLSetting;
import com.baomidou.plugin.idea.mybatisx.setting.ui.TranslationSettingPanel;
import com.baomidou.plugin.idea.mybatisx.ddl.TranslationAppEnum;
import com.baomidou.plugin.idea.mybatisx.model.ComboBoxItem;
import com.baomidou.plugin.idea.mybatisx.model.TranslationAppComboBoxItem;
import com.intellij.openapi.options.ConfigurationException;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public final class TranslationConfigurable extends SearchableConfigurableBase {

    TranslationSettingPanel settingPanel;
    private final JavaBean2DDLSetting javaBean2DDLSetting;
    public TranslationConfigurable() {
        settingPanel = new TranslationSettingPanel();
        this.javaBean2DDLSetting = JavaBean2DDLSetting.getInstance();
    }

    @Override
    public @Nullable JComponent createComponent() {
        return settingPanel.getMainPanel();
    }


    @Override
    public boolean isModified() {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;
        if (!StringUtils.equals(String.valueOf(myProperties.getAutoTranslationRadio()),
            String.valueOf(settingPanel.getAutoTranslationRadio().isSelected()))) {
            return true;
        }
        ComboBoxItem appComboBox = (ComboBoxItem) settingPanel.getTranslationAppComboBox().getSelectedItem();
        assert appComboBox != null;
        if (!StringUtils.equals(myProperties.getTranslationAppComboBox(), appComboBox.getValue())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getAppIdText(), settingPanel.getAppIdText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getSecretText(), settingPanel.getSecretText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getSecretId(), settingPanel.getSecretId().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getSecretKey(), settingPanel.getSecretKey().getText())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;
        myProperties.setAutoTranslationRadio(settingPanel.getAutoTranslationRadio().isSelected());
        myProperties.setAppIdText(settingPanel.getAppIdText().getText());
        myProperties.setSecretText(settingPanel.getSecretText().getText());
        myProperties.setSecretId(settingPanel.getSecretId().getText());
        myProperties.setSecretKey(settingPanel.getSecretKey().getText());
        ComboBoxItem appComboBox = (ComboBoxItem) settingPanel.getTranslationAppComboBox().getSelectedItem();
        assert appComboBox != null;
        myProperties.setTranslationAppComboBox(appComboBox.getValue());
    }

    @Override
    public void reset() {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;
        settingPanel.getAutoTranslationRadio().setSelected((myProperties.getAutoTranslationRadio()));
        settingPanel.getTranslationAppComboBox().setSelectedItem(new TranslationAppComboBoxItem(
            TranslationAppEnum.findByValue(myProperties.getTranslationAppComboBox())
        ));
        settingPanel.getAppIdText().setText(myProperties.getAppIdText());
        settingPanel.getSecretText().setText(myProperties.getSecretText());
        settingPanel.getSecretId().setText(myProperties.getSecretId());
        settingPanel.getSecretKey().setText(myProperties.getSecretKey());
    }
}
