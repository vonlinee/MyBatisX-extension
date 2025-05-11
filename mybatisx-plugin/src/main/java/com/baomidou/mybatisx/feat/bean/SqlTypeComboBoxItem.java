package com.baomidou.mybatisx.feat.bean;

import com.baomidou.mybatisx.feat.ddl.SqlTypeEnum;
import com.baomidou.mybatisx.model.AbstractComboBoxItem;

public class SqlTypeComboBoxItem extends AbstractComboBoxItem {

  public SqlTypeComboBoxItem(String name, String value) {
    super(name, value);
  }

  public SqlTypeComboBoxItem(SqlTypeEnum sqlTypeEnum) {
    this(sqlTypeEnum.getType(), sqlTypeEnum.getType());
  }
}
