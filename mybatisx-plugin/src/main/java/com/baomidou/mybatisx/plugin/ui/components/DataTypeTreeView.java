package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.TreeView;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Objects;

/**
 * 数据类型 树形结构
 * <p>
 * 第一层为类型组ID, 第二层为类型ID
 */
public final class DataTypeTreeView extends TreeView<DataTypeItem> {

    public DataTypeTreeView() {
        super();
        setModel(new Model(getRoot()));

        setShowsRootHandles(false);
    }

    @Override
    protected AnActionButtonRunnable getAddAction() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                addChild(new DefaultMutableTreeNode("java"));
            }
        };
    }

    public void addDataType(String groupId, String identifier) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        DefaultMutableTreeNode newGroupNode = new DefaultMutableTreeNode(groupId);
        DefaultMutableTreeNode newTypeNode = new DefaultMutableTreeNode(identifier);
        newGroupNode.add(newTypeNode);
        root.add(newGroupNode);
    }

    public void addDataTypeWithCheck(String groupId, String identifier) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        int childCount = root.getChildCount();
        DefaultMutableTreeNode node = null;
        for (int i = 0; i < childCount; i++) {
            TreeNode treeNode = root.getChildAt(i);
            if (treeNode instanceof MutableTreeNode) {
                DefaultMutableTreeNode curNode = (DefaultMutableTreeNode) treeNode;
                if (Objects.equals(curNode.getUserObject(), groupId)) {
                    node = curNode;
                    break;
                }
            }
        }
        if (node == null) {
            DefaultMutableTreeNode newGroupNode = new DefaultMutableTreeNode(groupId);
            DefaultMutableTreeNode newTypeNode = new DefaultMutableTreeNode(identifier);
            newGroupNode.add(newTypeNode);
            root.add(newGroupNode);
        } else {
            DefaultMutableTreeNode typeNode = null;
            for (int i = 0; i < node.getChildCount(); i++) {
                TreeNode treeNode = root.getChildAt(i);
                if (treeNode instanceof MutableTreeNode) {
                    DefaultMutableTreeNode curNode = (DefaultMutableTreeNode) treeNode;
                    if (Objects.equals(curNode.getUserObject(), identifier)) {
                        typeNode = curNode;
                        break;
                    }
                }
            }
            if (typeNode == null) {
                DefaultMutableTreeNode newTypeNode = new DefaultMutableTreeNode(identifier);
                node.add(newTypeNode);
            }
        }
    }

    static class Model extends DefaultTreeModel {

        public Model(TreeNode root) {
            super(root);
        }
    }
}
