package com.baomidou.mybatisx.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

  public static String getStacktrace(Throwable throwable) {
    StringWriter writer = new StringWriter();
    throwable.printStackTrace(new PrintWriter(writer));
    return writer.toString();
  }
}
