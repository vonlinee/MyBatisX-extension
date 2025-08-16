package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.plugin.components.BorderPane;
import com.baomidou.mybatisx.plugin.intention.MyBatisSqlPreviewDialog;
import com.baomidou.mybatisx.util.Icons;
import com.baomidou.mybatisx.util.MyBatisUtils;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MapperXmlSqlRunLineMarkerContributor extends RunLineMarkerContributor {

  @NotNull
  private static String getStatementId(@NotNull PsiElement element) {
    if (element instanceof XmlTag) {
      XmlFile mapperXmlFile = (XmlFile) element.getContainingFile();
      XmlTag rootTag = mapperXmlFile.getRootTag();
      if (rootTag == null) {
        return element.getText();
      }
      String namespace = rootTag.getAttributeValue(MyBatisUtils.NAMESPACE);
      XmlTag el = (XmlTag) element;
      return namespace + "." + el.getAttributeValue("id");
    }
    return element.getText();
  }

  /**
   * this method will be called once at startup only.
   *
   * @param element element
   * @return Info
   */
  @Nullable
  @Override
  public Info getInfo(@NotNull PsiElement element) {
    if (!(element instanceof XmlElement)) {
      return null;
    }
    if (element instanceof XmlTag) {
      if (!MyBatisUtils.isCrudXmlTag(((XmlTag) element).getName())) {
        return null;
      }
      final String statementId = getStatementId(element);
      return new Info(Icons.GUTTER_RUN_ICON_OLD, element1 -> statementId, new LineMarkIconAction(statementId, (XmlTag) element));
    }
    return null;
  }

  private static class LineMarkIconAction extends AnAction implements DumbAware {

    private final String statementId;
    private final XmlTag element;
    private final String fileName;

    public LineMarkIconAction(String statementId, XmlTag element) {
      this.statementId = statementId;
      this.element = element;
      this.fileName = element.getContainingFile().getName();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
      if (e.getProject() == null) {
        return;
      }
      String namespace = MyBatisUtils.getNamespace(element);
      MyBatisSqlPreviewDialog dialog = new MyBatisSqlPreviewDialog(e.getProject(), namespace, element);
      dialog.show();
    }

    @Nullable
    private JComponent createContentPanel(PsiElement element) {
      if (element instanceof XmlTag) {
        return new StatementPanel(element.getProject());
      }
      return null;
    }
  }

  static class StatementPanel extends BorderPane {

    SqlPreviewPanel previewPanel;

    public StatementPanel(Project project) {
      previewPanel = new SqlPreviewPanel(project);
      this.setCenter(previewPanel);
    }
  }
}
