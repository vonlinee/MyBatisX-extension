package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.ddl.SqlTypeEnum;
import com.baomidou.mybatisx.model.ComboBoxItem;
import com.baomidou.mybatisx.model.SqlTypeComboBoxItem;
import com.baomidou.mybatisx.plugin.setting.JavaBean2DDLSetting;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.baomidou.mybatisx.util.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public final class SettingPanelConfig implements SearchableConfigurable {

    private DataTypeSettingPanel dataTypeSettingPanel;

    private JavaBean2DDLSetting javaBean2DDLSetting;

    public SettingPanelConfig() {
        this.dataTypeSettingPanel = new DataTypeSettingPanel();
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
        return dataTypeSettingPanel.getMainPanel();
    }

    @Override
    public boolean isModified() {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;
        if (!StringUtils.equals(myProperties.getIdAnnotation(), dataTypeSettingPanel.getIdText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getTableAnnotation(), dataTypeSettingPanel.getTableText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getTableAnnotationProperty(), dataTypeSettingPanel.getTablePropertyText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getCommentAnnotation(), dataTypeSettingPanel.getCommendText().getText())) {
            return true;
        }
        ComboBoxItem intComboBox = (ComboBoxItem) dataTypeSettingPanel.getIntMapComboBox().getSelectedItem();
        assert intComboBox != null;
        if (!StringUtils.equals(myProperties.getIntType(), intComboBox.getValue())) {
            return true;
        }
        ComboBoxItem doubleComboBox = (ComboBoxItem) dataTypeSettingPanel.getDoubleMapComboBox().getSelectedItem();
        assert doubleComboBox != null;
        if (!StringUtils.equals(myProperties.getDoubleType(), doubleComboBox.getValue())) {
            return true;
        }
        ComboBoxItem floatComboBox = (ComboBoxItem) dataTypeSettingPanel.getFloatMapComboBox().getSelectedItem();
        assert floatComboBox != null;
        if (!StringUtils.equals(myProperties.getFloatType(), floatComboBox.getValue())) {
            return true;
        }
        ComboBoxItem longComboBox = (ComboBoxItem) dataTypeSettingPanel.getLongMapComboBox().getSelectedItem();
        assert longComboBox != null;
        if (!StringUtils.equals(myProperties.getLongType(), longComboBox.getValue())) {
            return true;
        }
        ComboBoxItem stringComboBox = (ComboBoxItem) dataTypeSettingPanel.getStringMapComboBox().getSelectedItem();
        assert stringComboBox != null;
        if (!StringUtils.equals(myProperties.getStringType(), stringComboBox.getValue())) {
            return true;
        }
        ComboBoxItem booleanComboBox = (ComboBoxItem) dataTypeSettingPanel.getBooleanMapComboBox().getSelectedItem();
        assert booleanComboBox != null;
        if (!StringUtils.equals(myProperties.getBooleanType(), booleanComboBox.getValue())) {
            return true;
        }
        ComboBoxItem dateComboBox = (ComboBoxItem) dataTypeSettingPanel.getDateMapComboBox().getSelectedItem();
        assert dateComboBox != null;
        if (!StringUtils.equals(myProperties.getDateType(), dateComboBox.getValue())) {
            return true;
        }
        ComboBoxItem bigDecimalComboBox = (ComboBoxItem) dataTypeSettingPanel.getBigDecimalMapComboBox().getSelectedItem();
        assert bigDecimalComboBox != null;
        if (!StringUtils.equals(myProperties.getBigDecimalType(), bigDecimalComboBox.getValue())) {
            return true;
        }
        ComboBoxItem localDateComboBoxItem = (ComboBoxItem) dataTypeSettingPanel.getLocalDateMapComboBox().getSelectedItem();
        assert localDateComboBoxItem != null;
        if (!StringUtils.equals(myProperties.getLocalDateType(), localDateComboBoxItem.getValue())) {
            return true;
        }
        ComboBoxItem localTimeComboBoxItem = (ComboBoxItem) dataTypeSettingPanel.getLocalTimeMapComboBox().getSelectedItem();
        assert localTimeComboBoxItem != null;
        if (!StringUtils.equals(myProperties.getLocalTimeType(), localTimeComboBoxItem.getValue())) {
            return true;
        }
        ComboBoxItem localDateTimeComboBoxItem = (ComboBoxItem) dataTypeSettingPanel.getLocalDateTimeMapComboBox().getSelectedItem();
        assert localDateTimeComboBoxItem != null;
        if (!StringUtils.equals(myProperties.getLocalDateTimeType(), localDateTimeComboBoxItem.getValue())) {
            return true;
        }

        if (!StringUtils.equals(myProperties.getIntDefaultLength(), dataTypeSettingPanel.getIntDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getLongDefaultLength(), dataTypeSettingPanel.getLongDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getDoubleDefaultLength(), dataTypeSettingPanel.getDoubleDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getFloatDefaultLength(), dataTypeSettingPanel.getFloatDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getBooleanDefaultLength(), dataTypeSettingPanel.getBooleanDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getStringDefaultLength(), dataTypeSettingPanel.getStringDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getBigDecimalDefaultLength(), dataTypeSettingPanel.getBigDecimalDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getLocalDateDefaultLength(), dataTypeSettingPanel.getLocalDateDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getLocalTimeDefaultLength(), dataTypeSettingPanel.getLocalTimeDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(myProperties.getLocalDateTimeDefaultLength(), dataTypeSettingPanel.getLocalDateTimeDefaultText().getText())) {
            return true;
        }
        if (!StringUtils.equals(String.valueOf(myProperties.getShowNoInMapFieldRadio()),
            String.valueOf(dataTypeSettingPanel.getShowNoInMapFieldRadio().isSelected()))) {
            return true;
        }
        return !StringUtils.equals(myProperties.getDateDefaultLength(), dataTypeSettingPanel.getDateDefaultText().getText());
    }

    @Override
    public void apply() throws ConfigurationException {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;

        myProperties.setIdAnnotation(dataTypeSettingPanel.getIdText().getText());
        myProperties.setTableAnnotation(dataTypeSettingPanel.getTableText().getText());
        myProperties.setTableAnnotationProperty(dataTypeSettingPanel.getTablePropertyText().getText());
        myProperties.setCommentAnnotation(dataTypeSettingPanel.getCommendText().getText());

        myProperties.setIntType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getIntMapComboBox().getSelectedItem())).getValue());
        myProperties.setBooleanType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getBooleanMapComboBox().getSelectedItem())).getValue());
        myProperties.setDoubleType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getDoubleMapComboBox().getSelectedItem())).getValue());
        myProperties.setFloatType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getFloatMapComboBox().getSelectedItem())).getValue());
        myProperties.setLongType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getLongMapComboBox().getSelectedItem())).getValue());
        myProperties.setStringType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getStringMapComboBox().getSelectedItem())).getValue());
        myProperties.setDateType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getDateMapComboBox().getSelectedItem())).getValue());
        myProperties.setBigDecimalType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getBigDecimalMapComboBox().getSelectedItem())).getValue());
        myProperties.setLocalDateType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getLocalDateMapComboBox().getSelectedItem())).getValue());
        myProperties.setLocalTimeType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getLocalTimeMapComboBox().getSelectedItem())).getValue());
        myProperties.setLocalDateTimeType(((ComboBoxItem) Objects.requireNonNull(dataTypeSettingPanel.getLocalDateTimeMapComboBox().getSelectedItem())).getValue());

        myProperties.setIntDefaultLength(dataTypeSettingPanel.getIntDefaultText().getText());
        myProperties.setLongDefaultLength(dataTypeSettingPanel.getLongDefaultText().getText());
        myProperties.setDoubleDefaultLength(dataTypeSettingPanel.getDoubleDefaultText().getText());
        myProperties.setFloatDefaultLength(dataTypeSettingPanel.getFloatDefaultText().getText());
        myProperties.setBooleanDefaultLength(dataTypeSettingPanel.getBooleanDefaultText().getText());
        myProperties.setDateDefaultLength(dataTypeSettingPanel.getDateDefaultText().getText());
        myProperties.setStringDefaultLength(dataTypeSettingPanel.getStringDefaultText().getText());
        myProperties.setBigDecimalDefaultLength(dataTypeSettingPanel.getBigDecimalDefaultText().getText());
        myProperties.setLocalDateDefaultLength(dataTypeSettingPanel.getLocalDateDefaultText().getText());
        myProperties.setLocalTimeDefaultLength(dataTypeSettingPanel.getLocalTimeDefaultText().getText());
        myProperties.setLocalDateTimeDefaultLength(dataTypeSettingPanel.getLocalDateTimeDefaultText().getText());

        myProperties.setShowNoInMapFieldRadio(dataTypeSettingPanel.getShowNoInMapFieldRadio().isSelected());
    }

    @Override
    public void reset() {
        JavaBean2DDLSetting.MySettingProperties myProperties = javaBean2DDLSetting.myProperties;
        dataTypeSettingPanel.getIdText().setText(myProperties.getIdAnnotation());
        dataTypeSettingPanel.getTableText().setText(myProperties.getTableAnnotation());
        dataTypeSettingPanel.getTablePropertyText().setText(myProperties.getTableAnnotationProperty());
        dataTypeSettingPanel.getCommendText().setText(myProperties.getCommentAnnotation());

        dataTypeSettingPanel.getIntMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getIntType()))));
        dataTypeSettingPanel.getLongMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getLongType()))));
        dataTypeSettingPanel.getBooleanMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getBooleanType()))));
        dataTypeSettingPanel.getDateMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getDateType()))));
        dataTypeSettingPanel.getStringMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getStringType()))));
        dataTypeSettingPanel.getDoubleMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getDoubleType()))));
        dataTypeSettingPanel.getFloatMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getFloatType()))));
        dataTypeSettingPanel.getBigDecimalMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getBigDecimalType()))));
        dataTypeSettingPanel.getLocalDateMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getLocalDateType()))));
        dataTypeSettingPanel.getLocalTimeMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getLocalTimeType()))));
        dataTypeSettingPanel.getLocalDateTimeMapComboBox().setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(myProperties.getLocalDateTimeType()))));

        dataTypeSettingPanel.getIntDefaultText().setText(myProperties.getIntDefaultLength());
        dataTypeSettingPanel.getLongDefaultText().setText(myProperties.getLongDefaultLength());
        dataTypeSettingPanel.getDoubleDefaultText().setText(myProperties.getDoubleDefaultLength());
        dataTypeSettingPanel.getFloatDefaultText().setText(myProperties.getFloatDefaultLength());
        dataTypeSettingPanel.getBooleanDefaultText().setText(myProperties.getBooleanDefaultLength());
        dataTypeSettingPanel.getDateDefaultText().setText(myProperties.getDateDefaultLength());
        dataTypeSettingPanel.getStringDefaultText().setText(myProperties.getStringDefaultLength());
        dataTypeSettingPanel.getBigDecimalDefaultText().setText(myProperties.getBigDecimalDefaultLength());
        dataTypeSettingPanel.getLocalDateDefaultText().setText(myProperties.getLocalDateDefaultLength());
        dataTypeSettingPanel.getLocalTimeDefaultText().setText(myProperties.getLocalTimeDefaultLength());
        dataTypeSettingPanel.getLocalDateTimeDefaultText().setText(myProperties.getLocalDateTimeDefaultLength());

        dataTypeSettingPanel.getShowNoInMapFieldRadio().setSelected(myProperties.getShowNoInMapFieldRadio());
    }
}
