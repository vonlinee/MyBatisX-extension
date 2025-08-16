package com.baomidou.mybatisx.plugin.components;

import java.awt.*;
import java.util.List;

public interface Size {
  int maximumSize(Container var1, List var2, FormLayout.Measure var3, FormLayout.Measure var4, FormLayout.Measure var5);

  boolean compressible();
}
