package com.baomidou.mybatisx.plugin.component;

import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultListTableModel<E> extends ListTableModel<E> {

    private final List<E> items;

    public DefaultListTableModel() {
        super(ColumnInfo.EMPTY_ARRAY, Collections.emptyList(), 0, SortOrder.ASCENDING);
        setItems(items = new ArrayList<>());
    }

    public DefaultListTableModel(ColumnInfo[] columnInfos) {
        super(columnInfos, Collections.emptyList(), 0, SortOrder.ASCENDING);
        setItems(items = new ArrayList<>());
    }

    public final void removeAllRows() {
        int size = items.size();
        items.clear();
        fireTableRowsDeleted(0, size - 1);
    }
}
