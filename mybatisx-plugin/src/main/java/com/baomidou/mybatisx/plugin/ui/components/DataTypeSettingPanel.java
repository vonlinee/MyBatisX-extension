package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.TabPane;
import com.baomidou.mybatisx.plugin.setting.DataTypeSettings;

/**
 * 数据类型映射设置面板
 */
public class DataTypeSettingPanel extends TabPane {
  private final DataTypeInfoPane dataTypeInfoPane;
  private final DataTypeMappingPane dataTypeMappingPane;

  public DataTypeSettingPanel() {

    DataTypeSettings dataTypeSettings = DataTypeSettings.getInstance();

    dataTypeMappingPane = new DataTypeMappingPane(dataTypeSettings.getState());

    dataTypeInfoPane = new DataTypeInfoPane(dataTypeSettings.getState(), dataTypeMappingPane);

    addTab("类型信息", dataTypeInfoPane);
    addTab("", dataTypeMappingPane);
  }

  public boolean isModified() {
    return dataTypeInfoPane.isModified() || dataTypeMappingPane.isModified();
  }
}
