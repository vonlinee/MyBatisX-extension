package com.baomidou.mybatisx.util;

import com.intellij.debugger.JavaDebuggerBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class IntellijSDK {

    public static void invokeLater(@NotNull Runnable runnable) {
        ApplicationManager.getApplication().invokeLater(runnable);
    }

    /**
     * 获取Application级别的Service单例
     *
     * @param <T> 类型
     * @return 单例
     */
    public static <T> T getService(@NotNull Class<T> requiredType) {
        return ApplicationManager.getApplication().getService(requiredType);
    }

    /**
     * 获取项目级别的Service单例
     *
     * @param <T> 类型
     * @return 单例
     */
    public static <T> T getService(@NotNull Class<T> requiredType, @NotNull Project project) {
        return project.getService(requiredType);
    }

    /**
     * 获取已经打开的所有项目实例
     *
     * @return 所有项目实例
     */
    @NotNull
    public static List<Project> getOpenedProjects() {
        ProjectManager manager = ProjectManager.getInstanceIfCreated();
        if (manager == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(manager.getOpenProjects());
    }

    public static String message(String key, @NotNull Object... params) {
        return JavaDebuggerBundle.message(key, params);
    }

    public static PluginId findPluginId(String pluginId) {
        return PluginId.findId(pluginId);
    }
}
