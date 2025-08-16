package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.util.IntellijSDK;
import com.intellij.openapi.util.IconLoader;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.Objects;

/**
 * @see com.baomidou.mybatisx.util.IntellijSDK
 * @see com.baomidou.mybatisx.util.SwingUtils
 */
public final class UIHelper {

  private UIHelper() {
  }

  public static GridBag newGridBagLayoutConstraints() {
    return new GridBag()
      .setDefaultAnchor(0, GridBagConstraints.EAST)
      .setDefaultAnchor(1, GridBagConstraints.WEST)
      .setDefaultWeightX(1, 1)
      .setDefaultFill(GridBagConstraints.HORIZONTAL);
  }

  public static GridConstraints newGridLayoutConstraints() {
    return new GridConstraints();
  }

  public static void setEmptyBorder(JComponent component, int top, int left, int bottom, int right) {
    component.setBorder(JBUI.Borders.empty(top, left, bottom, right));
  }

  public static void setEmptyBorder(JComponent component, int topLeftBottomRight) {
    component.setBorder(JBUI.Borders.empty(topLeftBottomRight, topLeftBottomRight, topLeftBottomRight, topLeftBottomRight));
  }

  public static void setEmptyBorder(JComponent component) {
    component.setBorder(BorderFactory.createEmptyBorder());
  }

  public static Icon getIcon(String name) {
    name = "/icons/" + name;
    return IconLoader.getIcon(name, Objects.requireNonNull(IntellijSDK.getCallerClass(), "caller class is null"));
  }

  /**
   * 创建一个图标按钮
   *
   * @param icon 图标路径
   * @return 图标按钮
   */
  public static JButton newIconButton(String icon) {
    JButton button = new JButton();
    Icon iconNode = UIHelper.getIcon(icon);
    button.setIcon(iconNode);
    button.setHorizontalTextPosition(JButton.CENTER); // 文本位置设置为居中（如果不需要文本，可以忽略此行代码）
    button.setVerticalTextPosition(JButton.CENTER); // 文本位置设置为居中（如果不需要文本，可以忽略此行代码）
    button.setText(""); // 如果不需要文本，确保将文本设置为空字符串
    // 为了确保图标居中，可以调整按钮的Insets
    Insets insets = button.getInsets();
    int iconWidth = iconNode.getIconWidth();
    int iconHeight = iconNode.getIconHeight();
    int buttonWidth = button.getWidth();
    int buttonHeight = button.getHeight();
    int hGap = (buttonWidth - iconWidth) / 2;
    int vGap = (buttonHeight - iconHeight) / 2;
    button.setMargin(JBUI.insets(vGap, hGap, vGap, hGap)); // 设置边距来居中图标
    return button;
  }

  /**
   * 获取编辑器提交的文本
   *
   * @param e 事件
   * @return 文本
   */
  public static String getCellEditorStringValue(@NotNull ChangeEvent e) {
    Object source = e.getSource();
    DefaultCellEditor editor = (DefaultCellEditor) source;
    Object cellEditorValue = editor.getCellEditorValue();
    return String.valueOf(cellEditorValue);
  }
}
