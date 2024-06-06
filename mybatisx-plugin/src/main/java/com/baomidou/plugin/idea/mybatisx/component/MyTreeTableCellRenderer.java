package com.baomidou.plugin.idea.mybatisx.component;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeModel;
import java.awt.*;

/**
 * 最后，必须创建 JTree 和 JTable。树组件继承自接口并实现接口。
 * 此类可确保树和表的行高相同，并且在选择过程中正确设置背景颜色。此外，还可以确保树的元素根据级别正确缩进
 */
public class MyTreeTableCellRenderer extends JTree implements TableCellRenderer {

    protected int visibleRow;

    private final MyTreeTable treeTable;

    public MyTreeTableCellRenderer(MyTreeTable treeTable, TreeModel model) {
        super(model);
        this.treeTable = treeTable;
        setRowHeight(getRowHeight());
    }

    @Override
    public void setRowHeight(int rowHeight) {
        if (rowHeight > 0) {
            super.setRowHeight(rowHeight);
            if (treeTable != null && treeTable.getRowHeight() != rowHeight) {
                treeTable.setRowHeight(getRowHeight());
            }
        }
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, 0, w, treeTable.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        g.translate(0, -visibleRow * getRowHeight());
        super.paint(g);
    }

    /**
     * Liefert den Renderer mit der passenden Hintergrundfarbe zurueck.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected)
            setBackground(table.getSelectionBackground());
        else
            setBackground(table.getBackground());
        visibleRow = row;
        return this;
    }
}
