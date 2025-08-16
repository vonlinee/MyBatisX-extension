package com.baomidou.mybatisx.plugin.components;

import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TextFieldPane extends JPanel {

  private final EditorTextField editorTextField;
  private final JButton button;

  public TextFieldPane() {
    setLayout(new BorderLayout());
    editorTextField = new EditorTextField(); // 创建EditorTextField实例
    button = new JButton(); // 创建JButton实例
    add(editorTextField, BorderLayout.CENTER); // 将EditorTextField添加到面板中央
    add(button, BorderLayout.EAST); // 将按钮添加到面板的东部（即右侧）
  }

  public String getText() {
    return editorTextField.getText();
  }

  public void setText(String text) {
    editorTextField.setText(text);
  }

  public void setOnButtonClicked(ActionListener listener) {
    ActionListener[] listeners = this.button.getActionListeners();
    for (ActionListener actionListener : listeners) {
      this.button.removeActionListener(actionListener);
    }
    this.button.addActionListener(listener);
  }

  public void setButtonIcon(Icon icon) {
    this.button.setIcon(icon);
  }
}
