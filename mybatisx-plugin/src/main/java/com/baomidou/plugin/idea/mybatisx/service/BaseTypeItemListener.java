package com.baomidou.plugin.idea.mybatisx.service;

import com.baomidou.plugin.idea.mybatisx.ddl.SqlTypeEnum;
import com.baomidou.plugin.idea.mybatisx.model.SqlTypeComboBoxItem;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class BaseTypeItemListener implements ItemListener {

    private JTextField jTextField;

    public BaseTypeItemListener(JTextField jTextField) {
        this.jTextField = jTextField;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        String value = ((SqlTypeComboBoxItem) e.getItem()).getValue();
        SqlTypeEnum typeEnum = SqlTypeEnum.findByType(value);
        if (null == typeEnum) {
            return;
        }
        if (typeEnum.getDefaultLengthNeedEmpty()) {
            jTextField.setText("");
            return;
        }
        if (StringUtils.equals(SqlTypeEnum.DECIMAL.getType(), value)) {
            jTextField.setText("(18,2)");
            return;
        }
        if (StringUtils.equals(SqlTypeEnum.TINYINT.getType(), value)) {
            jTextField.setText("(1)");
            return;
        }
        if (StringUtils.equals(SqlTypeEnum.INT.getType(), value)) {
            jTextField.setText("(11)");
            return;
        }
        if (StringUtils.equals(SqlTypeEnum.BIGINT.getType(), value)) {
            jTextField.setText("(20)");
            return;
        }
        if (StringUtils.equals(SqlTypeEnum.VARCHAR.getType(), value)) {
            jTextField.setText("(255)");
            return;
        }
        if (StringUtils.equals(SqlTypeEnum.CHAR.getType(), value)) {
            jTextField.setText("(255)");
        }
    }
}
