package com.baomidou.mybatisx.plugin.component;

import javax.swing.tree.TreeModel;

public interface MyTreeTableModel extends TreeModel {
    /**
     * 返回可用列的数量。
     *
     * @return Number of Columns
     */
    int getColumnCount();

    /**
     * 返回列名。
     *
     * @param column Column number
     * @return Column name
     */
    String getColumnName(int column);


    /**
     * 返回列的类型(类)。
     *
     * @param column Column number
     * @return Class
     */
    Class<?> getColumnClass(int column);

    /**
     * 返回列中节点的值。
     *
     * @param node   Node
     * @param column Column number
     *               返回列中节点的值
     */
    Object getValueAt(Object node, int column);


    /**
     * 检查某列中节点的单元格是否可编辑。
     *
     * @param node   Node
     * @param column Column number
     * @return true/false
     */
    boolean isCellEditable(Object node, int column);

    /**
     * 为一列中的节点设置值
     *
     * @param aValue New value
     * @param node   Node
     * @param column Column number
     */
    void setValueAt(Object aValue, Object node, int column);
}
