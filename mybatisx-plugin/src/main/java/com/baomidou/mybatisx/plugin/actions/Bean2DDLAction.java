package com.baomidou.mybatisx.plugin.actions;

import com.baomidou.mybatisx.model.ConvertBean;
import com.baomidou.mybatisx.plugin.ui.dialog.BeanToolDialog;
import com.baomidou.mybatisx.util.MessageNotification;
import com.baomidou.mybatisx.util.PsiHelper;
import com.baomidou.mybatisx.util.SqlTypeMapUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public final class Bean2DDLAction extends AnAction {

    public static ConcurrentHashMap<String, String> translationMap;
    public static ConcurrentHashMap<String, ConvertBean> convertMap;

    BeanToolDialog dialog;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        PsiFile file = PsiHelper.getPsiFile(anActionEvent);
        PsiClass currentClass = PsiHelper.getPsiClass(file);
        if (currentClass == null) {
            MessageNotification.showErrorDialog("No PsiClass Found");
            return;
        }

        if (dialog == null) {
            dialog = new BeanToolDialog(anActionEvent.getProject());
        }

        initTranslationMap();
        initConvertMap();

        dialog.showWithClass(currentClass);
    }

    private void initConvertMap() {
        if (null != convertMap) {
            convertMap.clear();
        }
        convertMap = SqlTypeMapUtil.getInstance().convertMapInit();
    }

    private void initTranslationMap() {
        if (null != translationMap) {
            translationMap.clear();
        } else {
            translationMap = new ConcurrentHashMap<>();
        }
    }
}
