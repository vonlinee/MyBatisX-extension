package com.baomidou.plugin.idea.mybatisx.model;

import com.baomidou.plugin.idea.mybatisx.enums.SqlTypeEnum;

public class SqlTypeComboBoxItem extends ComboBoxItem {

    public SqlTypeComboBoxItem(String name, String value) {
        super(name, value);
    }

    public SqlTypeComboBoxItem(SqlTypeEnum sqlTypeEnum) {
        super(sqlTypeEnum.getType(), sqlTypeEnum.getType());
    }
}
