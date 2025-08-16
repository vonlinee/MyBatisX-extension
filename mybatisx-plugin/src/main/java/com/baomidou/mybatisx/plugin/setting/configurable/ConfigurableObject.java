package com.baomidou.mybatisx.plugin.setting.configurable;

import lombok.Getter;

@Getter
public abstract class ConfigurableObject {

  volatile boolean modified;

  public void markModified() {
    this.modified = true;
  }
}
