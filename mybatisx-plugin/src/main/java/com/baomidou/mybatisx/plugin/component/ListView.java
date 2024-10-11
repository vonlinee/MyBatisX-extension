package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.components.JBList;

import javax.swing.*;

/**
 * 列表视图
 *
 * @param <E>
 */
public class ListView<E> extends JBList<E> implements ToolbarDecoratedComponent<ListView<E>> {

    public ListView() {
        super(new DefaultListModel<>());
    }

    public DefaultListModel<E> getListModel() {
        return (DefaultListModel<E>) getModel();
    }

    public void addItem(E item) {
        getListModel().addElement(item);
    }

    public E getItem(int index) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        return getModel().getElementAt(index);
    }
}
