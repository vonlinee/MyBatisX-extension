package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.feat.bean.TranslationAppComboBoxItem;
import com.baomidou.mybatisx.feat.ddl.TranslationAppEnum;
import com.baomidou.mybatisx.model.ComboBoxItem;
import com.baomidou.mybatisx.plugin.setting.JavaBean2DDLSetting;
import com.baomidou.mybatisx.plugin.ui.TranslationSettingPanel;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.openapi.options.ConfigurationException;
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
        return settingPanel.getRootPanel();
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
        return !StringUtils.equals(myProperties.getSecretKey(), settingPanel.getSecretKey().getText());
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

        TranslationAppEnum appEnum = TranslationAppEnum.findByValue(myProperties.getTranslationAppComboBox());
        settingPanel.getTranslationAppComboBox().setSelectedItem(new TranslationAppComboBoxItem(appEnum.getName(), appEnum.getValue()));
        settingPanel.getAppIdText().setText(myProperties.getAppIdText());
        settingPanel.getSecretText().setText(myProperties.getSecretText());
        settingPanel.getSecretId().setText(myProperties.getSecretId());
        settingPanel.getSecretKey().setText(myProperties.getSecretKey());
    }
}
