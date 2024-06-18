package com.baomidou.mybatisx.plugin.ui.components;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * @see DataTypeMappingItem
 */
@Getter
public class DataTypeMappingNode extends DefaultMutableTreeNode {

    @Nullable
    DataTypeMappingItem item;

    public DataTypeMappingNode() {
    }

    public DataTypeMappingNode(@Nullable DataTypeMappingItem item) {
        this.item = item;
    }

    public String getGroup() {
        return item == null ? null : item.getGroup();
    }

    public String getIdentifier() {
        return item == null ? null : item.getIdentifier();
    }

    public String getAnotherGroup() {
        return item == null ? null : item.getAnotherGroup();
    }

    public String getAnotherIdentifier() {
        return item == null ? null : item.getAnotherIdentifier();
    }

    public void add(DataTypeMappingNode newChild) {
        super.add(newChild);
    }

    @Override
    public void add(MutableTreeNode newChild) {
        super.add(newChild);
    }
}
