package com.baomidou.mybatisx.feat.bean;

import com.baomidou.mybatisx.feat.ddl.SqlTypeEnum;
import com.baomidou.mybatisx.plugin.component.SimpleComboBoxItem;

public class SqlTypeComboBoxItem extends SimpleComboBoxItem<String> {

  public SqlTypeComboBoxItem(String name, String value) {
    super(name, value, String.class);
  }

  public SqlTypeComboBoxItem(SqlTypeEnum sqlTypeEnum) {
    super(sqlTypeEnum.getType(), sqlTypeEnum.getType(), String.class);
  }
}
