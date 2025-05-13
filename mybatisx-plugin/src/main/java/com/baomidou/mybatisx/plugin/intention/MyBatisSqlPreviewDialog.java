package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.plugin.provider.SqlPreviewPanel;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyBatisSqlPreviewDialog extends DialogWrapper implements DumbAware {

  SqlPreviewPanel panel;

  public MyBatisSqlPreviewDialog(@Nullable Project project, String namespace, XmlTag mapperStatementXmlTag) {
    super(project);
    setModal(false);
    setAutoAdjustable(true);
    setCrossClosesWindow(true);
    this.setTitle("Mapped Statement Sql Preview");

    panel = new SqlPreviewPanel(project);
    panel.setMapperStatement(namespace, mapperStatementXmlTag);

    setOKButtonText("Next");
    setCancelButtonText("Cancel");

    init();
  }

  /**
   * 弹窗显示后进行初始化操作
   */
  public final void initUI() {
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
    Box box = Box.createHorizontalBox();

    box.add(Box.createHorizontalGlue());

    JButton btnParseParams = new JButton("获取参数");
    btnParseParams.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        panel.fillMapperStatementParams();
      }
    });

    JButton btnGetSql = new JButton("获取SQL");
    btnGetSql.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        panel.fillSqlWithParams();
      }
    });
    box.add(btnParseParams);
    box.add(btnGetSql);
    return box;
  }

}
