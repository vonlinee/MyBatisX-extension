package com.baomidou.plugin.idea.mybatisx.ui;

import com.baomidou.plugin.idea.mybatisx.ddl.SqlTypeEnum;
import com.baomidou.plugin.idea.mybatisx.model.ComboBoxItem;
import com.baomidou.plugin.idea.mybatisx.model.SqlTypeComboBoxItem;
import com.baomidou.plugin.idea.mybatisx.setting.JavaBean2DDLSetting;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public final class SettingPanelConfig implements SearchableConfigurable {

    private SettingPanel settingPanel;

    private JavaBean2DDLSetting javaBean2DDLSetting;

    public SettingPanelConfig() {
        this.settingPanel = new SettingPanel();
        this.javaBean2DDLSetting = JavaBean2DDLSetting.getInstance();
    }

    @NotNull
    @Override
    public String getId() {
        return "top.breezes.javabean2ddl.java-bean-2-ddl-idea-plugin";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Java Bean To DDL";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return settingPanel.getMainPanel();
    }

    @Override
    public boolean isModified() {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;
        if (!StringUtils.equals(myProperties.getIdAnnotation(), settingPanel.getIdText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getTableAnnotation(), settingPanel.getTableText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getTableAnnotationProperty(), settingPanel.getTablePropertyText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getCommentAnnotation(), settingPanel.getCommendText().getText())) {
            return true;
        }
        ComboBoxItem intComboBox = (ComboBoxItem) settingPanel.getIntMapComboBox().getSelectedItem();
        assert intComboBox != null;
        if (!StringUtils.equals(myProperties.getIntType(), intComboBox.getValue())) {
            return true;
        }
        ComboBoxItem doubleComboBox = (ComboBoxItem) settingPanel.getDoubleMapComboBox().getSelectedItem();
        assert doubleComboBox != null;
        if (!StringUtils.equals(myProperties.getDoubleType(), doubleComboBox.getValue())) {
            return true;
        }
        ComboBoxItem floatComboBox = (ComboBoxItem) settingPanel.getFloatMapComboBox().getSelectedItem();
        assert floatComboBox != null;
        if (!StringUtils.equals(myProperties.getFloatType(), floatComboBox.getValue())) {
            return true;
        }
        ComboBoxItem longComboBox = (ComboBoxItem) settingPanel.getLongMapComboBox().getSelectedItem();
        assert longComboBox != null;
        if (!StringUtils.equals(myProperties.getLongType(), longComboBox.getValue())) {
            return true;
        }
        ComboBoxItem stringComboBox = (ComboBoxItem) settingPanel.getStringMapComboBox().getSelectedItem();
        assert stringComboBox != null;
        if (!StringUtils.equals(myProperties.getStringType(), stringComboBox.getValue())) {
            return true;
        }
        ComboBoxItem booleanComboBox = (ComboBoxItem) settingPanel.getBooleanMapComboBox().getSelectedItem();
        assert booleanComboBox != null;
        if (!StringUtils.equals(myProperties.getBooleanType(), booleanComboBox.getValue())) {
            return true;
        }
        ComboBoxItem dateComboBox = (ComboBoxItem) settingPanel.getDateMapComboBox().getSelectedItem();
        assert dateComboBox != null;
        if (!StringUtils.equals(myProperties.getDateType(), dateComboBox.getValue())) {
            return true;
        }
        ComboBoxItem bigDecimalComboBox = (ComboBoxItem) settingPanel.getBigDecimalMapComboBox().getSelectedItem();
        assert bigDecimalComboBox != null;
        if (!StringUtils.equals(myProperties.getBigDecimalType(), bigDecimalComboBox.getValue())) {
            return true;
        }
        ComboBoxItem localDateComboBoxItem = (ComboBoxItem) settingPanel.getLocalDateMapComboBox().getSelectedItem();
        assert localDateComboBoxItem != null;
        if (!StringUtils.equals(myProperties.getLocalDateType(), localDateComboBoxItem.getValue())) {
            return true;
        }
        ComboBoxItem localTimeComboBoxItem = (ComboBoxItem) settingPanel.getLocalTimeMapComboBox().getSelectedItem();
        assert localTimeComboBoxItem != null;
        if (!StringUtils.equals(myProperties.getLocalTimeType(), localTimeComboBoxItem.getValue())) {
            return true;
        }
        ComboBoxItem localDateTimeComboBoxItem = (ComboBoxItem) settingPanel.getLocalDateTimeMapComboBox().getSelectedItem();
        assert localDateTimeComboBoxItem != null;
        if (!StringUtils.equals(myProperties.getLocalDateTimeType(), localDateTimeComboBoxItem.getValue())) {
            return true;
        }

        if (!StringUtils.equals(myProperties.getIntDefaultLength(), settingPanel.getIntDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getLongDefaultLength(), settingPanel.getLongDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getDoubleDefaultLength(), settingPanel.getDoubleDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getFloatDefaultLength(), settingPanel.getFloatDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getBooleanDefaultLength(), settingPanel.getBooleanDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getStringDefaultLength(), settingPanel.getStringDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getBigDecimalDefaultLength(), settingPanel.getBigDecimalDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getLocalDateDefaultLength(), settingPanel.getLocalDateDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getLocalTimeDefaultLength(), settingPanel.getLocalTimeDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getLocalDateTimeDefaultLength(), settingPanel.getLocalDateTimeDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(String.valueOf(myProperties.getShowNoInMapFieldRadio()),
            String.valueOf(settingPanel.getShowNoInMapFieldRadio().isSelected()))) {
            return true;
        }
        return !StringUtils.equals(myProperties.getDateDefaultLength(), settingPanel.getDateDefaultText().getText());
    }

    @Override
    public void apply() throws ConfigurationException {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;

        myProperties.setIdAnnotation(settingPanel.getIdText().getText());
        myProperties.setTableAnnotation(settingPanel.getTableText().getText());
        myProperties.setTableAnnotationProperty(settingPanel.getTablePropertyText().getText());
        myProperties.setCommentAnnotation(settingPanel.getCommendText().getText());

        myProperties.setIntType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getIntMapComboBox().getSelectedItem())).getValue());
        myProperties.setBooleanType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getBooleanMapComboBox().getSelectedItem())).getValue());
        myProperties.setDoubleType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getDoubleMapComboBox().getSelectedItem())).getValue());
        myProperties.setFloatType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getFloatMapComboBox().getSelectedItem())).getValue());
        myProperties.setLongType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getLongMapComboBox().getSelectedItem())).getValue());
        myProperties.setStringType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getStringMapComboBox().getSelectedItem())).getValue());
        myProperties.setDateType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getDateMapComboBox().getSelectedItem())).getValue());
        myProperties.setBigDecimalType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getBigDecimalMapComboBox().getSelectedItem())).getValue());
        myProperties.setLocalDateType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getLocalDateMapComboBox().getSelectedItem())).getValue());
        myProperties.setLocalTimeType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getLocalTimeMapComboBox().getSelectedItem())).getValue());
        myProperties.setLocalDateTimeType(((ComboBoxItem) Objects.requireNonNull(settingPanel.getLocalDateTimeMapComboBox().getSelectedItem())).getValue());

        myProperties.setIntDefaultLength(settingPanel.getIntDefaultText().getText());
        myProperties.setLongDefaultLength(settingPanel.getLongDefaultText().getText());
        myProperties.setDoubleDefaultLength(settingPanel.getDoubleDefaultText().getText());
        myProperties.setFloatDefaultLength(settingPanel.getFloatDefaultText().getText());
        myProperties.setBooleanDefaultLength(settingPanel.getBooleanDefaultText().getText());
        myProperties.setDateDefaultLength(settingPanel.getDateDefaultText().getText());
        myProperties.setStringDefaultLength(settingPanel.getStringDefaultText().getText());
        myProperties.setBigDecimalDefaultLength(settingPanel.getBigDecimalDefaultText().getText());
        myProperties.setLocalDateDefaultLength(settingPanel.getLocalDateDefaultText().getText());
        myProperties.setLocalTimeDefaultLength(settingPanel.getLocalTimeDefaultText().getText());
        myProperties.setLocalDateTimeDefaultLength(settingPanel.getLocalDateTimeDefaultText().getText());

        myProperties.setShowNoInMapFieldRadio(settingPanel.getShowNoInMapFieldRadio().isSelected());
    }

    @Override
    public void reset() {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;
        settingPanel.getIdText().setText(myProperties.getIdAnnotation());
        settingPanel.getTableText().setText(myProperties.getTableAnnotation());
        settingPanel.getTablePropertyText().setText(myProperties.getTableAnnotationProperty());
        settingPanel.getCommendText().setText(myProperties.getCommentAnnotation());

        settingPanel.getIntMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getIntType()))));
        settingPanel.getLongMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getLongType()))));
        settingPanel.getBooleanMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getBooleanType()))));
        settingPanel.getDateMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getDateType()))));
        settingPanel.getStringMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getStringType()))));
        settingPanel.getDoubleMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getDoubleType()))));
        settingPanel.getFloatMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getFloatType()))));
        settingPanel.getBigDecimalMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getBigDecimalType()))));
        settingPanel.getLocalDateMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getLocalDateType()))));
        settingPanel.getLocalTimeMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getLocalTimeType()))));
        settingPanel.getLocalDateTimeMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getLocalDateTimeType()))));

        settingPanel.getIntDefaultText().setText(myProperties.getIntDefaultLength());
        settingPanel.getLongDefaultText().setText(myProperties.getLongDefaultLength());
        settingPanel.getDoubleDefaultText().setText(myProperties.getDoubleDefaultLength());
        settingPanel.getFloatDefaultText().setText(myProperties.getFloatDefaultLength());
        settingPanel.getBooleanDefaultText().setText(myProperties.getBooleanDefaultLength());
        settingPanel.getDateDefaultText().setText(myProperties.getDateDefaultLength());
        settingPanel.getStringDefaultText().setText(myProperties.getStringDefaultLength());
        settingPanel.getBigDecimalDefaultText().setText(myProperties.getBigDecimalDefaultLength());
        settingPanel.getLocalDateDefaultText().setText(myProperties.getLocalDateDefaultLength());
        settingPanel.getLocalTimeDefaultText().setText(myProperties.getLocalTimeDefaultLength());
        settingPanel.getLocalDateTimeDefaultText().setText(myProperties.getLocalDateTimeDefaultLength());

        settingPanel.getShowNoInMapFieldRadio().setSelected(myProperties.getShowNoInMapFieldRadio());
    }
}
