package com.baomidou.mybatisx.plugin.intention;

import com.intellij.ide.fileTemplates.impl.FileTemplateHighlighter;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.lang.ASTNode;
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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.AstBufferUtil;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import com.intellij.ui.LanguageTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
    this.setOneLineMode(false);
    this.setEnabled(true);
  }

  @Override
  protected @NotNull EditorEx createEditor() {
    EditorEx editor = super.createEditor();
    // 水平滚动条
    HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
    // 垂直滚动条
    editor.setVerticalScrollbarVisible(true);

    EditorSettings editorSettings = editor.getSettings();
//    editorSettings.setVirtualSpace(false);
//    editorSettings.setLineMarkerAreaShown(false);
//    editorSettings.setIndentGuidesShown(false);
//    editorSettings.setLineNumbersShown(false);
//    editorSettings.setFoldingOutlineShown(false);
//    editorSettings.setAdditionalColumnsCount(3);
//    editorSettings.setAdditionalLinesCount(3);
//    editorSettings.setCaretRowShown(false);

    editor.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void documentChanged(@NotNull DocumentEvent e) {
        onTextChanged();
      }
    }, ((EditorImpl) editor).getDisposable());

    // editor.setHighlighter(createHighlighter());

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

  public void updateStatement(@NotNull XmlTag statement) {
    this.mapperFile = (XmlFile) statement.getContainingFile();
    this.element = statement;
    this.mapperFileDocument = mapperFile.getDocument();

    setText(getText(statement));
  }

  /**
   * handle <include ref='xxx'></include>
   *
   * @param copy mapper statement xml psi element
   * @return string
   */
  private String getText(@NotNull XmlTag copy) {
    PsiFile containingFile = copy.getContainingFile();
    if (containingFile instanceof XmlFile) {
      XmlDocument document = ((XmlFile) containingFile).getDocument();
      recursiveReplace(copy, document);
    }

    final String text = copy.getText();
    // remove <sql>

    int fromIndex = 0;
    StringBuilder sb = new StringBuilder();
    while (fromIndex < text.length()) {
      int start = text.indexOf("<sql", fromIndex);
      if (start > 0) {
        sb.append(text, fromIndex, start);
        for (int i = start; i < text.length(); i++) {
          if (text.charAt(i) == '>') {
            fromIndex = i + 1;
            break;
          }
        }

        // '</sql >' has no errors in xml validation, but '</ sql' is not allowed,  so we search '</sql'
        int i = text.indexOf("</sql", fromIndex);
        if (i > 0) { // should be true
          sb.append(text, fromIndex, i);
          fromIndex = i + 5;
          for (int j = fromIndex; j < text.length(); j++) {
            if (text.charAt(j) == '>') {
              fromIndex = j + 1;
              break;
            }
          }
        }
      } else {
        break;
      }
    }

    if (fromIndex < text.length()) {
      sb.append(text, fromIndex, text.length());
    }

    return sb.toString();
  }

  private void recursiveReplace(PsiElement element, XmlDocument document) {
    if (!(element instanceof XmlTag)) {
      return;
    }
    XmlTag xmlTag = (XmlTag) element;
    if ("include".equals(xmlTag.getName())) {
      String refid = xmlTag.getAttributeValue("refid");
      XmlTag rootTag = document.getRootTag();
      if (rootTag == null) {
        return;
      }
      XmlTag[] sqlTags = rootTag.findSubTags("sql");
      XmlTag sqlElement = null;
      for (XmlTag sqlTag : sqlTags) {
        if (Objects.equals(sqlTag.getAttributeValue("id"), refid)) {
          sqlElement = sqlTag;
        }
      }
      if (sqlElement != null) {
        // replace <include/> with <sql/>

        // note: there will be some text of the tag <sql id='xxx'/> in the final text
        xmlTag.replace(sqlElement);
      }
      return;
    }
    if (xmlTag.isEmpty()) {
      return;
    }
    for (PsiElement child : xmlTag.getChildren()) {
      recursiveReplace(child, document);
    }
  }
}
