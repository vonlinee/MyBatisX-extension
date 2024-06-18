package com.baomidou.mybatisx.feat.jpa.operate.dialect.oracle;

import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomSuffixAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.operator.SuffixOperator;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.intellij.psi.PsiClass;

import java.util.LinkedList;

/**
 * 插入后缀处理器
 */
public class InsertCustomSuffixAppender extends CustomSuffixAppender {

    /**
     * Instantiates a new Insert custom suffix appender.
     *
     * @param tipName        the tip name
     * @param suffixOperator the suffix operator
     * @param areaSequence   the area sequence
     */
    public InsertCustomSuffixAppender(String tipName, SuffixOperator suffixOperator, AreaSequence areaSequence) {
        super(tipName, suffixOperator, areaSequence);
    }

    /**
     * Create insert by suffix operator syntax appender.
     *
     * @param all            the all
     * @param suffixOperator the suffix operator
     * @param areaSequence   the area sequence
     * @return the syntax appender
     */
    public static SyntaxAppender createInsertBySuffixOperator(String all, SuffixOperator suffixOperator, AreaSequence areaSequence) {
        return new InsertCustomSuffixAppender(all, suffixOperator, areaSequence);
    }

    @Override
    public void toTree(LinkedList<SyntaxAppender> jpaStringList, SyntaxAppenderWrapper syntaxAppenderWrapper) {
        syntaxAppenderWrapper.addWrapper(new SyntaxAppenderWrapper(InsertCustomSuffixAppender.this));
    }

    @Override
    protected String getFieldTemplateText(String tableName, PsiClass entityClass, LinkedList<TxParameter> parameters, LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper, SyntaxAppender appender) {
        InsertCustomSuffixAppender suffixAppender = (InsertCustomSuffixAppender) appender;
        return suffixAppender.getSuffixOperator().getTemplateText(tableName, parameters, conditionFieldWrapper);
    }
}
