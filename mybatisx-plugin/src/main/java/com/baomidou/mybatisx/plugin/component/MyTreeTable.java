package com.baomidou.mybatisx.plugin.component;

import javax.swing.*;
import java.awt.*;

/**
 * 现在，数据模型、帮助程序组件和 Main 类已就位，但实际的 TreeTable 仍然缺失。该类就是为此目的而创建的。
 * 这继承自 。由于 Java 无法实现多重继承，因此树组件通过关联包含在类中。
 * 数据模型同时传递给（树）和对象（表）。类设置为模型。为了同时选择树和表，使用树和表。
 * 然后，您必须为树设置默认渲染器，并为表设置默认编辑器。
 */
public class MyTreeTable extends JTable {
    private MyTreeTableCellRenderer tree;

    public MyTreeTable(MyAbstractTreeTableModel treeTableModel) {
        super();
        // JTree编写.
        tree = new MyTreeTableCellRenderer(this, treeTableModel);

        // 模型设定.
        super.setModel(new MyTreeTableModelAdapter(treeTableModel, tree));

        // 同时进行同时选择树和表.
        MyTreeTableSelectionModel selectionModel = new MyTreeTableSelectionModel();
        tree.setSelectionModel(selectionModel);
        setSelectionModel(selectionModel.getListSelectionModel());


        // Renderer fuer den Tree.
        setDefaultRenderer(MyTreeTableModel.class, tree);
        // Editor fuer die TreeTable
        setDefaultEditor(MyTreeTableModel.class, new MyTreeTableCellEditor(tree, this));

        // Kein Grid anzeigen.
        setShowGrid(false);

        // Keine Abstaende.
        setIntercellSpacing(new Dimension(0, 0));
    }
}
