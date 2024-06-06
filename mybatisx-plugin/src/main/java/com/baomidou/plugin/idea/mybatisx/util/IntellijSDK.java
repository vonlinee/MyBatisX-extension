package com.baomidou.plugin.idea.mybatisx.util;

import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

public final class IntellijSDK {

    public static void invokeLater(@NotNull Runnable runnable) {
        ApplicationManager.getApplication().invokeLater(runnable);
    }
}
