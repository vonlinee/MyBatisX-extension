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
    private SqlDataTypeMappingPanel sqlDataTypeMappingPanel;

    public DataTypeMappingSettingPanel() {
        sqlDataTypeMappingPanel = new SqlDataTypeMappingPanel();
        addTab("类型映射", sqlDataTypeMappingPanel.getRootPanel());
    }
}
