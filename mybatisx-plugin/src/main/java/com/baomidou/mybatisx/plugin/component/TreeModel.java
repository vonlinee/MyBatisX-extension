package com.baomidou.mybatisx.plugin.component;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @param <T>
 * @see TreeView
 */
@SuppressWarnings("unchecked")
public class TreeModel<T> extends DefaultTreeModel {

    private final TreeView<T> treeView;

    public TreeModel(TreeView<T> treeView) {
        super(treeView.getRootNode());
        this.treeView = treeView;
    }

    public final int getChildCountOfRoot() {
        Object root = getRoot();
        if (root == null) {
            return 0;
        }
        return getChildCount(root);
    }

    public T getRootItem() {
        return (T) getRoot();
    }

    public T getChildItem(T parent, int index) {
        return (T) getChild(parent, index);
    }

    /**
     * 将新节点添加到根节点
     *
     * @param data 数据对象
     */
    public void addChild(T data) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(data);
        DefaultMutableTreeNode root = treeView.getRootNode();
        insertNodeInto(newNode, root, root.getChildCount());
    }
}
