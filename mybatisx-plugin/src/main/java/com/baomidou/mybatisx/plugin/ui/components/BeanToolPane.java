package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.model.BeanInfo;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class BeanToolPane extends JTabbedPane {

    private final Map<Integer, BeanToolHandler> handlerMap;

    public BeanToolPane() {
        this.setTabPlacement(JTabbedPane.TOP);
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        handlerMap = new HashMap<>();
    }

    public final void addTool(BeanToolHandler provider) {
        addTab(provider.getText(), provider.getRoot());
        handlerMap.put(getTabCount() - 1, provider);
    }

    public void doAction(BeanInfo beanInfo) {
        int index = getSelectedIndex();
        BeanToolHandler provider = handlerMap.get(index);
        if (provider != null) {
            provider.accept(beanInfo);
        }
    }
}
