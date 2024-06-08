package com.baomidou.mybatisx.feat.bean;

import com.baomidou.mybatisx.feat.ddl.SqlTypeEnum;
import com.baomidou.mybatisx.model.ComboBoxItem;

public class SqlTypeComboBoxItem extends ComboBoxItem {

    public SqlTypeComboBoxItem(String name, String value) {
        super(name, value);
    }

    public SqlTypeComboBoxItem(SqlTypeEnum sqlTypeEnum) {
        super(sqlTypeEnum.getType(), sqlTypeEnum.getType());
    }
}
