package com.baomidou.mybatisx.plugin.components;

import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * 参考: com.intellij.ui.components.JBTreeTable
 *
 * @param <T>
 * @see TreeTable
 */
public class TreeTableView<T> extends TreeTable {

  protected TreeTableModel treeTableModel;

  public TreeTableView(TreeTableModel treeTableModel) {
    super(treeTableModel);
    this.treeTableModel = treeTableModel;
  }

  @Nullable
  public TreePath getSelectedPath() {
    // 通过JTable获取选中的行
    int selectedRow = getSelectedRow();
    // 通过行获取JTree中的TreePath
    return getTree().getPathForRow(selectedRow);
  }

  /**
   * 删除选中行
   */
  public DefaultMutableTreeNode deleteSelectedRow() {
    TreePath selectedPath = getSelectedPath();
    if (selectedPath == null) {
      return null;
    }
    // 删除该Path
    // 获取待删除节点的父节点
    DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
    TreeNode parent = lastNode.getParent();
    DefaultTreeModel treeModel = (DefaultTreeModel) getTree().getModel();
    treeModel.nodeStructureChanged(parent);
    treeModel.removeNodeFromParent(lastNode);
    return lastNode;
  }

  public final TreeTableModel getTreeTableModel() {
    return treeTableModel;
  }
}
