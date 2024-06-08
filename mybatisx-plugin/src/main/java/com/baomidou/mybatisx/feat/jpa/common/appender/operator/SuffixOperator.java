package com.baomidou.mybatisx.feat.jpa.common.appender.operator;

import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;

import java.util.LinkedList;

/**
 * The interface Suffix operator.
 */
public interface SuffixOperator {

    /**
     * Gets template text.
     *
     * @param fieldName             the field name
     * @param parameters            the parameters
     * @param conditionFieldWrapper
     * @return the template text
     */
    String getTemplateText(String fieldName, LinkedList<TxParameter> parameters, ConditionFieldWrapper conditionFieldWrapper);
}
