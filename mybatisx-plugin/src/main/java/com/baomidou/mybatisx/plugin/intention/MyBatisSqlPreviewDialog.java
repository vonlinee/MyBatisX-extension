package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.plugin.provider.SqlPreviewPanel;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MyBatisSqlPreviewDialog extends DialogWrapper implements DumbAware {

  private final SqlPreviewPanel panel;

  public MyBatisSqlPreviewDialog(@Nullable Project project, String namespace, XmlTag mapperStatementXmlTag) {
    super(project);
    this.panel = new SqlPreviewPanel(project);
    setModal(false);
    setAutoAdjustable(true);
    setCrossClosesWindow(true);
    this.setTitle("Mapped Statement Sql Preview");
    setOKButtonText("Next");
    setCancelButtonText("Cancel");
    init();
    setStatement(namespace, mapperStatementXmlTag);
  }

  public void setStatement(String namespace, XmlTag mapperStatementXmlTag) {
    panel.setMapperStatement(namespace, mapperStatementXmlTag);
  }

  @Override
  public void show() {
    super.show();
    IntellijSDK.invokeLater(panel::fillMapperStatementParams);
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    return panel;
  }

  /**
   * 该样式会展示在会话框的最下方的位置
   *
   * @return Swing 组件
   */
  @Override
  protected JComponent createSouthPanel() {
    return SqlPreviewPanel.createOperationBox(this.panel);
  }
}
