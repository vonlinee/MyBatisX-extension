package com.baomidou.plugin.idea.mybatisx.component;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.jetbrains.annotations.NotNull;

public class TabPane extends JBTabsImpl {
    public TabPane(@NotNull Project project) {
        super(project);
    }
}
