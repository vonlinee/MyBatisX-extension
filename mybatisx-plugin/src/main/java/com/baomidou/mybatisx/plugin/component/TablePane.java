package com.baomidou.mybatisx.plugin.component;

import com.intellij.database.run.ui.TableResultPanel;
import com.intellij.database.run.ui.table.TableScrollPane;
import com.intellij.ui.AnActionButtonRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @see TableResultPanel
 * @see TableScrollPane
 * @see TableView
 */
public class TablePane<T> extends ScrollPane {

    TableView<T> tableView;

    @SuppressWarnings("unchecked")
    public TablePane() {
        super();
        getViewport().setView(createTableView());
        this.tableView = (TableView<T>) getViewport().getView();
    }

    @NotNull
    protected TableView<T> createTableView() {
        return new TableView<T>() {
            @Override
            protected AnActionButtonRunnable getAddAction() {
                return super.getAddAction();
            }
        };
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
}
