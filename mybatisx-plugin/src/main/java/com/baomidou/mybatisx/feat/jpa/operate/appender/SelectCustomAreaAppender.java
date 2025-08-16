package com.baomidou.mybatisx.feat.jpa.operate.appender;

import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomAreaAppender;

/**
 * @author ls9527
 */
public class SelectCustomAreaAppender extends CustomAreaAppender {


  public SelectCustomAreaAppender(final String area, final String areaType, final SyntaxAppenderFactory syntaxAppenderFactory) {
    super(area, areaType, AreaSequence.AREA, AreaSequence.RESULT, syntaxAppenderFactory);
  }

}
