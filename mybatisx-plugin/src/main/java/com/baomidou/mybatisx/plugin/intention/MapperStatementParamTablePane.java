package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.ComboBoxCellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapperStatementParamTablePane extends JScrollPane {

  MSParamTreeTable table;

  public MapperStatementParamTablePane(Project project) {
    table = new MSParamTreeTable(project);
    JPanel panel = table.getPanel();
    setViewportView(panel);

    TableColumn dataTypeColumn = table.getColumnModel().getColumn(2);
    dataTypeColumn.setCellEditor(new ComboBoxCellEditor() {
      @Override
      protected List<String> getComboBoxItems() {
        return Arrays.asList(ParamDataType.names());
      }
    });
    dataTypeColumn.setMaxWidth(120);
    dataTypeColumn.setMinWidth(120);

    TableColumn paramKeyColumn = table.getColumnModel().getColumn(0);
    paramKeyColumn.setMaxWidth(300);
    paramKeyColumn.setMinWidth(150);
  }

  /**
   * TODO 支持导入模式
   *
   * @param params
   * @param mode
   */
  public void addParams(List<ParamNode> params, ImportModel mode) {
    table.resetAll(params);
  }

  /**
   * 将参数名转化为嵌套Map形式
   *
   * @return 扁平化Map
   */
  public Map<String, Object> getParamsAsMap() {
    return table.getParamsAsMap();
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

  public void setAll(List<ParamNode> paramNodeList) {
    table.setAll(paramNodeList);
  }

  /**
   * 参数表
   *
   * @deprecated
   */
  @Deprecated
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
      return super.getCellEditor(row, column);
    }
  }

  @Deprecated
  private static class TableModel extends DefaultTableModel {

    public TableModel() {
      super(null, new String[]{"Key", "Value", "Type", "JdbcType"});
    }

    /**
     * @param row    行号，从0开始
     * @param column 列号，从0开始
     * @return 是否可编辑
     */
    @Override
    public boolean isCellEditable(int row, int column) {
      return true;
    }
  }
}
