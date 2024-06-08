package com.baomidou.mybatisx.tip;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

/**
 * tip jdbc type
 *
 * @author ls9527
 */
public class JdbcTypeHashMarkTip implements HashMarkTip {
    @Override
    public String getName() {
        return "jdbcType";
    }

    @Override
    public void tipValue(CompletionResultSet completionResultSet, Mapper mapper) {
        CompletionResultSet insensitiveResultSet = completionResultSet.caseInsensitive();
        for (JdbcType value : JdbcType.values()) {
            insensitiveResultSet.addElement(LookupElementBuilder.create(value.name()));
        }
    }
}
