package com.baomidou.mybatisx.model;

import com.baomidou.mybatisx.ddl.TranslationAppEnum;

public class TranslationAppComboBoxItem extends ComboBoxItem {

    public TranslationAppComboBoxItem(TranslationAppEnum appEnum) {
        this.name = appEnum.getName();
        this.value = appEnum.getValue();
    }
}
