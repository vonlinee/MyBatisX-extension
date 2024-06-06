package com.baomidou.plugin.idea.mybatisx.jpa.common.appender.operator.suffix;


import com.baomidou.plugin.idea.mybatisx.jpa.common.appender.JdbcTypeUtils;
import com.baomidou.plugin.idea.mybatisx.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.plugin.idea.mybatisx.jpa.component.TxParameter;

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
