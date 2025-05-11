package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.feat.bean.BeanInfo;

import javax.swing.*;

public interface BeanToolHandler {

  JComponent getRoot();

  String getId();

  String getText();

  void accept(BeanInfo bean);
}
