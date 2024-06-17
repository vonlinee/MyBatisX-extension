package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.TreeView;
import com.baomidou.mybatisx.plugin.ui.dialog.DataTypeAddDialog;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * 数据类型 树形结构
 * <p>
 * 第一层为类型组ID, 第二层为类型ID
 */
public final class DataTypeTreeView extends TreeView<DataTypeItem> {

    DataTypeAddDialog dialog;

    public DataTypeTreeView() {
        super();
        setModel(new Model(getRoot()));
        // 设置不显示根节点需要在setModel调用之后调用才行
        setRootVisible(false);

        initDefaultDataTypes();
    }

    @Override
    protected AnActionButtonRunnable getAddAction() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                if (dialog == null || dialog.isDisposed()) {
                    dialog = new DataTypeAddDialog(DataTypeTreeView.this);
                }
                dialog.show();
            }
        };
    }

    private void initDefaultDataTypes() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();

        DataTypeNode javaGroup = new DataTypeNode("Java");
        javaGroup.addChildDataType(int.class.getName());
        javaGroup.addChildDataType(short.class.getName());
        javaGroup.addChildDataType(byte.class.getName());
        javaGroup.addChildDataType(char.class.getName());
        javaGroup.addChildDataType(long.class.getName());
        javaGroup.addChildDataType(double.class.getName());
        javaGroup.addChildDataType(float.class.getName());
        javaGroup.addChildDataType(boolean.class.getName());

        javaGroup.addChildDataType(Integer.class.getName());
        javaGroup.addChildDataType(Short.class.getName());
        javaGroup.addChildDataType(Byte.class.getName());
        javaGroup.addChildDataType(Character.class.getName());
        javaGroup.addChildDataType(Long.class.getName());
        javaGroup.addChildDataType(Double.class.getName());
        javaGroup.addChildDataType(Float.class.getName());
        javaGroup.addChildDataType(Boolean.class.getName());

        javaGroup.addChildDataType(BigDecimal.class.getName());
        javaGroup.addChildDataType(String.class.getName());
        javaGroup.addChildDataType(CharSequence.class.getName());
        javaGroup.addChildDataType(BigInteger.class.getName());
        javaGroup.addChildDataType(Date.class.getName());
        javaGroup.addChildDataType(LocalDateTime.class.getName());
        javaGroup.addChildDataType(LocalDate.class.getName());

        root.add(javaGroup);
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
