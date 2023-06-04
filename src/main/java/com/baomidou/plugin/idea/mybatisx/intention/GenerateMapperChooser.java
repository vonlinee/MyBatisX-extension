package com.baomidou.plugin.idea.mybatisx.intention;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * The type Generate mapper chooser.
 * @author yanglin
 */
public class GenerateMapperChooser extends JavaFileIntentionChooser {

    /**
     * The constant INSTANCE.
     */
    public static final JavaFileIntentionChooser INSTANCE = new GenerateMapperChooser();

    @Override
    public boolean isAvailable(@NotNull PsiElement element) {
        if (isPositionOfInterfaceDeclaration(element)) {
            // ensure parent element is a PsiClass
            PsiElement firstParent = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            if (firstParent != null) {
                PsiClass psiClass = (PsiClass) firstParent;
                if (psiClass.isInterface()) {
                    return !isTargetPresentInXml(psiClass);
                }
            }
        }
        return false;
    }

}
