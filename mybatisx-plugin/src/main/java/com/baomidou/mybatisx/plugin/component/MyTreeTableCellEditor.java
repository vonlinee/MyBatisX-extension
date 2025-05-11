package com.baomidou.mybatisx.plugin.component;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class MyTreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
  /**
   * 要允许打开树，您需要一个 .因此，您将创建一个扩展和实现接口的类。
   * 该类的唯一功能是将双击转发到树。该方法检查是否已单击第一列 （）。
   * 如果是这种情况，双击将转发给 ，以便他们可以做出反应。
   */
  private JTree tree;
  private JTable table;

  public MyTreeTableCellEditor(JTree tree, JTable table) {
    this.tree = tree;
    this.table = table;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
    return tree;
  }

  @Override
  public boolean isCellEditable(EventObject e) {
    if (e instanceof MouseEvent) {
      int colunm1 = 0;
      MouseEvent me = (MouseEvent) e;
      int doubleClick = 2;
      MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - table.getCellRect(0, colunm1, true).x, me.getY(), doubleClick, me.isPopupTrigger());
      tree.dispatchEvent(newME);
    }
    return false;
  }

  @Override
  public Object getCellEditorValue() {
    return null;
  }
}
