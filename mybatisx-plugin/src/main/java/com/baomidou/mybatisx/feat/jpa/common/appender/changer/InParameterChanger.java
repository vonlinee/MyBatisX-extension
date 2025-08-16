package com.baomidou.mybatisx.feat.jpa.common.appender.changer;

import com.baomidou.mybatisx.feat.jpa.common.appender.MxParameterChanger;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The type In parameter changer.
 */
public class InParameterChanger implements MxParameterChanger {
  private static final Logger logger = LoggerFactory.getLogger(InParameterChanger.class);

  @Override
  public List<TxParameter> getParameter(TxParameter txParameter) {
    TxParameter collectionParameter = TxParameter.createCollectionByTxParameter(txParameter);
    return Collections.singletonList(collectionParameter);
  }

  @Override
  public String getTemplateText(String fieldName, LinkedList<TxParameter> parameters, ConditionFieldWrapper conditionFieldWrapper) {
    final TxParameter parameter = parameters.poll();
    if (parameter == null) {
      logger.info("parameter is null, can not getTemplateText");
      return "";
    }
    final String collectionName = parameter.getName();
    String itemName = "item";
    String itemContent = "#{" + itemName + "}";
    // 如果集合的泛型不是空的, 就给遍历的内容加入 jdbcType
    if (parameter.getItemContent(itemName) != null) {
      itemContent = parameter.getItemContent(itemName);
    }
    return fieldName + " " + getIn() + "\n" +
           "<foreach collection=\"" +
           collectionName +
           "\" item=\"item\" open=\"(\" close=\")\" separator=\",\">" + "\n" +
           itemContent + "\n" +
           "</foreach>";
  }

  /**
   * Gets in.
   *
   * @return the in
   */
  @NotNull
  protected String getIn() {
    return "in";
  }
}
