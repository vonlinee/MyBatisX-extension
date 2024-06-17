package com.baomidou.mybatisx.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

public abstract class DialogBase extends DialogWrapper {

    private Project project;

    public DialogBase() {
        super(null);
    }

    protected DialogBase(@Nullable Project project) {
        super(project);
        this.project = project;
    }

    @Override
    public void show() {
        init();
        super.show();
    }

    public final Project getProject() {
        return project;
    }
}
