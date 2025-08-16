package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * MyBatis Mapper Xml 中生成 if test 标签
 */
public class MapperXmlIfTestHintIntention extends MyBatisMapperXmlBaseIntentionAction {

  @Override
  public @IntentionName
  @NotNull String getText() {
    return "MyBatisX  Generate If String hasText Eq";
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
    Document document = editor.getDocument();
    CaretModel model = editor.getCaretModel();
    Caret currentCaret = model.getCurrentCaret();
    String selectedText = currentCaret.getSelectedText();
    selectedText = StringUtils.hasText(selectedText) ? selectedText : "";
    String sb = "<if test=\" " +
                selectedText +
                " != null and " +
                selectedText +
                " != ''\">\n" +
                " = #{" + selectedText + "}" +
                "\n" +
                "</if>";
    document.insertString(currentCaret.getOffset(), sb);
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
    return isMyBatisMapperXmlFile(psiElement.getContainingFile());
  }
}
