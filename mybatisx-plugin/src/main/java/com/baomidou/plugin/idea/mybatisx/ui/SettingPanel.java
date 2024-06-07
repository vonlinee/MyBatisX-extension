package com.baomidou.plugin.idea.mybatisx.ui;

import com.baomidou.plugin.idea.mybatisx.enums.SqlTypeEnum;
import com.baomidou.plugin.idea.mybatisx.model.ComboBoxItem;
import com.baomidou.plugin.idea.mybatisx.model.SqlTypeComboBoxItem;
import com.baomidou.plugin.idea.mybatisx.service.BaseTypeItemListener;
import com.baomidou.plugin.idea.mybatisx.setting.JavaBean2DDLSetting;
import com.intellij.openapi.components.ServiceManager;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Objects;

@Getter
@Setter
public class SettingPanel {
    private JPanel mainPanel;
    private JTextField tableText;
    private JTextField idText;
    private JTextField commendText;
    private JComboBox<ComboBoxItem> intMapComboBox;
    private JTextField intDefaultText;
    private JPanel auxiliaryPanel;
    private JPanel annotationPanel;
    private JPanel docPanel;
    private JPanel mapPanel;
    private JPanel commonlyUsedMapPanel;
    private JComboBox<ComboBoxItem> longMapComboBox;
    private JTextField longDefaultText;
    private JComboBox<ComboBoxItem> stringMapComboBox;
    private JTextField stringDefaultText;
    private JComboBox<ComboBoxItem> booleanMapComboBox;
    private JTextField booleanDefaultText;
    private JComboBox<ComboBoxItem> dateMapComboBox;
    private JTextField dateDefaultText;
    private JTextField tablePropertyText;
    private JComboBox<ComboBoxItem> doubleMapComboBox;
    private JComboBox<ComboBoxItem> floatMapComboBox;
    private JTextField doubleDefaultText;
    private JTextField floatDefaultText;
    private JComboBox<ComboBoxItem> bigDecimalMapComboBox;
    private JTextField bigDecimalDefaultText;
    private JComboBox<ComboBoxItem> localDateMapComboBox;
    private JTextField localDateDefaultText;
    private JComboBox<ComboBoxItem> localTimeMapComboBox;
    private JTextField localTimeDefaultText;
    private JComboBox<ComboBoxItem> localDateTimeMapComboBox;
    private JTextField localDateTimeDefaultText;
    private JPanel mapControlPanel;
    private JRadioButton showNoInMapFieldRadio;

    private JavaBean2DDLSetting.MySettingProperties properties;

    public SettingPanel() {
        JavaBean2DDLSetting service = ServiceManager.getService(JavaBean2DDLSetting.class);
        this.properties = service.myProperties;

        /*自定义映射下拉框初始化*/
        commonlyUsedMapComboBoxInit();

        /*sql类型默认长度文本框初始化*/
        sqlLengthTextInit();

        showNoInMapFieldRadioInit();
    }

    private void showNoInMapFieldRadioInit() {
        showNoInMapFieldRadio.setSelected(properties.getShowNoInMapFieldRadio());
    }

    private void sqlLengthTextInit() {
        intDefaultText.setText(properties.getIntDefaultLength());
        longDefaultText.setText(properties.getLongDefaultLength());
        doubleDefaultText.setText(properties.getDoubleDefaultLength());
        floatDefaultText.setText(properties.getFloatDefaultLength());
        booleanDefaultText.setText(properties.getBooleanDefaultLength());
        dateDefaultText.setText(properties.getDateDefaultLength());
        stringDefaultText.setText(properties.getStringDefaultLength());
        bigDecimalDefaultText.setText(properties.getBigDecimalDefaultLength());
        localDateDefaultText.setText(properties.getLocalDateDefaultLength());
        localTimeDefaultText.setText(properties.getLocalTimeDefaultLength());
        localDateTimeDefaultText.setText(properties.getLocalDateTimeDefaultLength());
    }

    private void commonlyUsedMapComboBoxInit() {
        addSqlItem(intMapComboBox);
        addSqlItem(longMapComboBox);
        addSqlItem(booleanMapComboBox);
        addSqlItem(dateMapComboBox);
        addSqlItem(stringMapComboBox);
        addSqlItem(doubleMapComboBox);
        addSqlItem(floatMapComboBox);
        addSqlItem(bigDecimalMapComboBox);
        addSqlItem(localDateMapComboBox);
        addSqlItem(localTimeMapComboBox);
        addSqlItem(localDateTimeMapComboBox);

        intMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getIntType()))));
        longMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getLongType()))));
        booleanMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getBooleanType()))));
        dateMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getDateType()))));
        stringMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getStringType()))));
        doubleMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getDoubleType()))));
        floatMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getFloatType()))));
        bigDecimalMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getBigDecimalType()))));
        localDateMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getLocalDateType()))));
        localTimeMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getLocalTimeType()))));
        localDateTimeMapComboBox.setSelectedItem(new SqlTypeComboBoxItem(Objects.requireNonNull(SqlTypeEnum.findByType(properties.getLocalDateTimeType()))));

        intMapComboBox.addItemListener(new BaseTypeItemListener(intDefaultText));
        longMapComboBox.addItemListener(new BaseTypeItemListener(longDefaultText));
        booleanMapComboBox.addItemListener(new BaseTypeItemListener(booleanDefaultText));
        dateMapComboBox.addItemListener(new BaseTypeItemListener(dateDefaultText));
        stringMapComboBox.addItemListener(new BaseTypeItemListener(stringDefaultText));
        doubleMapComboBox.addItemListener(new BaseTypeItemListener(doubleDefaultText));
        floatMapComboBox.addItemListener(new BaseTypeItemListener(floatDefaultText));
        bigDecimalMapComboBox.addItemListener(new BaseTypeItemListener(bigDecimalDefaultText));
        localDateMapComboBox.addItemListener(new BaseTypeItemListener(localDateDefaultText));
        localTimeMapComboBox.addItemListener(new BaseTypeItemListener(localTimeDefaultText));
        localDateTimeMapComboBox.addItemListener(new BaseTypeItemListener(localDateTimeDefaultText));
    }

    private void addSqlItem(JComboBox<ComboBoxItem> comboBox) {
        for (SqlTypeEnum typeEnum : SqlTypeEnum.values()) {
            comboBox.addItem(new SqlTypeComboBoxItem(typeEnum));
        }
    }
}
