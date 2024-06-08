package com.baomidou.mybatisx.feat.jpa.operate.appender;

import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomFieldAppender;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
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
