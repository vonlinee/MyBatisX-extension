package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.model.DataTypeMappingSystem;
import com.baomidou.mybatisx.plugin.component.DecoratedTableView;
import com.baomidou.mybatisx.plugin.component.DefaultListTableModel;
import com.baomidou.mybatisx.plugin.component.TablePane;
import com.baomidou.mybatisx.plugin.component.TableView;
import com.baomidou.mybatisx.plugin.ui.dialog.DataTypeMappingUpdateDialog;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.util.ui.ColumnInfo;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 数据类型映射表
 */
@Setter
public class DataTypeMappingConfigTable extends TablePane<DataTypeMappingItem> {

  String currentTypeGroup;
  String currentAnotherTypeGroup;
  DataTypeMappingSystem typeMapping;
  @Getter
  boolean modified;

  public DataTypeMappingConfigTable(DataTypeMappingSystem typeMapping) {
    this.typeMapping = typeMapping;
  }

  public void setGroupMapping(String typeGroup, String anotherTypeGroup) {
    currentTypeGroup = typeGroup;
    currentAnotherTypeGroup = anotherTypeGroup;
  }

  @Override
  protected @NotNull Component getViewPortView(TableView<DataTypeMappingItem> tableView) {
    if (tableView instanceof DecoratedTableView) {
      return ((DecoratedTableView<?>) tableView).createPanel();
    }
    return tableView;
  }

  @Override
  protected @NotNull TableView<DataTypeMappingItem> createTableView() {
    DecoratedTableView<DataTypeMappingItem> tableView = new DecoratedTableView<>();
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

    tableView.setAddAction(new AnActionButtonRunnable() {
      @Override
      public void run(AnActionButton anActionButton) {
        if (StringUtils.hasText(currentTypeGroup, currentAnotherTypeGroup)) {
          DataTypeMappingUpdateDialog dialog = new DataTypeMappingUpdateDialog(currentTypeGroup, currentAnotherTypeGroup) {
            @Override
            protected @NotNull Action getOKAction() {
              return new DialogWrapperAction("Add Mapping") {
                @Override
                protected void doAction(ActionEvent e) {
                  DataTypeMappingItem typeMappingItem = getTypeMappingItem();
                  if (typeMapping.addTypeMappingItem(typeMappingItem)) {
                    addRow(typeMappingItem);
                    markModified();
                  }
                }
              };
            }
          };
          dialog.show();
        }
      }
    });
    tableView.setRemoveAction(new AnActionButtonRunnable() {
      @Override
      public void run(AnActionButton anActionButton) {
        int selectedRow = getSelectedRow();
        DefaultListTableModel<DataTypeMappingItem> model = getTableViewModel();
        DataTypeMappingItem item = model.getItem(selectedRow);
        typeMapping.removeTypeMapping(item);
        model.removeRow(selectedRow);
        markModified();
      }
    });
    return tableView;
  }

  public void markModified() {
    modified = true;
  }
}
