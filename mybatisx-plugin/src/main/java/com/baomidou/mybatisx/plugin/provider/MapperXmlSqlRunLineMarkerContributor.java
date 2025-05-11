package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.plugin.component.SplitPane;
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
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

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
      return new Info(Icons.GUTTER_RUN_ICON_OLD, element1 -> statementId, new LineMarkIconAction(statementId, element));
    }
    return null;
  }

  static class LineMarkIconAction extends AnAction implements DumbAware {

    private final String statementId;
    private final PsiElement element;
    private final String fileName;

    public LineMarkIconAction(String statementId, PsiElement element) {
      this.statementId = statementId;
      this.element = element;
      this.fileName = element.getContainingFile().getName();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
      if (e.getProject() == null) {
        return;
      }

      InputEvent inputEvent = e.getInputEvent();
      if (inputEvent instanceof MouseEvent) {
        String namespace = MyBatisUtils.getNamespace((XmlElement) element);
        MyBatisSqlPreviewDialog dialog = new MyBatisSqlPreviewDialog(e.getProject(), namespace, (XmlTag) element);
        dialog.show();
        dialog.initUI();
      }


//            ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow(MyBatisToolWindowView.NAME);
//            if (toolWindow == null) {
//                return;
//            }
//            ContentManager contentManager = toolWindow.getContentManager();
//
//            final int contentCount = contentManager.getContentCount();
//            if (contentCount == 1) {
//                // Statement
//                Content content = contentManager.getFactory().createContent(createContentPanel(element), "Statements", false);
//                content.setCloseable(false);
//                contentManager.addContent(content);
//                contentManager.setSelectedContent(content);
//            } else {
//                //
//                Content content = contentManager.getContent(1);
//                if (content != null) {
//                    StatementPanel statementPanel = (StatementPanel) content.getComponent();
//                    statementPanel.fileTree.addStatement(element);
//                }
//            }
//
//            if (toolWindow.isVisible()) {
//                toolWindow.show();
//            } else {
//                toolWindow.activate(() -> {
//                });
//            }
    }

    @Nullable
    private JComponent createContentPanel(PsiElement element) {
      if (element instanceof XmlTag) {
        StatementPanel statementPanel = new StatementPanel(element.getProject());
        statementPanel.fileTree.addStatement(element);
        return statementPanel;
      }
      return null;
    }
  }

  static class StatementPanel extends SplitPane {

    MapperFileTree fileTree;
    SqlPreviewPanel previewPanel;


    public StatementPanel(Project project) {

      fileTree = new MapperFileTree();
      fileTree.setPreferredSize(new Dimension(200, 400));

      previewPanel = new SqlPreviewPanel(project);

      this.setLeftComponent(fileTree);
      this.setRightComponent(previewPanel);
    }
  }
}
