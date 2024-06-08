package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.codeInspection.util.IntentionFamilyName;
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
 * MyBatis Mapper Xml 中生成 if foreach 标签
 */
public class MapperXmlForeachHintIntention extends MyBatisMapperXmlBaseIntentionAction {

    @Override
    public @IntentionName
    @NotNull String getText() {
        return "MyBatisX Generate If In";
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        Document document = editor.getDocument();
        CaretModel model = editor.getCaretModel();
        Caret currentCaret = model.getCurrentCaret();
        String selectedText = currentCaret.getSelectedText();
        selectedText = StringUtils.hasText(selectedText) ? selectedText : "";

        String text = String.format("<if test=\"%s != null and %s.size() > 0\">\n" +
                                    "    AND  IN\n" +
                                    "    <foreach item=\"item\" index=\"index\" collection=\"%s\" open=\"(\" close=\")\" separator=\",\">\n" +
                                    "        #{item}\n" +
                                    "    </foreach>\n" +
                                    "</if>", selectedText, selectedText, selectedText);
        document.insertString(currentCaret.getOffset(), text);
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        return isMyBatisMapperXmlFile(psiElement.getContainingFile());
    }

    @Override
    public @NotNull
    @IntentionFamilyName String getFamilyName() {
        return getText();
    }
}
