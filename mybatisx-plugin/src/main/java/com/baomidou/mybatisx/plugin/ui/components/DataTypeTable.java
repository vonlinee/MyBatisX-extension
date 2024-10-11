package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.TableView;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataTypeTable extends TableView<DataTypeItem> {

    public DataTypeTable() {
        ColumnInfo<DataTypeItem, String> col1 = new ColumnInfo<>("类型分组") {
            @Override
            public @Nullable String valueOf(DataTypeItem item) {
                return item.getGroupId();
            }
        };

        ColumnInfo<DataTypeItem, String> col2 = new ColumnInfo<>("类型名称") {
            @Override
            public @NotNull String valueOf(DataTypeItem item) {
                return item.getIdentifier();
            }
        };

        ListTableModel<DataTypeItem> model = new ListTableModel<>(col1, col2);

        setModelAndUpdateColumns(model);
    }

    @Override
    protected AnActionButtonRunnable getAddAction() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {

            }
        };
    }

    @Override
    protected AnActionButtonRunnable getRemoveAction() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {

            }
        };
    }
}
