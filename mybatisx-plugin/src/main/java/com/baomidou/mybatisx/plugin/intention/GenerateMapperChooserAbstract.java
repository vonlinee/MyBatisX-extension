package com.baomidou.mybatisx.plugin.intention;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * The type Generate mapper chooser.
 *
 * @author yanglin
 */
public class GenerateMapperChooserAbstract extends AbstractJavaFileIntentionChooser {

  /**
   * The constant INSTANCE.
   */
  public static final AbstractJavaFileIntentionChooser INSTANCE = new GenerateMapperChooserAbstract();

  @Override
  public boolean isAvailable(@NotNull PsiElement element) {
    if (isPositionOfInterfaceDeclaration(element)) {
      // ensure parent element is a PsiClass
      PsiClass firstParent = PsiTreeUtil.getParentOfType(element, PsiClass.class);
      if (firstParent != null) {
        if (firstParent.isInterface()) {
          return !isTargetPresentInXml(firstParent);
        }
      }
    }
    return false;
  }

}
