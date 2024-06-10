package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;

import javax.swing.*;

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
        // 不展示根节点
        JTree tree = this.getTree();
        tree.setShowsRootHandles(true);
    }

    public final TreeTableModel getTreeTableModel() {
        return treeTableModel;
    }
}
