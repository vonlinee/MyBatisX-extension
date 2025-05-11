package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.ui.TableViewModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;

/**
 * 必须把JTable添加到JScrollPane控件中，否则默认不显示列标题
 */
public class TableView<T> extends com.intellij.ui.table.TableView<T> {

  public TableView() {
    super(new DefaultListTableModel<>());
  }


  public final void addRow(T rowItem) {
    TableViewModel<T> model = getTableViewModel();
    if (model instanceof ListTableModel) {
      ((ListTableModel<T>) model).addRow(rowItem);
    }
  }

  public final void addRows(Collection<T> rowItems) {
    TableViewModel<T> model = getTableViewModel();
    if (model instanceof ListTableModel) {
      ((ListTableModel<T>) model).addRows(rowItems);
    }
  }

  @Override
  @NotNull
  public DefaultListTableModel<T> getModel() {
    return (DefaultListTableModel<T>) super.getListTableModel();
  }

  public final void clearAllRows() {
    DefaultListTableModel<T> listTableModel = getModel();
    listTableModel.removeAllRows();
  }
}
