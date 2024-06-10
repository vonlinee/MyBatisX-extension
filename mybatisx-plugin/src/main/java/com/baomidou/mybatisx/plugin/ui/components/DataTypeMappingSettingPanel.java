package com.baomidou.mybatisx.plugin.ui.components;

import javax.swing.*;
import java.util.Map;

/**
 * 数据类型映射设置面板
 */
public class DataTypeMappingSettingPanel extends JTabbedPane {
    /**
     * 模板表
     */
    private DataTypeTreeTable dataTypeTreeTablePanel;
    private DataTypeTable dataTypeTable;
    private DataTypeMappingTable dataTypeMappingTablePanel;

    public DataTypeMappingSettingPanel() {

        try {
            dataTypeTreeTablePanel = new DataTypeTreeTable();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        dataTypeMappingTablePanel = new DataTypeMappingTable();
        dataTypeTable = new DataTypeTable();
        addTab("数据类型树形结构", dataTypeTreeTablePanel);
        addTab("数据类型", dataTypeTable.createPanel());
        addTab("类型映射", dataTypeMappingTablePanel.createPanel());
    }

    /**
     * 初始化UI
     */
    public void initUI() {


    }

    public void setDataTypeMappingTable(Map<String, Map<String, String>> dataTypeMappingMap) {
//        DataTypeMappingTableModel model = (DataTypeMappingTableModel) dataTypeMappingTable.getModel();
//        for (Map.Entry<String, Map<String, String>> entry : dataTypeMappingMap.entrySet()) {
//            for (Map.Entry<String, String> mappingEntry : entry.getValue().entrySet()) {
//                model.addRow(new Object[]{entry.getKey(), mappingEntry.getKey(), mappingEntry.getValue()});
//            }
//        }
    }
}
