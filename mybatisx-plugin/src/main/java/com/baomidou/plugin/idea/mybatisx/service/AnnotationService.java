package com.baomidou.plugin.idea.mybatisx.service;

import com.baomidou.plugin.idea.mybatisx.annotation.Annotation;
import com.baomidou.plugin.idea.mybatisx.util.JavaUtils;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.jetbrains.annotations.NotNull;

/**
 * The type Annotation service.
 *
 * @author yanglin
 */
public class AnnotationService {

    private final Project project;

    /**
     * Instantiates a new Annotation service.
     *
     * @param project the project
     */
    public AnnotationService(Project project) {
        this.project = project;
    }

    /**
     * Gets instance.
     *
     * @param project the project
     * @return the instance
     */
    public static AnnotationService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, AnnotationService.class);
    }

    /**
     * Add annotation.
     *
     * @param parameter  the parameter
     * @param annotation the annotation
     */
    public void addAnnotation(@NotNull PsiModifierListOwner parameter, @NotNull Annotation annotation) {
        PsiModifierList modifierList = parameter.getModifierList();
        if (JavaUtils.isAnnotationPresent(parameter, annotation) || null == modifierList) {
            return;
        }
        JavaService.getInstance(parameter.getProject())
            .importClazz((PsiJavaFile) parameter.getContainingFile(), annotation.getQualifiedName());

        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiAnnotation psiAnnotation = elementFactory.createAnnotationFromText(annotation.toString(), parameter);
        modifierList.add(psiAnnotation);
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiAnnotation.getParent());
    }

    /**
     * Add annotation with parameter name for method parameters.
     *
     * @param method the method
     */
    public void addAnnotationWithParameterNameForMethodParameters(@NotNull PsiMethod method) {
        PsiParameterList parameterList = method.getParameterList();
        PsiParameter[] parameters = parameterList.getParameters();
        for (PsiParameter param : parameters) {
            addAnnotationWithParameterName(param);
        }
    }

    /**
     * Add annotation with parameter name.
     *
     * @param parameter the parameter
     */
    public void addAnnotationWithParameterName(@NotNull PsiParameter parameter) {
        try {
            String name = parameter.getName();
            AnnotationService.getInstance(parameter.getProject())
                .addAnnotation(parameter, Annotation.PARAM.withValue(new Annotation.StringValue(name)));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
