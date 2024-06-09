package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.table.JBTable;

import javax.swing.*;

public abstract class TableView extends JScrollPane {

    public TableView() {
        super(new JBTable());
        initTable((JBTable) getViewport().getView());
    }

    public abstract void initTable(JBTable table);
}
