package com.baomidou.mybatisx.plugin.component;

import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import org.jetbrains.annotations.NotNull;

public class CodeArea extends EditorTextField {

    public CodeArea() {
        super(null, PlainTextFileType.INSTANCE);
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
