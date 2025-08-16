package com.baomidou.mybatisx.plugin.ui.components;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;

@Getter
public class DataTypeNode extends DefaultMutableTreeNode {

  public static final DataTypeNode ROOT = new DataTypeNode(new DataTypeItem("Root"));

  @NotNull
  DataTypeItem item;

  DataTypeNode(@NotNull DataTypeItem item) {
    super(item);
    this.item = item;
  }

  DataTypeNode(String typeGroupId) {
    this(new DataTypeItem(typeGroupId));
  }

  public void addDataType(DataTypeNode dataTypeNode) {
    if (dataTypeNode == null) {
      return;
    }
    add(dataTypeNode);
  }

  public void addChildDataType(String identifier) {
    add(new DataTypeNode(new DataTypeItem(this.item.getGroupId(), identifier)));
  }
}
