package com.baomidou.plugin.idea.mybatisx.actions;

import com.baomidou.plugin.idea.mybatisx.model.Field;
import com.baomidou.plugin.idea.mybatisx.service.MainService;
import com.baomidou.plugin.idea.mybatisx.ui.dialog.JavaBean2DDLResultDialog;
import com.baomidou.plugin.idea.mybatisx.util.BaseUtil;
import com.baomidou.plugin.idea.mybatisx.util.DdlFormatUtil;
import com.baomidou.plugin.idea.mybatisx.util.SqlTypeMapUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class Bean2DDLAction extends AnAction {

    public static ConcurrentHashMap<String, String> translationMap;
    JavaBean2DDLResultDialog dialog;
    public static ConcurrentHashMap<String, SqlTypeMapUtil.ConvertBean> convertMap;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        translationMapInit();
        convertMapInit();
        PsiFile file = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        PsiClass currentClass = BaseUtil.getClassEntity(file);
        assert currentClass != null;
        MainService mainService = new MainService();
        String tableName = mainService.getTableName(currentClass);
        List<Field> fieldList = mainService.getFieldList(currentClass);
        String script = DdlFormatUtil.buildDdlScript(tableName, fieldList);
        if (dialog == null) {
            dialog = new JavaBean2DDLResultDialog(anActionEvent.getProject());
        }
        dialog.showSql(script);
    }

    private void convertMapInit() {
        if (null != convertMap) {
            convertMap.clear();
        }
        convertMap = SqlTypeMapUtil.getInstance().convertMapInit();
    }

    private void translationMapInit() {
        if (null != translationMap) {
            translationMap.clear();
        } else {
            translationMap = new ConcurrentHashMap<>();
        }
    }
}
