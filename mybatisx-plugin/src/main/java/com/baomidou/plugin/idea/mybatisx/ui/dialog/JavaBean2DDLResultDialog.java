package com.baomidou.plugin.idea.mybatisx.ui.dialog;

import com.baomidou.plugin.idea.mybatisx.ui.MainPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JavaBean2DDLResultDialog extends DialogWrapper {

    MainPanel center;

    public JavaBean2DDLResultDialog(@Nullable Project project) {
        super(project, false);

        setSize(550, 600);
        setTitle("Java Bean Convert DDL");

        center = new MainPanel();

        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return center.getContent();
    }

    public final void showSql(String sql) {
        center.setContentTxt(sql);
        show();
    }
}
