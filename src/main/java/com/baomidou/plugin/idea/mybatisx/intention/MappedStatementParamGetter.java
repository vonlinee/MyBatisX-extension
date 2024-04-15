package com.baomidou.plugin.idea.mybatisx.intention;

import com.baomidou.plugin.idea.mybatisx.model.ParamDataType;
import com.intellij.psi.PsiElement;

import java.util.Map;

public interface MappedStatementParamGetter {

    void getParams(PsiElement element, Map<String, ParamDataType> paramMap);
}
