package com.baomidou.mybatisx.tip;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.intellij.codeInsight.completion.CompletionResultSet;

public class JavaTypeHashMark implements HashMarkTip {
    @Override
    public String getName() {
        return "javaType";
    }

    /**
     * java 类如此多，所以就不提示了
     *
     * @param completionResultSet CompletionResultSet
     * @param mapper              Mapper
     */
    @Override
    public void tipValue(CompletionResultSet completionResultSet, Mapper mapper) {

    }
}
