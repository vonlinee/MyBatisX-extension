package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public class TextFieldListCellRenderer<E> implements ListCellRenderer<E> {
    @Override
    public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
        // 使用 JTextField 作为单元格
        JTextField textField = new JTextField(value.toString());
        textField.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        textField.setBackground(isSelected ? JBColor.LIGHT_GRAY : JBColor.WHITE);
        textField.setEditable(true);
        return textField;
    }
}
