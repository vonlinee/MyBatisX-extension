package com.baomidou.plugin.idea.mybatisx.intention;

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import org.jetbrains.annotations.NotNull;

/**
 * @see com.intellij.ui.LanguageTextField
 */
public class MapperStatementEditor extends EditorTextField {

    public MapperStatementEditor(Project project) {
        super(project, XmlFileType.INSTANCE);
    }

    @Override
    protected @NotNull EditorEx createEditor() {
        EditorEx editor = super.createEditor();
        // 水平滚动条
        HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
        // 垂直滚动条
        editor.setVerticalScrollbarVisible(true);
        return editor;
    }
}