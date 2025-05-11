package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.model.ComboBoxItem;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DataTypeItem implements MultableDataType, ComboBoxItem {

  /**
   * 类型组唯一ID
   */
  private String groupId;

  /**
   * 单个组内唯一
   */
  private String identifier;

  private String name;

  private int minLength = -1;

  private int maxLength = -1;

  public DataTypeItem(String groupId) {
    this.groupId = groupId;
  }

  public DataTypeItem(String groupId, String identifier) {
    this.groupId = groupId;
    this.identifier = identifier;
  }

  public static DataTypeItem parse(String name) {
    String[] groupIdAndDataType = name.split(">");
    if (groupIdAndDataType.length == 2) {
      return new DataTypeItem(groupIdAndDataType[0], groupIdAndDataType[1]);
    }
    throw new IllegalArgumentException("pattern: ${groupId}>${dataTypeId}");
  }

  public static List<DataTypeItem> of(String group, String... types) {
    List<DataTypeItem> items = new ArrayList<>(types.length);
    for (String type : types) {
      items.add(new DataTypeItem(group, type));
    }
    return items;
  }

  @Override
  public @NotNull String getName() {
    if (this.maxLength != -1 && this.minLength != -1) {
      return this.name + "(" + this.minLength + ", " + this.maxLength + ")";
    }
    if (this.name == null) {
      return this.identifier;
    }
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Object getValue() {
    return groupId;
  }

  @Override
  public @NotNull String getGroupIdentifier() {
    return groupId;
  }

  @Override
  public void setTypeGroup(String typeGroup) {
    setGroupId(typeGroup);
  }

  @Override
  public void setLength(int minLength, int maxLength) {
    setMinLength(minLength);
    setMaxLength(maxLength);
  }
}
