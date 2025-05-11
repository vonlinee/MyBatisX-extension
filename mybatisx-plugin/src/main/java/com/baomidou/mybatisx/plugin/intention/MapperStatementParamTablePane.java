package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.util.MapUtils;
import com.intellij.util.ui.ComboBoxCellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperStatementParamTablePane extends JScrollPane {

  public static final List<String> DATA_TYPE_NAMES = Arrays.asList(ParamDataType.names());
  ParamTable table;

  public MapperStatementParamTablePane() {
    super(new ParamTable());

    table = (ParamTable) getViewport().getView();

    TableColumn dataTypeColumn = table.getColumnModel().getColumn(2);
    dataTypeColumn.setCellEditor(new ComboBoxCellEditor() {
      @Override
      protected List<String> getComboBoxItems() {
        return DATA_TYPE_NAMES;
      }
    });
    dataTypeColumn.setMaxWidth(120);
    dataTypeColumn.setMinWidth(120);

    TableColumn paramKeyColumn = table.getColumnModel().getColumn(0);
    paramKeyColumn.setMaxWidth(300);
    paramKeyColumn.setMinWidth(150);
  }

  public final void setAll(List<ParamNode> params) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0); // 清空表格的数据
    for (ParamNode param : params) {
      Object[] row = {param.getKey(), param.getValue(), param.getDataType().getLabel()};
      model.addRow(row);
    }
  }

  public void addParams(Map<String, ParamNode> params, ImportModel mode) {
    TableModel model = (TableModel) table.getModel();
    if (mode == ImportModel.APPEND) {
      for (Map.Entry<String, ParamNode> entry : params.entrySet()) {
        ParamNode node = entry.getValue();
        model.addRow(new Object[]{entry.getKey(), node.getValue(), node.getDataType()});
      }
    } else if (mode == ImportModel.OVERRIDE) {
      model.setRowCount(0);
      for (Map.Entry<String, ParamNode> entry : params.entrySet()) {
        ParamNode node = entry.getValue();
        model.addRow(new Object[]{entry.getKey(), node.getValue(), node.getDataType()});
      }
    } else if (mode == ImportModel.MERGE_OVERRIDE) { // 合并且覆盖
      Map<String, String> paramKeyValues = new HashMap<>();
      Map<String, Integer> keyRowNumMap = new HashMap<>();
      int rowCount = model.getRowCount();
      for (int i = 0; i < rowCount; i++) {
        String key = String.valueOf(model.getValueAt(i, 0));
        paramKeyValues.put(key, String.valueOf(model.getValueAt(i, 1)));
        keyRowNumMap.put(key, i);
      }
      for (Map.Entry<String, ParamNode> entry : params.entrySet()) {
        ParamNode node = entry.getValue();
        if (paramKeyValues.containsKey(entry.getKey())) {
          Integer rowNum = keyRowNumMap.get(entry.getKey());
          model.setValueAt(node.getValue(), rowNum, 1);
          model.setValueAt(node.getDataType(), rowNum, 2);
        } else {
          model.addRow(new Object[]{entry.getKey(), node.getValue(), node.getDataType()});
        }
      }
    } else if (mode == ImportModel.MERGE) { // 仅合并
      Map<String, String> paramKeyValues = new HashMap<>();
      int rowCount = model.getRowCount();
      for (int i = 0; i < rowCount; i++) {
        String key = String.valueOf(model.getValueAt(i, 0));
        paramKeyValues.put(key, String.valueOf(model.getValueAt(i, 1)));
      }
      for (Map.Entry<String, ParamNode> entry : params.entrySet()) {
        ParamNode node = entry.getValue();
        if (!paramKeyValues.containsKey(entry.getKey())) {
          model.addRow(new Object[]{entry.getKey(), node.getValue(), node.getDataType()});
        }
      }
    }
  }

  /**
   * 将参数名转化为嵌套Map形式
   *
   * @return 扁平化Map
   */
  public Map<String, Object> getParamsAsMap() {
    Map<String, Object> map = new HashMap<>();
    TableModel tableModel = (TableModel) table.getModel();
    int rowCount = tableModel.getRowCount();
    Map<String, ParamDataType> dataTypeNameMap = ParamDataType.asMap();

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < rowCount; i++) {
      String dataType = (String) tableModel.getValueAt(i, 2);
      ParamDataType type = dataTypeNameMap.getOrDefault(dataType, ParamDataType.UNKNOWN);
      String value = (String) tableModel.getValueAt(i, 1);
      map.put((String) tableModel.getValueAt(i, 0), type.parseObject(value, sb));
    }
    map = MapUtils.expandKeys(map, "\\.");
    return map;
  }

  /**
   * 校验表格数据
   *
   * @return 校验成功返回空集合，失败返回错误信息
   */
  public List<String> validateParams() {
    TableModel tableModel = (TableModel) table.getModel();
    int rowCount = tableModel.getRowCount();

    return Collections.emptyList();
  }

  /**
   * 参数表
   */
  static class ParamTable extends JTable {

    public ParamTable() {
      super(new TableModel());
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
      if (column != 1) {
        return super.getCellEditor(row, column);
      }
      // 根据数据类型来确定不同的参数值编辑器
      Object dataType = getModel().getValueAt(row, column + 1);
      if (dataType == null) {
        return super.getCellEditor(row, column);
      }
      return super.getCellEditor(row, column);
    }
  }

  private static class TableModel extends DefaultTableModel {

    public TableModel() {
      super(null, new String[]{"Key", "Value", "Type"});
    }

    /**
     * @param row    行号，从0开始
     * @param column 列号，从0开始
     * @return 是否可编辑
     */
    @Override
    public boolean isCellEditable(int row, int column) {
      return column != 0;
    }
  }
}
