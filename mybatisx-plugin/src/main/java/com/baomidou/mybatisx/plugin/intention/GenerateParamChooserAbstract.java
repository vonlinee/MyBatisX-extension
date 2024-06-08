package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.annotation.Annotation;
import com.baomidou.mybatisx.util.JavaUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * The type Generate param chooser.
 *
 * @author yanglin
 */
public class GenerateParamChooserAbstract extends AbstractJavaFileIntentionChooser {

    /**
     * The constant INSTANCE.
     */
    public static final AbstractJavaFileIntentionChooser INSTANCE = new GenerateParamChooserAbstract();

    @Override
    public boolean isAvailable(@NotNull PsiElement element) {
        PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        return (null != parameter && !JavaUtils.isAnnotationPresent(parameter, Annotation.PARAM)) ||
               (null != method && !JavaUtils.isAllParameterWithAnnotation(method, Annotation.PARAM));
    }
}
