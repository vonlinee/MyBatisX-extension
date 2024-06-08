package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.plugin.setting.config.AbstractStatementGenerator;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * The type Generate statement intention.
 *
 * @author yanglin
 */
public class GenerateStatementIntention extends GenericIntention {

    /**
     * Instantiates a new Generate statement intention.
     */
    public GenerateStatementIntention() {
        super(GenerateStatementChooserAbstract.INSTANCE);
    }

    @NotNull
    @Override
    public String getText() {
        return "[MybatisX] Generate new statement";
    }

    @Override
    public void invoke(@NotNull final Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        AbstractStatementGenerator.applyGenerate(PsiTreeUtil.getParentOfType(element, PsiMethod.class), project);
    }

}
