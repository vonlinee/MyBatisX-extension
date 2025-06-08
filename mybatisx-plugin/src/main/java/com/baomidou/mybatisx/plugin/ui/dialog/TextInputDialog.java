package com.baomidou.mybatisx.plugin.ui.dialog;

import com.baomidou.mybatisx.plugin.component.BorderPane;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TextInputDialog extends DialogBase {
  BorderPane panel;
  JBTextArea textArea;

  public TextInputDialog(Project project) {
    super(project); // 使用当前项目上下文
    setTitle("Input Value");
    init();
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    if (textArea == null) {

      // 创建主面板
      BorderPane panel = new BorderPane();

      // 创建文本区域
      textArea = new JBTextArea();
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);

      // 添加滚动条
      JScrollPane scrollPane = new JScrollPane(textArea);
      panel.add(scrollPane, BorderLayout.CENTER);

      // 设置首选大小
      panel.setPreferredSize(new Dimension(400, 300));
    }
    return panel;
  }

  public String getText() {
    return textArea.getText();
  }

  public void setText(String text) {
    textArea.setText(text);
  }
}
