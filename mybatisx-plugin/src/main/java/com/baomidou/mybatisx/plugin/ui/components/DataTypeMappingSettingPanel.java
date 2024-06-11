package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.ui.SqlDataTypeMappingPanel;

import javax.swing.*;

/**
 * 数据类型映射设置面板
 */
public class DataTypeMappingSettingPanel extends JTabbedPane {
    /**
     * 模板表
     */
    private DataTypeTreeView dataTypeTable;
    private DataTypeMappingTable dataTypeMappingTablePanel;
    private SqlDataTypeMappingPanel sqlDataTypeMappingPanel;

    public DataTypeMappingSettingPanel() {
        sqlDataTypeMappingPanel = new SqlDataTypeMappingPanel();
        dataTypeMappingTablePanel = new DataTypeMappingTable();
        dataTypeTable = new DataTypeTreeView();
        addTab("数据类型", dataTypeTable.createPanel());
        addTab("类型映射", dataTypeMappingTablePanel.createPanel());
        addTab("代码生成类型映射", sqlDataTypeMappingPanel.getRootPanel());
    }
}
