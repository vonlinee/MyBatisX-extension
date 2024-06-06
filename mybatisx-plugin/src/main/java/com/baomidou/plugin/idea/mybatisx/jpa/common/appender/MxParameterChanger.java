package com.baomidou.plugin.idea.mybatisx.jpa.common.appender;


import com.baomidou.plugin.idea.mybatisx.jpa.common.appender.operator.suffix.SuffixOperator;
import com.baomidou.plugin.idea.mybatisx.jpa.component.TxParameter;

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
