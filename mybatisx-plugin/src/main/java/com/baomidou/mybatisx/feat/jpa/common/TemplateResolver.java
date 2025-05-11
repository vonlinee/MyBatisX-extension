package com.baomidou.mybatisx.feat.jpa.common;


import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
import com.baomidou.mybatisx.feat.jpa.common.appender.CompositeAppender;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.intellij.psi.PsiClass;

import java.util.LinkedList;

/**
 * The type Template resolver.
 */
public class TemplateResolver {

  /**
   * Gets template text.
   *
   * @param current               the current
   * @param tableName             the table name
   * @param entityClass           the entity class
   * @param parameters            the parameters
   * @param collector             the collector
   * @param conditionFieldWrapper the condition field wrapper
   * @return the template text
   */
  public String getTemplateText(LinkedList<SyntaxAppender> current,
                                String tableName, PsiClass entityClass,
                                LinkedList<TxParameter> parameters,
                                LinkedList<SyntaxAppenderWrapper> collector,
                                ConditionFieldWrapper conditionFieldWrapper) {
    SyntaxAppender syntaxAppender;
    if (current.size() == 1) {
      syntaxAppender = current.poll();
    } else if (current.size() > 1) {
      syntaxAppender = new CompositeAppender(current.toArray(new SyntaxAppender[0]));
    } else {
      return "";
    }
    return syntaxAppender.getTemplateText(tableName, entityClass, parameters, collector, conditionFieldWrapper);

  }
}
