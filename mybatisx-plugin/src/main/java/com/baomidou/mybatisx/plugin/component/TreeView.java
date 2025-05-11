package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * 默认不展示根节点
 *
 * @param <T>
 */
public class TreeView<T> extends Tree {

    private final DefaultMutableTreeNode root;

    public TreeView() {
        root = new DefaultMutableTreeNode();
    }

    public final void addChild(DefaultMutableTreeNode newChild) {
        root.add(newChild);
    }

    public final void addChild(T item) {
        this.getTreeModel().addChild(item);
    }

    @Override
    public void setRootVisible(boolean rootVisible) {
        if (rootVisible) {
            super.setRootVisible(true);
        } else {
            if (getRootNode().getChildCount() > 0) {
                super.setRootVisible(true);
                // 设置根节点展开, 需要有子节点才有效果
                expandRow(0);
                // 隐藏根节点
                super.setRootVisible(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public final TreeModel<T> getTreeModel() {
        return (TreeModel<T>) super.getModel();
    }

    /**
     * 要等到根节点下面有节点后才能设置setRootVisible(false)
     */
    public final void expandRoot() {
        expandRow(0); // 展开根节点，因为根节点已设置为不显示
    }

    public final void expandAll() {
        setRootVisible(true);
        expandRoot();
        setRootVisible(false);
        int rowCount = this.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            this.expandRow(i);
        }
    }

    public final DefaultMutableTreeNode getRootNode() {
        return root;
    }

    protected AnActionButtonRunnable getAddAction() {
        return null;
    }

    protected AnActionButtonRunnable getRemoveAction() {
        return null;
    }

    protected void initToolbarDecoratorExtra(ToolbarDecorator decorator) {
    }

    protected void addActionPanelExtra(@NotNull JPanel actionsPanel) {
    }

    /**
     * 返回的Panel的布局方式是BorderLayout
     *
     * @return 容器
     */
    public final JPanel createPanel() {
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this)
            .setPreferredSize(new Dimension(-1, -1))
            .setAddAction(getAddAction())
            .setRemoveAction(getRemoveAction());
        initToolbarDecoratorExtra(decorator);
        JPanel panel = decorator.createPanel();
        addActionPanelExtra(decorator.getActionsPanel());
        return panel;
    }

    public DefaultMutableTreeNode getLastSelectedNode() {
        return (DefaultMutableTreeNode) getLastSelectedPathComponent();
    }

    @SuppressWarnings("unchecked")
    public T getSelectedItem() {
        DefaultMutableTreeNode node = getLastSelectedNode();
        return (T) node.getUserObject();
    }

    // ===================================== Static Utility Methods ====================================

    @SuppressWarnings("unchecked")
    public static <T> TreeView<T> getTreeView(TreeSelectionEvent event) {
        Object source = event.getSource();
        if (source instanceof TreeView) {
            return (TreeView<T>) source;
        }
        throw new UnsupportedOperationException("event source is not a TreeView.");
    }
}
