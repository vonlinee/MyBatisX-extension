package com.baomidou.mybatisx.plugin.component;

import com.baomidou.mybatisx.util.CollectionUtils;

import java.util.Collection;

public class SimpleComboBox<E> extends com.intellij.openapi.ui.ComboBox<E> {

    public void addItems(Collection<E> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            for (E item : items) {
                addItem(item);
            }
        }
    }
}
