package com.baomidou.plugin.idea.mybatisx.jpa.common.appender.changer;


import com.baomidou.plugin.idea.mybatisx.jpa.common.appender.MxParameterChanger;
import com.baomidou.plugin.idea.mybatisx.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.plugin.idea.mybatisx.jpa.component.TxParameter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Boolean parameter changer.
 */
public class BooleanParameterChanger implements MxParameterChanger {

    private Boolean booleanValue;

    /**
     * Instantiates a new Boolean parameter changer.
     *
     * @param booleanValue the boolean value
     */
    public BooleanParameterChanger(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    @Override
    public List<TxParameter> getParameter(TxParameter txParameter) {
        return Collections.emptyList();
    }


    @Override
    public String getTemplateText(String fieldName, LinkedList<TxParameter> parameters, ConditionFieldWrapper conditionFieldWrapper) {
        return fieldName + " = " + booleanValue.toString();
    }


}
