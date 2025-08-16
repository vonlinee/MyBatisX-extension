package com.baomidou.mybatisx.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;

public class SwingUtils {

  public static JPanel newHBoxLayoutPanel() {
    return newBoxLayoutPanel(false);
  }

  public static JPanel newVBoxLayoutPanel() {
    return newBoxLayoutPanel(true);
  }

  /**
   * 创建一个BoxLayout布局的Panel
   *
   * @param vertical vertical
   * @return JPanel
   */
  public static JPanel newBoxLayoutPanel(boolean vertical) {
    JPanel jPanel = new JPanel();
    BoxLayout layout = new BoxLayout(jPanel, vertical ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS);
    jPanel.setLayout(layout);
    return jPanel;
  }

  public static void setPreferredWidth(@NotNull JComponent component, int width) {
    component.setPreferredSize(new Dimension(width, component.getPreferredSize().height));
  }

  public static void setPreferredHeight(JComponent component, int height) {
    component.setPreferredSize(new Dimension(component.getPreferredSize().width, height));
  }

  public static void setEmptyBorder(JComponent component) {
    component.setBorder(BorderFactory.createEmptyBorder());
  }

  /**
   * 设置固定宽度
   *
   * @param column 列
   * @param width  列宽度
   */
  public static void setFixedWidth(TableColumn column, int width) {
    column.setMinWidth(width);
    column.setMaxWidth(width);
    column.setWidth(width);
  }

  /**
   * 设置固定宽度
   *
   * @param table       表格
   * @param columnIndex 列索引
   * @param width       列宽度
   */
  public static void setFixedWidth(JTable table, int columnIndex, int width) {
    int columnCount = table.getColumnCount();
    if (columnIndex < 0 || columnCount <= columnIndex) {
      return;
    }
    TableColumn column = table.getColumnModel().getColumn(columnIndex);
    column.setMinWidth(width);
    column.setMaxWidth(width);
    column.setWidth(width);
    column.setPreferredWidth(width);
  }

  public static Dimension getScreenBasedDimension(double wr, double hr) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    return new Dimension((int) (screenSize.getWidth() * wr), (int) (screenSize.getHeight() * hr));
  }

  /**
   * 根据屏幕尺寸适应的大小
   *
   * @param r 宽度和高度比例
   * @return 尺寸大小
   */
  public static Dimension getScreenBasedDimension(double r) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    return new Dimension((int) (screenSize.getWidth() * r), (int) (screenSize.getHeight() * r));
  }

  public static void invokeLater(Runnable runnable) {
    SwingUtilities.invokeLater(runnable);
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public static <T extends TreeNode> T getSelectedNode(JTree tree) {
    if (tree == null) {
      return null;
    }
    return (T) tree.getSelectionModel().getSelectionPath().getLastPathComponent();
  }

  /**
   * 复制内容到系统剪贴板
   *
   * @param content 文本内容
   */
  public static void copyToClipboard(@NotNull String content) {
    // 封装文本内容
    StringSelection selection = new StringSelection(content);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    // 把文本内容设置到系统剪贴板
    clipboard.setContents(selection, selection);
  }

  /**
   * 鼠标左键单击
   *
   * @param event MouseEvent
   * @return 是否鼠标左键单击
   */
  public static boolean isLeftClicked(MouseEvent event) {
    return SwingUtilities.isLeftMouseButton(event);
  }

  /**
   * 鼠标右键单击
   *
   * @param event MouseEvent
   * @return 是否鼠标右键单击
   */
  public static boolean isRightClicked(MouseEvent event) {
    return SwingUtilities.isRightMouseButton(event);
  }

  /**
   * Creates a horizontal glue component.
   *
   * @return the component
   * @see Box#createHorizontalGlue()
   */
  public static Component createHorizontalGlue(int w) {
    // Dimension min, Dimension pref, Dimension max
    return new Box.Filler(new Dimension(0, 0), new Dimension(w, 0),
      new Dimension(Short.MAX_VALUE, 0));
  }

  /**
   * Creates a vertical glue component.
   *
   * @return the component
   */
  public static Component createVerticalGlue(int h) {
    // Dimension min, Dimension pref, Dimension max
    return new Box.Filler(new Dimension(0, 0), new Dimension(0, h),
      new Dimension(0, Short.MAX_VALUE));
  }

  /**
   * 添加带标题的分组边框
   *
   * @param component 组件
   * @param title     标题文本
   */
  public static void addTitleBorder(@NotNull JComponent component, String title) {
    component.setBorder(new TitledBorder(new EtchedBorder(), title));
  }

  public static Component createStrut(int width) {
    return Box.createHorizontalStrut(width);
  }

  public static boolean isEventThread() {
    return EventQueue.isDispatchThread();
  }

  public static void ensureEventDispatchThread() {
    if (!EventQueue.isDispatchThread()) {
      throw new IllegalStateException("The operation can only be used in event dispatch thread. Current thread: " + Thread.currentThread());
    }
  }

  public static void expandAll(@NotNull JTree tree) {
    for (int i = 0; i < tree.getRowCount(); i++) {
      tree.expandRow(i);
    }
  }

  public static void wrapFilterBox() {
    Box box = Box.createHorizontalBox();
    box.add(new Box.Filler(new Dimension(100, 0), new Dimension(200, 0), new Dimension(Short.MAX_VALUE, 0)));
  }
}
