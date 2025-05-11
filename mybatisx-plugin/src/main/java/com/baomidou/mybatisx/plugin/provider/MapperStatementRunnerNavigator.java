package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.plugin.intention.MyBatisSqlPreviewDialog;
import com.baomidou.mybatisx.util.Icons;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlTag;

import javax.swing.*;

/**
 * 运行SQL语句
 */
public class MapperStatementRunnerNavigator implements MapperNavigator {

  @Override
  public Icon getIcon() {
    return Icons.GUTTER_RUN_ICON;
  }

  @Override
  public String getDisplayText() {
    return "Run";
  }

  @Override
  public void navigate(Project project, MapperStatementItem item) {
    MyBatisSqlPreviewDialog dialog = new MyBatisSqlPreviewDialog(project, item.getNamespace(), (XmlTag) item.getElement().getParent());
    dialog.show();
    dialog.initUI();
  }

  @Override
  public String getNavigationGroupName() {
    return null;
  }
}
