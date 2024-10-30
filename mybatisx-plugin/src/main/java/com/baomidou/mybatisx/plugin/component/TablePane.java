package com.baomidou.mybatisx.plugin.component;

import com.intellij.database.run.ui.TableResultPanel;
import com.intellij.database.run.ui.table.TableScrollPane;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;

/**
 * @see TableResultPanel
 * @see TableScrollPane
 * @see TableView
 */
public class TablePane<T> extends ScrollPane {

    TableView<T> tableView;

    public TablePane() {
        super();
        this.tableView = createTableView();
        getViewport().setView(getViewPortView(tableView));
    }

    @NotNull
    protected TableView<T> createTableView() {
        return new TableView<>();
    }

    @NotNull
    protected Component getViewPortView(TableView<T> tableView) {
        return tableView;
    }

    public final void addRows(Collection<T> rowItems) {
        tableView.addRows(rowItems);
    }

    public final void addRow(T rowItem) {
        tableView.addRow(rowItem);
    }

    public int getVisibleRowCount() {
        return tableView.getVisibleRowCount();
    }

    public void setEditingColumn(int i) {
        tableView.setEditingColumn(i);
    }

    public void setEditingRow(int editingRow) {
        tableView.setEditingRow(editingRow);
    }

    public int getSelectedRow() {
        return tableView.getSelectedRow();
    }

    public DefaultListTableModel<T> getTableViewModel() {
        return tableView.getModel();
    }
}
