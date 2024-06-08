package com.baomidou.mybatisx.ddl;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddColumnDDL extends CreatorSupport {

    @Override
    public String createDDL(AnActionEvent event, PsiClass psiClass) {
        //获得当前编辑器
        final Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return null;
        }
        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            return null;
        }

        String[] targetFieldNameArray = selectedText.split("\n");

        if (targetFieldNameArray.length == 0) {
            return "Target fields undefined from your selection.";
        }
        Set<String> fieldName = new HashSet<>();
        for (String name : targetFieldNameArray) {
            if (!name.contains(";")) {
                continue;
            }
            String[] fieldElements = name.substring(0, name.indexOf(";")).split(" ");
            int indexCounter = 0;
            for (String fieldElementTxt : fieldElements) {
                if (!fieldElementTxt.trim().isEmpty()) {
                    indexCounter++;
                }
                if (indexCounter == 3) {
                    fieldName.add(fieldElementTxt.trim());
                    break;
                }
            }
        }

        //获得当前类的名称
        String tableName = getTableName(psiClass);
        //获得当前类的字段
        PsiField[] psiFields = psiClass.getFields();

        //遍历分析获得字段内容，包括getter和其字段的 annotation标签
        List<TableField> tableFields = new ArrayList<>();
        for (PsiField psiField : psiFields) {
            //解析
            if (fieldName.contains(psiField.getName())) {
                TableField tableField = getTableField(psiField);
                if (tableField != null) {
                    tableFields.add(tableField);
                }
            }
        }
        //组织SQL
        StringBuilder createTableDDL = new StringBuilder();
        for (TableField tableField : tableFields) {
            //获得字段的列SQL
            createTableDDL.append(getSqlOfColumnPart(tableName, tableField));
            createTableDDL.append("\n");
        }
        return createTableDDL.toString();
    }


    /**
     * 将tableField转换成部分的创建表字段语句
     * alter table t_order add COLUMN youchi_agent_id bigint(20) COMMENT '接收分销ID'
     *
     * @param tableField
     * @return
     */
    private String getSqlOfColumnPart(String tableName, TableField tableField) {
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN `").append(tableField.getName()).append("` ").append(tableField.getType());
        if (tableField.getLength() != null) {
            sql.append("(").append(tableField.getLength()).append(")");
        }
        if (!tableField.isPrimaryKey() && tableField.isNullable()) {
            sql.append(" NULL");
        } else {
            sql.append(" NOT NULL");
        }
        if (tableField.getDesc() != null && !tableField.getDesc().isEmpty()) {
            sql.append(" COMMENT '").append(tableField.getDesc()).append("'");
        }
        sql.append(";");
        return sql.toString();
    }


    /**
     * 分析当前编辑类是否存在@Entity 同时选择相关字段信息 如果不存在则不显示功能按钮
     *
     * @param e
     */
    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        // 获取当前编辑的文件, 通过PsiFile可获得PsiClass, PsiField等对象
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        // 获取当前编辑的文件, 通过PsiFile可获得PsiClass, PsiField等对象
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (!editor.getSelectionModel().hasSelection()) {
            e.getPresentation().setEnabled(false);
        }
    }
}
