package com.baomidou.mybatisx.feat.jpa.common.appender;


import com.baomidou.mybatisx.feat.jpa.common.appender.operator.SuffixOperator;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;

import java.util.List;

/**
 * The interface Mx parameter changer.
 */
public interface MxParameterChanger extends SuffixOperator {
  /**
   * Gets parameter.
   *
   * @param txParameter the tx parameter
   * @return the parameter
   */
  List<TxParameter> getParameter(TxParameter txParameter);
}
