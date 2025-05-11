package com.baomidou.mybatisx.plugin.intention;

import com.intellij.ide.fileTemplates.impl.FileTemplateHighlighter;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.lang.Language;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import com.intellij.ui.LanguageTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.intellij.ui.LanguageTextField
 */
public class MapperStatementEditor extends LanguageTextField {

    private XmlDocument mapperFileDocument;
    private boolean myModified = false;

    private XmlFile mapperFile;
    private XmlElement element;

    public MapperStatementEditor(Project project) {
        super(XMLLanguage.INSTANCE, project, "");
    }

    public MapperStatementEditor() {
        super(XMLLanguage.INSTANCE, null, "");
    }

    @Override
    protected @NotNull EditorEx createEditor() {
        EditorEx editor = super.createEditor();
        // 水平滚动条
        HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
        // 垂直滚动条
        editor.setVerticalScrollbarVisible(true);

        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setVirtualSpace(false);
        editorSettings.setLineMarkerAreaShown(false);
        editorSettings.setIndentGuidesShown(false);
        editorSettings.setLineNumbersShown(false);
        editorSettings.setFoldingOutlineShown(false);
        editorSettings.setAdditionalColumnsCount(3);
        editorSettings.setAdditionalLinesCount(3);
        editorSettings.setCaretRowShown(false);

        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent e) {
                onTextChanged();
            }
        }, ((EditorImpl) editor).getDisposable());

        editor.setHighlighter(createHighlighter());

        return editor;
    }

    private EditorHighlighter createHighlighter() {
        SyntaxHighlighter originalHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(XmlFileType.INSTANCE, null, null);
        if (originalHighlighter == null) {
            originalHighlighter = new PlainSyntaxHighlighter();
        }

        final EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        LayeredLexerEditorHighlighter highlighter = new LayeredLexerEditorHighlighter(new FileTemplateHighlighter(), scheme);
        highlighter.registerLayer(new IElementType("TEXT", Language.ANY), new LayerDescriptor(originalHighlighter, ""));
        return highlighter;
    }

    @NotNull
    private Document createDocument(@Nullable PsiFile file) {
        Document document = file != null ? PsiDocumentManager.getInstance(file.getProject()).getDocument(file) : null;
        return document != null ? document : EditorFactory.getInstance().createDocument("");
    }

    private void onTextChanged() {
        myModified = true;
    }

    public void updateStatement(@NotNull XmlElement statement) {
        this.mapperFile = (XmlFile) statement.getContainingFile();
        this.element = statement;
        this.mapperFileDocument = mapperFile.getDocument();

        setText(statement.getText());
    }
}
