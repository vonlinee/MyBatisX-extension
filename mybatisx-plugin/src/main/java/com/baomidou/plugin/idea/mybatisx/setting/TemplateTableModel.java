package com.baomidou.plugin.idea.mybatisx.setting;

import javax.swing.table.DefaultTableModel;

public class TemplateTableModel extends DefaultTableModel {

    public TemplateTableModel() {
        super(null, new String[]{"ID", "Name", "Path"});
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 2;
    }
}
