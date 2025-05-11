package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

@Setter
@Getter
public class DecoratedTableView<T> extends TableView<T> {

  AnActionButtonRunnable addAction;
  AnActionButtonRunnable removeAction;

  /**
   * 返回的Panel的布局方式是BorderLayout
   *
   * @return 容器
   */
  public final JPanel createPanel() {
    ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this)
      .setPreferredSize(new Dimension(-1, -1))
      .setAddAction(getAddAction())
      .setRemoveAction(getRemoveAction());
    initToolbarDecoratorExtra(decorator);
    JPanel panel = decorator.createPanel();
    addActionPanelExtra(decorator.getActionsPanel());
    return panel;
  }

  protected void initToolbarDecoratorExtra(ToolbarDecorator decorator) {
  }

  protected void addActionPanelExtra(@NotNull JPanel actionsPanel) {
  }
}
