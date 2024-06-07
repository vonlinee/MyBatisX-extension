package com.baomidou.plugin.idea.mybatisx.model;

import com.baomidou.plugin.idea.mybatisx.enums.TranslationAppEnum;

public class TranslationAppComboBoxItem extends ComboBoxItem {

    public TranslationAppComboBoxItem(TranslationAppEnum appEnum) {
        this.name = appEnum.getName();
        this.value = appEnum.getValue();
    }
}
