package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.plugin.component.BorderPane;
import com.baomidou.mybatisx.plugin.component.Button;
import com.baomidou.mybatisx.plugin.component.HBox;
import com.baomidou.mybatisx.plugin.component.SplitPane;
import com.baomidou.mybatisx.plugin.intention.MapperStatementEditor;
import com.baomidou.mybatisx.plugin.intention.MapperStatementParamTablePane;
import com.baomidou.mybatisx.plugin.intention.ParamImportPane;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SqlPreviewPanel extends JBSplitter {

  SplitPane splitPane;
  MapperStatementParamTablePane paramTablePane;
  MapperStatementEditor editor;
  ParamImportPane paramImportPane;
  BorderPane borderPane;

  public SqlPreviewPanel(Project project) {

    splitPane = new SplitPane(true);
    paramTablePane = new MapperStatementParamTablePane();
    editor = new MapperStatementEditor();

    paramImportPane = new ParamImportPane(project, paramTablePane);
    splitPane.setFirstComponent(paramTablePane);
    splitPane.setRightComponent(paramImportPane);

    setFirstComponent(splitPane);

    borderPane = new BorderPane();

    HBox hBox = new HBox();

    Button button = new Button("Generate");

    button.setAction(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {

        System.out.println(e);
      }
    });

    hBox.add(button);
    borderPane.addToTop(hBox);

    borderPane.addToCenter(editor);
    setSecondComponent(borderPane);
  }
}
