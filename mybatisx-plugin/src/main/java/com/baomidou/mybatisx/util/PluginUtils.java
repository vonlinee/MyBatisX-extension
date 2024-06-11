package com.baomidou.mybatisx.util;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PluginUtils {

    public static final String PLUGIN_NAME = "MyBatisX";
    public static final String PLUGIN_ID = "com.baomidou.plugin.idea.mybatisx";
    private static volatile Boolean existsDatabaseTools = null;

    private PluginUtils() {
    }

    /**
     * Idea企业版才有Database Tool工具
     *
     * @return 是否存在Database Tool工具
     */
    public static boolean existsDbTools() {
        if (existsDatabaseTools == null) {
            synchronized (PluginUtils.class) {
                if (existsDatabaseTools == null) {
                    try {
                        Class.forName("com.intellij.database.psi.DbTable");
                        existsDatabaseTools = true;
                    } catch (ClassNotFoundException ex) {
                        existsDatabaseTools = false;
                    }
                }
            }
        }
        return existsDatabaseTools;
    }

    public static void invokeLater(@NotNull Runnable runnable) {
        ApplicationManager.getApplication().invokeLater(runnable);
    }

    public static boolean isJavaElement(PsiElement element) {
        return element.getLanguage().is(JavaLanguage.INSTANCE);
    }

    public static boolean isJavaClassElement(PsiElement element) {
        return element instanceof PsiClass;
    }

    /**
     * 获取当前的 Project 实例
     *
     * @return Project
     */
    public static Project getCurrentProject() {
        return ProjectManager.getInstance().getDefaultProject();
    }
}
