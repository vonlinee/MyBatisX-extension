package com.baomidou.mybatisx.boot;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyBatisTemplateSetupHandler implements AppLifecycleListener {

  private static final Logger logger = Logger.getInstance(MyBatisTemplateSetupHandler.class);

  @Override
  public void appFrameCreated(@NotNull List<String> commandLineArgs) {
    AppLifecycleListener.super.appFrameCreated(commandLineArgs);
  }
}
