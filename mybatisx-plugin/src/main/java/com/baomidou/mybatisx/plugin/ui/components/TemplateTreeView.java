package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.components.TreeModel;
import com.baomidou.mybatisx.plugin.components.TreeView;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 模板树形结构。
 */
class TemplateTreeView extends TreeView<TemplateTreeViewNode> {

  TemplateTreeView() {
    super();
    setModel(new TreeModel<>(this));
    tree.setCellRenderer(new DefaultTreeCellRenderer() {
      @Override
      public java.awt.Component getTreeCellRendererComponent(
        javax.swing.JTree tree,
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus
      ) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof DefaultMutableTreeNode node
          && node.getUserObject() instanceof TemplateTreeViewNode treeNode) {
          setText(treeNode.getName());
        }
        return this;
      }
    });
  }
}
