package com.baomidou.mybatisx.model;

import com.baomidou.mybatisx.ddl.SqlTypeEnum;

public class SqlTypeComboBoxItem extends ComboBoxItem {

    public SqlTypeComboBoxItem(String name, String value) {
        super(name, value);
    }

    public SqlTypeComboBoxItem(SqlTypeEnum sqlTypeEnum) {
        super(sqlTypeEnum.getType(), sqlTypeEnum.getType());
    }
}
