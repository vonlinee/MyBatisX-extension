package com.baomidou.mybatisx.plugin.ui.components;

import javax.swing.*;

/**
 * 数据类型映射设置面板
 */
public class DataTypeSettingPanel extends JTabbedPane {

    private DataTypeMappingTable dataTypeMappingTable;

    public DataTypeSettingPanel() {
        dataTypeMappingTable = new DataTypeMappingTable();

        addTab("类型映射", dataTypeMappingTable.createPanel());
    }
}
