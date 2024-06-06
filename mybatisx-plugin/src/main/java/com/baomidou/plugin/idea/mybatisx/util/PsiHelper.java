package com.baomidou.plugin.idea.mybatisx.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

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
}
