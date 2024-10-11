package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.ComboBoxCellEditor;
import com.baomidou.mybatisx.plugin.component.TablePane;
import com.baomidou.mybatisx.plugin.component.TableView;
import com.baomidou.mybatisx.util.ArrayUtils;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.sql.JDBCType;

/**
 * 数据类型映射表
 */
public class DataTypeMappingTable extends TablePane<DataTypeMappingItem> {

    @Override
    protected @NotNull TableView<DataTypeMappingItem> createTableView() {
        TableView<DataTypeMappingItem> tableView = new TableView<>() {
            @Override
            protected AnActionButtonRunnable getAddAction() {
                return anActionButton -> {
                    DataTypeMappingItem item = new DataTypeMappingItem();
                    item.setJdbcTypeEnum(JDBCType.VARCHAR);
                    addRow(item);
                    setEditingColumn(0);
                    setEditingRow(getVisibleRowCount());
                };
            }

            @Override
            protected AnActionButtonRunnable getRemoveAction() {
                return anActionButton -> {
                    int selectedRow = getSelectedRow();
                    remove(selectedRow);
                };
            }
        };

        tableView.setModelAndUpdateColumns(new ListTableModel<>(new ColumnInfo[]{
                new ColumnInfo<DataTypeMappingItem, String>("Type") {

                    @Override
                    public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                        return dataTypeItem.getIdentifier();
                    }

                    @Override
                    public boolean isCellEditable(DataTypeMappingItem item) {
                        return true;
                    }
                },

                new ColumnInfo<DataTypeMappingItem, String>("Mapped Type") {

                    @Override
                    public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                        return dataTypeItem.getAnotherIdentifier();
                    }

                    @Override
                    public boolean isCellEditable(DataTypeMappingItem item) {
                        return true;
                    }

                    @Override
                    public @NotNull TableCellEditor getEditor(DataTypeMappingItem item) {
                        ComboBoxCellEditor editor = new ComboBoxCellEditor(ArrayUtils.asList(JDBCType.values(), JDBCType::getName));
                        editor.addCellEditorListener(new CellEditorListener() {
                            @Override
                            public void editingStopped(ChangeEvent e) {
                                ComboBoxCellEditor eventCellEditor = (ComboBoxCellEditor) e.getSource();
                                JComboBox<?> comboBox = eventCellEditor.getComboBox();
                                Object selectedItem = comboBox.getSelectedItem();
                                item.setJdbcType(String.valueOf(selectedItem));
                            }

                            @Override
                            public void editingCanceled(ChangeEvent e) {

                            }
                        });
                        return editor;
                    }
                },
        }));
        return tableView;
    }
}
