package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.feat.bean.SqlTypeComboBoxItem;
import com.baomidou.mybatisx.feat.ddl.SqlTypeEnum;
import com.baomidou.mybatisx.plugin.components.ComboBoxItem;
import com.baomidou.mybatisx.plugin.setting.OtherSetting;
import com.baomidou.mybatisx.service.BaseTypeItemListener;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.intellij.ui.SimpleListCellRenderer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

/**
 * Java类型映射为sql数据类型
 */
@Getter
@Setter
public class SqlDataTypeMappingPanel {
  private JPanel rootPanel;
  private JComboBox<ComboBoxItem> intMapComboBox;
  private JTextField intDefaultText;
  private JComboBox<ComboBoxItem> longMapComboBox;
  private JTextField longDefaultText;
  private JComboBox<ComboBoxItem> stringMapComboBox;
  private JTextField stringDefaultText;
  private JComboBox<ComboBoxItem> booleanMapComboBox;
  private JTextField booleanDefaultText;
  private JComboBox<ComboBoxItem> dateMapComboBox;
  private JTextField dateDefaultText;
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

  private OtherSetting.State properties;

  public SqlDataTypeMappingPanel() {
    this.properties = IntellijSDK.getService(OtherSetting.class).getProperties();
    /*自定义映射下拉框初始化*/
    commonlyUsedMapComboBoxInit();
    /*sql类型默认长度文本框初始化*/
    initSqlLengthText();
  }

  private void initSqlLengthText() {
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
    comboBox.setRenderer(new SimpleListCellRenderer<>() {
      @Override
      public void customize(@NotNull JList<? extends ComboBoxItem> list, ComboBoxItem value, int index, boolean selected, boolean hasFocus) {
        // 设置选项显示的文本
        setText(value.getLabel());
      }
    });
    for (SqlTypeEnum typeEnum : SqlTypeEnum.values()) {
      comboBox.addItem(new SqlTypeComboBoxItem(typeEnum));
    }
  }
}
