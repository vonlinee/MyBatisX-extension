package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.plugin.resultmap.ResultMapGenerationService;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public final class GenerateResultMappingIntention extends MyBatisMapperXmlBaseIntentionAction {

  public GenerateResultMappingIntention() {
  }

  @Override
  public @NotNull String getText() {
    return "Generate ResultMap and Java Class";
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element)
    throws IncorrectOperationException {
    XmlTag select = findSelect(element);
    if (select != null) {
      new ResultMapGenerationService().showDialog(project, select);
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    return isMyBatisMapperXmlFile(element.getContainingFile()) && findSelect(element) != null;
  }

  private static XmlTag findSelect(PsiElement element) {
    XmlTag select = element instanceof XmlTag ? (XmlTag) element : element.getParent() instanceof XmlTag
      ? (XmlTag) element.getParent() : null;
    if (select == null) {
      PsiElement current = element;
      while (current != null && !(current instanceof XmlTag)) {
        current = current.getParent();
      }
      select = current instanceof XmlTag ? (XmlTag) current : null;
    }
    if (select == null || !"select".equalsIgnoreCase(select.getName())) {
      return null;
    }
    XmlAttribute id = select.getAttribute("id");
    if (id == null) {
      return null;
    }
    return isWithin(id, element) ? select : null;
  }

  private static boolean isWithin(PsiElement ancestor, PsiElement element) {
    PsiElement current = element;
    while (current != null) {
      if (current == ancestor) {
        return true;
      }
      current = current.getParent();
    }
    return false;
  }
}
