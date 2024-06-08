package com.baomidou.mybatisx.plugin.actions;

import com.baomidou.mybatisx.plugin.ui.dialog.BeanToolDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * JavaBean 工具
 */
public final class BeanToolAction extends AnAction {

    BeanToolDialog dialog;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        if (dialog == null) {
            dialog = new BeanToolDialog(event.getProject());
        }
        dialog.show();
    }
}
