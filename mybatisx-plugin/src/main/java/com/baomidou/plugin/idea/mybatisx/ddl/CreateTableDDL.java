package com.baomidou.plugin.idea.mybatisx.ddl;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.ArrayList;
import java.util.List;

public class CreateTableDDL extends CreatorSupport {

    /**
     * 分析获得建表语句
     *
     * @param psiClass
     * @return
     */
    @Override
    public String createDDL(AnActionEvent event, PsiClass psiClass) {

        //获得当前类的名称
        String tableName = getTableName(psiClass);
        //获得当前类的字段
        PsiField[] psiFields = psiClass.getFields();

        //遍历分析获得字段内容，包括getter和其字段的 annotation标签
        List<TableField> tableFields = new ArrayList<>();
        for (PsiField psiField : psiFields) {
            //解析
            TableField tableField = getTableField(psiField);
            if (tableField != null) {
                tableFields.add(tableField);
            }
        }

        //组织SQL
        StringBuffer createTableDDL = new StringBuffer("DROP TABLE IF EXISTS `" + tableName + "`;\n");
        createTableDDL.append("CREATE TABLE `" + tableName + "` (\n");
        for (int i = 0; i < tableFields.size(); i++) {
            //获得字段的列SQL
            createTableDDL.append("    " + getSqlOfColumnPart(tableFields.get(i)));
            if (i < tableFields.size() - 1) {
                createTableDDL.append(",");
            }
            createTableDDL.append("\n");
        }
        createTableDDL.append(") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;");
        return createTableDDL.toString();
    }


    /**
     * 将tableField转换成部分的创建表字段语句
     *
     * @param tableField
     * @return
     */
    private String getSqlOfColumnPart(TableField tableField) {
        StringBuffer sql = new StringBuffer();
        sql.append("`" + tableField.getName() + "` " + tableField.getType());
        if (tableField.getLength() != null) {
            sql.append("(" + tableField.getLength() + ")");
        }
        if (!tableField.isPrimayKey() && tableField.isNullable()) {
            sql.append(" NULL");
        } else {
            sql.append(" NOT NULL");
        }
        if (tableField.isPrimayKey()) {
            sql.append(" PRIMARY KEY");
        }
        if (tableField.isGeneratedValue()) {
            sql.append(" AUTO_INCREMENT");
        }
        if (tableField.getDesc() != null && tableField.getDesc().length() > 0) {
            sql.append(" COMMENT '" + tableField.getDesc() + "'");
        }
        return sql.toString();
    }

    /**
     * 分析当前编辑类是否存在@Entity 如果不存在则不显示功能按钮
     *
     * @param e
     */
    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }
}
