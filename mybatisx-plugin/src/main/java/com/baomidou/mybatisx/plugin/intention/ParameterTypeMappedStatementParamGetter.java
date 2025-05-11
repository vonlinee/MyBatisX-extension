package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.intellij.psi.PsiElement;

import java.util.Map;

/**
 * TODO 根据Mapper Statement标签的parameterType指定的java类型分析参数
 */
public class ParameterTypeMappedStatementParamGetter implements MappedStatementParamGetter {
  @Override
  public void getParams(PsiElement element, Map<String, ParamDataType> paramMap) {

  }
}
