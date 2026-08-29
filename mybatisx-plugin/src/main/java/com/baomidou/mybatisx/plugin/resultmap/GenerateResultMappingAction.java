package com.baomidou.mybatisx.plugin.resultmap;

import com.baomidou.mybatisx.plugin.structure.MyBatisMapperXmlStructureViewFactory;
import com.baomidou.mybatisx.util.Icons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

public final class GenerateResultMappingAction extends AnAction {

  public GenerateResultMappingAction() {
    super("Generate ResultMap and Java Class",
      "Generate a resultMap and, when needed, a Java result class from this select",
      Icons.MAPPER_CLASS_ICON);
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    XmlTag select = getSelectTag(event);
    Project project = event.getProject();
    if (select != null && project != null) {
      new ResultMapGenerationService().showDialog(project, select);
    }
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    event.getPresentation().setEnabledAndVisible(getSelectTag(event) != null);
  }

  private static XmlTag getSelectTag(AnActionEvent event) {
    PsiElement element = event.getData(CommonDataKeys.PSI_ELEMENT);
    if (element instanceof XmlTag && "select".equalsIgnoreCase(((XmlTag) element).getName())) {
      PsiFile file = element.getContainingFile();
      return file instanceof XmlFile && MyBatisMapperXmlStructureViewFactory.isMyBatisMapperXml(file)
             ? (XmlTag) element : null;
    }
    return null;
  }
}
