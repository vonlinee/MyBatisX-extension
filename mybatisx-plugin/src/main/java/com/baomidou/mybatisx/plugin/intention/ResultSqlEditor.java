package com.baomidou.mybatisx.plugin.intention;

import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.sql.SqlFileType;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import org.jetbrains.annotations.NotNull;

public class ResultSqlEditor extends EditorTextField {
    public ResultSqlEditor(Project project) {
        super(project, SqlFileType.INSTANCE);
    }

    @Override
    protected @NotNull EditorEx createEditor() {
        EditorEx editor = super.createEditor();
        // 水平滚动条
        HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
        editor.setOneLineMode(false);
        // 垂直滚动条
        editor.setVerticalScrollbarVisible(true);
        return editor;
    }
}
