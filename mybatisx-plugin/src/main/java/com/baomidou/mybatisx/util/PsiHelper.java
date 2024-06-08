package com.baomidou.mybatisx.util;

import com.intellij.lang.jvm.types.JvmPrimitiveTypeKind;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class PsiHelper {

    public static PsiElement[] getPsiElements(AnActionEvent event) {
        return event.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
    }

    /**
     * 获取PsiElement所在元素的文件路径
     *
     * @param element PsiElement
     * @return 文件路径
     */
    public static String getPathOfContainingFile(PsiElement element) {
        PsiFile psiFile = element.getContainingFile();
        // 获取VirtualFile
        VirtualFile virtualFile = psiFile.getVirtualFile();
        if (virtualFile != null) {
            // 获取文件的绝对路径  或者使用 virtualFile.getPath();
            return virtualFile.getCanonicalPath();
        }
        // PsiFile不是基于文件系统的，可能是内存中的文件等
        return null;
    }

    /**
     * 是否基础类型
     *
     * @param field
     * @return
     */
    public static boolean isPrimitiveType(PsiField field) {
        if (null == field) {
            return false;
        }
        String canonicalText = field.getType().getCanonicalText();
        JvmPrimitiveTypeKind kindByName = JvmPrimitiveTypeKind.getKindByName(canonicalText);
        if (null != kindByName) {
            return true;
        }
        JvmPrimitiveTypeKind kindByFqn = JvmPrimitiveTypeKind.getKindByFqn(canonicalText);
        return null != kindByFqn;
    }

    public static PsiFile getPsiFile(AnActionEvent event) {
        return event.getData(CommonDataKeys.PSI_FILE);
    }

    /**
     * 获取当前类
     *
     * @param element PSI元素
     * @return 类元素
     */
    public static PsiClass getPsiClass(PsiElement element) {
        return PsiTreeUtil.getChildOfType(element, PsiClass.class);
    }

    /**
     * 获取当前类的所有字段(包括父类)
     *
     * @param psiClass
     * @return
     */
    public static PsiField[] getAllPsiFields(PsiClass psiClass) {
        if (null == psiClass) {
            return new PsiField[0];
        }
        return psiClass.getAllFields();
    }

    /**
     * 获取当前类的所有字段
     *
     * @param psiClass
     * @return
     */
    public static PsiField[] getPsiFields(PsiClass psiClass) {
        if (null == psiClass) {
            return new PsiField[0];
        }
        return psiClass.getFields();
    }

    public static PsiField[] getPsiFields(PsiClass psiClass, boolean all) {
        return all ? getAllPsiFields(psiClass) : getPsiFields(psiClass);
    }

    /**
     * 根据class 获取字段的名称
     *
     * @param entityClass the entity class
     * @return string psi field map
     */
    @NotNull
    public static Map<String, PsiField> getStringPsiFieldMap(PsiClass entityClass) {
        return Arrays.stream(entityClass.getAllFields())
            .filter(field -> (!field.hasModifierProperty(PsiModifier.STATIC))
                             && (!field.hasModifierProperty(PsiModifier.TRANSIENT)))
            .collect(Collectors.toMap(PsiField::getName, x -> x, BinaryOperator.maxBy(Comparator.comparing(PsiField::getName))));
    }

    /**
     * Gets psi field list.
     *
     * @param entityClass the entity class
     * @return the psi field list
     */
    @NotNull
    public static List<PsiField> getPsiFieldList(PsiClass entityClass) {
        return Arrays.stream(entityClass.getAllFields())
            .filter(field -> (!field.hasModifierProperty(PsiModifier.STATIC))
                             && (!field.hasModifierProperty(PsiModifier.TRANSIENT)))
            .collect(Collectors.toList());
    }
}
