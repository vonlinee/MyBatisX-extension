package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;

/**
 * @param <T>
 * @see TreeTable
 * @see com.intellij.ui.components.JBTreeTable
 */
public class JBTreeTableView<T> extends TreeTable {

  protected TreeTableModel treeTableModel;

  public JBTreeTableView(TreeTableModel treeTableModel) {
    super(treeTableModel);
    this.treeTableModel = treeTableModel;
  }

  public final TreeTableModel getTreeTableModel() {
    return treeTableModel;
  }
}
