package com.baomidou.mybatisx.feat.jpa.common.appender.operator;

import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 固定后缀
 * <p>
 * is null
 * is not null
 */
public class FixedSuffixOperator implements SuffixOperator {
  /**
   * 比较符号
   */
  private String operatorName;
  private List<TxField> mappingField;

  /**
   * Instantiates a new Fixed suffix operator.
   *
   * @param operatorName the operator name
   * @param mappingField
   */
  public FixedSuffixOperator(final String operatorName, List<TxField> mappingField) {
    this.operatorName = operatorName;
    this.mappingField = mappingField;
  }

  /**
   * 通过字段名称找到表的列名, 然后拼接列名和操作符，例如  username is null
   *
   * @param fieldName  the field name 字段名称
   * @param parameters
   */
  @Override
  public String getTemplateText(String fieldName,
                                LinkedList<TxParameter> parameters,
                                ConditionFieldWrapper conditionFieldWrapper) {
    return mappingField.stream()
      .filter(field -> field.getFieldName().equals(fieldName))
      .map(field -> field.getColumnName() + " " + operatorName)
      .collect(Collectors.joining());
  }
}
