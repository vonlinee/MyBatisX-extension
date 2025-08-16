package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.feat.bean.TemplateInfo;
import com.baomidou.mybatisx.plugin.components.TableView;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 模板信息表
 */
public class TemplateTableView extends TableView<TemplateInfo> {

  public TemplateTableView() {
    ColumnInfo<TemplateInfo, String> col1 = new ColumnInfo<>("模板ID") {
      @Override
      public @Nullable String valueOf(TemplateInfo templateInfo) {
        return templateInfo.getId();
      }

      @Override
      public int getWidth(JTable table) {
        return 50;
      }
    };
    ColumnInfo<TemplateInfo, String> col2 = new ColumnInfo<>("模板名称") {
      @Override
      public @Nullable String valueOf(TemplateInfo templateInfo) {
        return templateInfo.getName();
      }

      @Override
      public int getWidth(JTable table) {
        return 150;
      }
    };
    ColumnInfo<TemplateInfo, String> col3 = new ColumnInfo<>("模板路径") {
      @Override
      public @Nullable String valueOf(TemplateInfo templateInfo) {
        return templateInfo.getPath();
      }
    };
    setModelAndUpdateColumns(new ListTableModel<>(col1, col2, col3));
  }
}
