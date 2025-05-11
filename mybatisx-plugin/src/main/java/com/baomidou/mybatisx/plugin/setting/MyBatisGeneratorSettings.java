package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.util.MyBatisXPlugin;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * MyBatis 代码生成器配置
 */
@Service
@State(name = "GeneratorSettings", storages = @Storage(value = MyBatisXPlugin.PERSISTENT_STATE_FILE))
public final class MyBatisGeneratorSettings implements PersistentStateComponent<MyBatisGeneratorSettings.State> {

  private final State state = new State();

  @Override
  public MyBatisGeneratorSettings.@NotNull State getState() {
    return this.state;
  }

  @Override
  public void loadState(@NotNull State state) {
    XmlSerializerUtil.copyBean(state, this.state);
  }

  public static class State {

  }
}
