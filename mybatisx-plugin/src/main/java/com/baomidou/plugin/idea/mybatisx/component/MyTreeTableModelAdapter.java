package com.baomidou.plugin.idea.mybatisx.component;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

public class MyTreeTableModelAdapter extends AbstractTableModel {
    /**
     * 由于使用 Java Swing 时，GUI 组件仍然需要一个模型，该模型不等于实际的数据模型，因此现在创建了一个继承自 的类。
     * 此类稍后在类中用作 的模型。如果 TreeTable 稍后要求显示值，
     * 则必须区分请求的值是由树提供还是直接由数据模型提供。此外，在类中创建并注册。
     * 这会对树中的单击做出反应，并确保树被打开和关闭。
     */
    JTree tree;
    MyAbstractTreeTableModel treeTableModel;

    public MyTreeTableModelAdapter(MyAbstractTreeTableModel treeTableModel, JTree tree) {
        this.tree = tree;
        this.treeTableModel = treeTableModel;

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                fireTableDataChanged();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                fireTableDataChanged();
            }
        });
    }


    @Override
    public int getColumnCount() {
        return treeTableModel.getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        return treeTableModel.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return treeTableModel.getColumnClass(column);
    }

    @Override
    public int getRowCount() {
        return tree.getRowCount();
    }

    protected Object nodeForRow(int row) {
        TreePath treePath = tree.getPathForRow(row);
        return treePath.getLastPathComponent();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return treeTableModel.getValueAt(nodeForRow(row), column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return treeTableModel.isCellEditable(nodeForRow(row), column);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        treeTableModel.setValueAt(value, nodeForRow(row), column);
    }
}
