package com.baomidou.mybatisx.feat.bean;

import com.baomidou.mybatisx.feat.ddl.TranslationAppEnum;
import com.baomidou.mybatisx.model.ComboBoxItem;

public class TranslationAppComboBoxItem extends ComboBoxItem {

    public TranslationAppComboBoxItem(TranslationAppEnum appEnum) {
        this.name = appEnum.getName();
        this.value = appEnum.getValue();
    }
}
