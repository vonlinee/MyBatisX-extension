package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.AnActionButtonRunnable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public interface ToolbarDecoratedComponent<T extends JComponent> {

  @SuppressWarnings("unchecked")
  default T getComponent() {
    return (T) this;
  }

  /**
   * 返回的Panel的布局方式是BorderLayout
   *
   * @return 容器
   */
  default JPanel createPanel() {
    com.intellij.ui.ToolbarDecorator decorator = com.intellij.ui.ToolbarDecorator.createDecorator(getComponent())
      .setPreferredSize(new Dimension(-1, -1))
      .setAddAction(getAddAction())
      .setRemoveAction(getRemoveAction());
    initToolbarDecoratorExtra(decorator);
    JPanel panel = decorator.createPanel();
    addActionPanelExtra(decorator.getActionsPanel());
    return panel;
  }


  default DefaultMutableTreeNode getRootNode() {
    return null;
  }

  default AnActionButtonRunnable getAddAction() {
    return null;
  }

  default AnActionButtonRunnable getRemoveAction() {
    return null;
  }

  default void initToolbarDecoratorExtra(com.intellij.ui.ToolbarDecorator decorator) {
  }

  default void addActionPanelExtra(@NotNull JPanel actionsPanel) {
  }
}
