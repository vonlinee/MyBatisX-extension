package com.baomidou.mybatisx.tip;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

/**
 * see mybatis : org.apache.ibatis.mapping.ParameterMode
 *
 * @author ls9527
 */
public class ModeHashMarkTip implements HashMarkTip {
    private static final String[] MODE_LIST = {"IN",
            "OUT",
            "INOUT",
    };

    @Override
    public String getName() {
        return "mode";
    }

    /**
     * 最简单的枚举值提示
     *
     * @param completionResultSet CompletionResultSet
     * @param mapper              Mapper
     */
    @Override
    public void tipValue(CompletionResultSet completionResultSet, Mapper mapper) {
        for (String mode : MODE_LIST) {
            completionResultSet.addElement(LookupElementBuilder.create(mode));
        }
    }
}
