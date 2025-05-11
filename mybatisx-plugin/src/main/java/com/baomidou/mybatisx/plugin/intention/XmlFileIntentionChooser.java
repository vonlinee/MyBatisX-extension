package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.service.JavaService;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;

/**
 * The type xml file intention chooser.
 */
public abstract class XmlFileIntentionChooser implements IntentionChooser {

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return file instanceof XmlFile;
  }

  /**
   * Is available boolean.
   *
   * @param element the element
   * @return the boolean
   */
  public abstract boolean isAvailable(@NotNull PsiElement element);

  /**
   * Is position of parameter declaration boolean.
   *
   * @param element the element
   * @return the boolean
   */
  public boolean isPositionOfParameterDeclaration(@NotNull PsiElement element) {
    return element.getParent() instanceof PsiParameter;
  }

  /**
   * Is position of method declaration boolean.
   *
   * @param element the element
   * @return the boolean
   */
  public boolean isPositionOfMethodDeclaration(@NotNull PsiElement element) {
    return element.getParent() instanceof PsiMethod;
  }

  /**
   * Is position of interface declaration boolean.
   *
   * @param element the element
   * @return the boolean
   */
  public boolean isPositionOfInterfaceDeclaration(@NotNull PsiElement element) {
    return element.getParent() instanceof PsiClass;
  }

  /**
   * Is target present in xml boolean.
   *
   * @param psiClass the element
   * @return the boolean
   */
  public boolean isTargetPresentInXml(@NotNull PsiClass psiClass) {
    return JavaService.getInstance(psiClass.getProject()).findWithFindFirstProcessor(psiClass).isPresent();
  }
}
