package com.baomidou.mybatisx.plugin.ui.components;

import com.intellij.ui.table.TableView;

public class DataTypeMappingTable extends TableView<DataTypeItem> {

    public DataTypeMappingTable() {
        setModel(new DataTypeMappingTableModel());
    }
}
