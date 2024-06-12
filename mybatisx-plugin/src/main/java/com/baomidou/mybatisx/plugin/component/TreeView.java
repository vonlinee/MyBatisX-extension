package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;

public class TreeView<T> extends Tree {

    DefaultMutableTreeNode root;

    public TreeView() {
        root = new DefaultMutableTreeNode("11111111");
        setOpaque(true);
    }

    public final void addChild(DefaultMutableTreeNode newChild) {
        root.add(newChild);
    }

    public final void expandRoot() {
        // 要等到根节点下面有节点后才能设置setRootVisible(false)
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

    public final MutableTreeNode getRoot() {
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
}
