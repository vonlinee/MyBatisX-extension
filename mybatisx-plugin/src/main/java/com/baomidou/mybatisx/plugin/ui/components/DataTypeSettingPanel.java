package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.setting.DataTypeSettings;

import javax.swing.*;

/**
 * 数据类型映射设置面板
 */
public class DataTypeSettingPanel extends JTabbedPane {
    private final DataTypeInfoPane dataTypeInfoPane;
    private final DataTypeMappingPane dataTypeMappingPane;

    public DataTypeSettingPanel() {

        DataTypeSettings dataTypeSettings = DataTypeSettings.getInstance();

        dataTypeInfoPane = new DataTypeInfoPane(dataTypeSettings.getState());
        dataTypeMappingPane = new DataTypeMappingPane(dataTypeSettings.getState());
        addTab("类型信息", dataTypeInfoPane);
        addTab("类型映射", dataTypeMappingPane);
    }
}
