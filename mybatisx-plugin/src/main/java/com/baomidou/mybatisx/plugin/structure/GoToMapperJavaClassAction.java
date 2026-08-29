package com.baomidou.mybatisx.plugin.structure;

import com.baomidou.mybatisx.util.Icons;
import com.baomidou.mybatisx.util.JavaUtils;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

public final class GoToMapperJavaClassAction extends AnAction {

  public GoToMapperJavaClassAction() {
    super("Go to Mapper Class", "Navigate to the Mapper class declared by the Mapper XML namespace",
      Icons.MAPPER_CLASS_ICON);
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    PsiFile psiFile = getMapperXmlFile(event);
    if (psiFile == null) {
      return;
    }

    XmlTag rootTag = ((XmlFile) psiFile).getRootTag();
    if (rootTag == null) {
      return;
    }

    String namespace = rootTag.getAttributeValue("namespace");
    JavaUtils.findClass(psiFile.getProject(), namespace)
      .ifPresent(clazz -> NavigationUtil.activateFileWithPsiElement(clazz, true));
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    PsiFile mapperXmlFile = getMapperXmlFile(event);
    event.getPresentation().setEnabledAndVisible(mapperXmlFile != null);
  }

  private static PsiFile getMapperXmlFile(@NotNull AnActionEvent event) {
    PsiElement element = event.getData(CommonDataKeys.PSI_ELEMENT);
    PsiFile psiFile = element instanceof PsiFile ? (PsiFile) element
      : element == null ? event.getData(LangDataKeys.PSI_FILE) : element.getContainingFile();
    if (!(psiFile instanceof XmlFile) || !MyBatisMapperXmlStructureViewFactory.isMyBatisMapperXml(psiFile)) {
      return null;
    }
    return psiFile;
  }
}
