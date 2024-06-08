package com.baomidou.mybatisx.tip;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.intellij.codeInsight.completion.CompletionResultSet;

public interface HashMarkTip {
    String getName();

    void tipValue(CompletionResultSet completionResultSet, Mapper mapper);
}
