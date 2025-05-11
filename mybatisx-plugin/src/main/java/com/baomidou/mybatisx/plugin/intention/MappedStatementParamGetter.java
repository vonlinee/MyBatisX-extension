package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.intellij.psi.PsiElement;

import java.util.Map;

public interface MappedStatementParamGetter {

  void getParams(PsiElement element, Map<String, ParamDataType> paramMap);
}
