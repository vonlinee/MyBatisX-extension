package com.baomidou.mybatisx.feat.jpa.common.appender.operator;


import com.baomidou.mybatisx.feat.jpa.common.appender.JdbcTypeUtils;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;

import java.util.LinkedList;

/**
 * 忽略大小写
 */
public class ParamIgnoreCaseSuffixOperator implements SuffixOperator {


    @Override
    public String getTemplateText(String fieldName,
                                  LinkedList<TxParameter> parameters,
                                  ConditionFieldWrapper conditionFieldWrapper) {

        TxParameter parameter = parameters.poll();
        return "UPPER(" + fieldName + ")"
               + " "
               + "="
               + " "
               + "UPPER(" + JdbcTypeUtils.wrapperField(parameter.getName(), parameter.getCanonicalTypeText()) + ")";
    }
}
