package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.feat.bean.TranslationAppComboBoxItem;
import com.baomidou.mybatisx.plugin.component.ComboBox;
import com.intellij.ui.SimpleListCellRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * 翻译选择组件
 */
public class TransalationComboBox extends ComboBox<TranslationAppComboBoxItem> {

    public TransalationComboBox() {
        this.setRenderer(new SimpleListCellRenderer<>() {
            @Override
            public void customize(@NotNull JList<? extends TranslationAppComboBoxItem> list, TranslationAppComboBoxItem value, int index, boolean selected, boolean hasFocus) {
                // 设置选项显示的文本
                setText(value.getName());
            }
        });
    }
}
