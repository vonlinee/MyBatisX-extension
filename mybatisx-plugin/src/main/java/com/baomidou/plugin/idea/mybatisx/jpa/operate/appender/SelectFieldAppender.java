package com.baomidou.plugin.idea.mybatisx.jpa.operate.appender;

import com.baomidou.plugin.idea.mybatisx.jpa.common.appender.AreaSequence;
import com.baomidou.plugin.idea.mybatisx.jpa.common.appender.CustomFieldAppender;
import com.baomidou.plugin.idea.mybatisx.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.plugin.idea.mybatisx.jpa.component.TxField;
import com.baomidou.plugin.idea.mybatisx.jpa.component.TxParameter;
import com.baomidou.plugin.idea.mybatisx.jpa.util.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;

import java.util.LinkedList;

/**
 * @author ls9527
 */
// 查询类型的结果集区域,  字段拼接部分, 只需要字段名称就可以了
public class SelectFieldAppender extends CustomFieldAppender {

    public SelectFieldAppender(TxField txField) {
        super(txField, AreaSequence.RESULT);
    }


    @Override
    public String getTemplateText(String tableName, PsiClass
            entityClass, LinkedList<TxParameter> parameters, LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper
                                          conditionFieldWrapper) {
        return columnName;
    }
}
