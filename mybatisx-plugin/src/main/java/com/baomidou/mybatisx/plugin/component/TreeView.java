package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.treeStructure.Tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class TreeView<T> extends Tree {

    DefaultMutableTreeNode root;

    public TreeView() {
        root = new DefaultMutableTreeNode("");
        setOpaque(true);
    }

    public void addChild(MutableTreeNode newChild) {
        root.add(newChild);
    }

    public void expandRoot() {
        // 要等到根节点下面有节点后才能设置setRootVisible(false)
        expandRow(0); // 展开根节点，因为根节点已设置为不显示
    }

    public void expandAll() {
        setRootVisible(true);
        expandRoot();
        setRootVisible(false);
        int rowCount = this.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            this.expandRow(i);
        }
    }

    public TreeNode getRoot() {
        return root;
    }
}
