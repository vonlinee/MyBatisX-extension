package com.baomidou.mybatisx.feat.jpa.common.factory;


import com.baomidou.mybatisx.feat.jpa.common.BaseAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomFieldAppender;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 结果集追加
 */
public class ResultAppenderFactory extends BaseAppenderFactory {
    /**
     * The constant RESULT.
     */
// 区域类型
    public static final String RESULT = "Result";
    /**
     * 区域前缀
     */
    private final String areaPrefix;

    private final List<SyntaxAppender> syntaxAppenderList = new ArrayList<>();

    /**
     * Instantiates a new Result appender factory.
     *
     * @param areaPrefix the area prefix
     */
    public ResultAppenderFactory(final String areaPrefix) {
        this.areaPrefix = areaPrefix;
    }

    /**
     * Register appender.
     *
     * @param syntaxAppender the syntax appender
     */
    public void registerAppender(final SyntaxAppender syntaxAppender) {
        this.syntaxAppenderList.add(syntaxAppender);
    }


    @Override
    public List<TxParameter> getMxParameter(PsiClass entityClass, LinkedList<SyntaxAppenderWrapper> jpaStringList) {
        return Collections.emptyList();
    }

    @Override
    public List<SyntaxAppender> getSyntaxAppenderList() {
        return this.syntaxAppenderList;
    }


    @Override
    public String getTipText() {
        return this.areaPrefix;
    }


    @Override
    protected AreaSequence getAreaSequence() {
        return AreaSequence.RESULT;
    }


    public static class WrapDateCustomFieldAppender extends CustomFieldAppender {


        /**
         * Instantiates a new Custom field appender.
         *
         * @param field        the field
         * @param areaSequence the area sequence
         */
        public WrapDateCustomFieldAppender(TxField field, AreaSequence areaSequence) {
            super(field, areaSequence);
        }


        @Override
        protected String wrapFieldValueInTemplateText(String columnName, ConditionFieldWrapper conditionFieldWrapper, String fieldValue) {
            return conditionFieldWrapper.wrapDefaultDateIfNecessary(columnName, fieldValue);
        }
    }
}
