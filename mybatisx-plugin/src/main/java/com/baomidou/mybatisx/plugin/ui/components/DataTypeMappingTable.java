package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.DefaultListTableModel;
import com.baomidou.mybatisx.plugin.component.TablePane;
import com.baomidou.mybatisx.plugin.component.TableView;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        tableView.setModelAndUpdateColumns(new DefaultListTableModel<>(new ColumnInfo[]{
            new ColumnInfo<DataTypeMappingItem, String>("Type") {

                @Override
                public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                    return dataTypeItem.getIdentifier();
                }
            },

            new ColumnInfo<DataTypeMappingItem, String>("Mapped Type") {

                @Override
                public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                    return dataTypeItem.getAnotherIdentifier();
                }
            }
        }));
        return tableView;
    }
}
